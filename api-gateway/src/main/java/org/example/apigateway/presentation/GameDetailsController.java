package org.example.apigateway.presentation;

import org.example.apigateway.dto.GameDTO;
import org.example.apigateway.dto.PlayerDTO;
import org.example.apigateway.dto.GameDetailsDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/details")
@Tag(name = "Game Details", description = "Stellt kombinierte Daten aus mehreren Microservices bereit")
public class GameDetailsController {

    private final WebClient.Builder webClientBuilder;

    public GameDetailsController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/game/{gameId}")
    @Operation(summary = "Hole kombinierte Spieldetails (Game + Player)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daten erfolgreich geladen"),
            @ApiResponse(responseCode = "500", description = "Fehler beim Abrufen der Daten")
    })
    @CircuitBreaker(name = "gameDetailsCircuitBreaker", fallbackMethod = "fallbackGameDetails")
    public Mono<GameDetailsDTO> getGameDetails(@PathVariable String gameId) {
        // Request an game-service
        Mono<GameDTO> gameMono = webClientBuilder.build()
                .get()
                .uri("http://game-service/games/" + gameId)
                .retrieve()
                .bodyToMono(GameDTO.class);

        // Request an player-service
        Mono<PlayerDTO> playerMono = webClientBuilder.build()
                .get()
                .uri("http://player-service/players/" + gameId)
                .retrieve()
                .bodyToMono(PlayerDTO.class);

        // Kombiniere beide Antworten in ein DTO
        return Mono.zip(gameMono, playerMono)
                .map(tuple -> new GameDetailsDTO(tuple.getT1(), tuple.getT2()));
    }

    // Fallback-Methode, wenn einer der Services nicht verfügbar ist
    public Mono<GameDetailsDTO> fallbackGameDetails(String gameId, Throwable throwable) {
        GameDTO fallbackGame = new GameDTO("N/A", "Spiel nicht verfügbar");
        PlayerDTO fallbackPlayer = new PlayerDTO("N/A", "Spieler nicht verfügbar");

        return Mono.just(new GameDetailsDTO(fallbackGame, fallbackPlayer));
    }
}
