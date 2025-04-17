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
@Tag(name = "Game Details", description = "combines game, player and ship")
public class GameDetailsController {

    private final WebClient.Builder webClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(GameDetailsController.class);

    public GameDetailsController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/game/{gameId}")
    @Operation(summary = "get game with all players and ships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "loaded successfully"),
            @ApiResponse(responseCode = "500", description = "error loading data")
    })
    @CircuitBreaker(name = "gameDetailsCB", fallbackMethod = "fallbackGameDetails")
    public Mono<GameDetailsDTO> getGameDetails(@PathVariable Long gameId) {
        log.info("inquiry started: /details/game/{}", gameId);

        WebClient client = webClientBuilder.build();

        Mono<GameDTO> gameMono = client.get()
                .uri("http://game/games/" + gameId)
                .retrieve()
                .bodyToMono(GameDTO.class)
                .doOnNext(game -> log.info("game loaded: {}", game))
                .doOnError(e -> log.error("error loading game: {}", e.toString()));

        Mono<List<PlayerDTO>> playersMono = client.get()
                .uri("http://player/players")
                .retrieve()
                .bodyToFlux(PlayerDTO.class)
                .filter(player -> {
                    boolean relevant = player.gameId() != null && gameId.equals(player.gameId());
                    if (relevant) log.info("player belongs to game: {}", player);
                    return relevant;
                })
                .doOnError(e -> log.error("error loading player: {}", e.toString()))
                .collectList();


        Mono<List<ShipDTO>> shipsMono = client.get()
                .uri("http://ship/ships")
                .retrieve()
                .bodyToFlux(ShipDTO.class)
                .doOnNext(ship -> log.info("ship loaded: {}", ship))
                .doOnError(e -> log.error("error loading ships: {}", e.toString()))
                .collectList();


        return Mono.zip(gameMono, playersMono, shipsMono)
                .map(tuple -> {
                    GameDTO game = tuple.getT1();
                    List<PlayerDTO> players = tuple.getT2();
                    List<ShipDTO> allShips = tuple.getT3();

                    List<Long> playerIds = players.stream().map(PlayerDTO::id).toList();
                    List<ShipDTO> relevantShips = allShips.stream()
                            .filter(ship -> ship.playerId() != null && playerIds.contains(ship.playerId()))
                            .toList();

                    log.info("successfully combined data");
                    return new GameDetailsDTO(game, players, relevantShips);
                });
    }

    public Mono<GameDetailsDTO> fallbackGameDetails(Long gameId, Throwable throwable) {
        log.error("Fallback activated for gameId={}: {}", gameId, throwable.toString(), throwable);
        GameDTO fallbackGame = new GameDTO(gameId, "Unavailable");
        return Mono.just(new GameDetailsDTO(fallbackGame, List.of(), List.of()));
    }
}
