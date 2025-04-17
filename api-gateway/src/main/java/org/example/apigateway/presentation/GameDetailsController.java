package org.example.apigateway.presentation;

import org.example.apigateway.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/details")
@Tag(name = "Game Details", description = "Kombiniert Game-, Player- und Ship-Daten")
public class GameDetailsController {

    private final WebClient.Builder webClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(GameDetailsController.class);

    public GameDetailsController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/game/{gameId}")
    @Operation(summary = "Hole ein Spiel mit allen zugehörigen Spielern und Schiffen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich geladen"),
            @ApiResponse(responseCode = "500", description = "Fehler beim Abrufen der Daten")
    })
    @CircuitBreaker(name = "gameDetailsCB", fallbackMethod = "fallbackGameDetails")
    public Mono<GameDetailsDTO> getGameDetails(@PathVariable Long gameId) {
        log.info("➡️ Anfrage gestartet: /details/game/{}", gameId);

        WebClient client = webClientBuilder.build();

        // 1. Spiel holen
        Mono<GameDTO> gameMono = client.get()
                .uri("http://game/games/" + gameId)
                .retrieve()
                .bodyToMono(GameDTO.class)
                .doOnNext(game -> log.info("✅ Spiel geladen: {}", game))
                .doOnError(e -> log.error("❌ Fehler beim Laden des Spiels: {}", e.toString()));

        // 2. Alle Spieler holen
        Mono<List<PlayerDTO>> playersMono = client.get()
                .uri("http://player/players")
                .retrieve()
                .bodyToFlux(PlayerDTO.class)
                .filter(player -> {
                    boolean relevant = player.gameId() != null && gameId.equals(player.gameId());
                    if (relevant) log.info("👤 Spieler gehört zum Spiel: {}", player);
                    return relevant;
                })
                .doOnError(e -> log.error("❌ Fehler beim Laden der Spieler: {}", e.toString()))
                .collectList();

        // 3. Alle Schiffe holen
        Mono<List<ShipDTO>> shipsMono = client.get()
                .uri("http://ship/ships")
                .retrieve()
                .bodyToFlux(ShipDTO.class)
                .doOnNext(ship -> log.info("🚢 Schiff geladen: {}", ship))
                .doOnError(e -> log.error("❌ Fehler beim Laden der Schiffe: {}", e.toString()))
                .collectList();

        // 4. Kombinieren und filtern
        return Mono.zip(gameMono, playersMono, shipsMono)
                .map(tuple -> {
                    GameDTO game = tuple.getT1();
                    List<PlayerDTO> players = tuple.getT2();
                    List<ShipDTO> allShips = tuple.getT3();

                    List<Long> playerIds = players.stream().map(PlayerDTO::id).toList();
                    List<ShipDTO> relevantShips = allShips.stream()
                            .filter(ship -> ship.playerId() != null && playerIds.contains(ship.playerId()))
                            .toList();

                    log.info("✅ Kombinierte Daten erfolgreich erstellt");
                    return new GameDetailsDTO(game, players, relevantShips);
                });
    }

    // Fallback bei Fehler
    public Mono<GameDetailsDTO> fallbackGameDetails(Long gameId, Throwable throwable) {
        log.error("❌ Fallback aktiviert für gameId={}: {}", gameId, throwable.toString(), throwable);
        GameDTO fallbackGame = new GameDTO(gameId, "Unavailable");
        return Mono.just(new GameDetailsDTO(fallbackGame, List.of(), List.of()));
    }
}
