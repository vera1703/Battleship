package org.example.apigateway.controller;

import org.example.apigateway.dto.GameDTO;
import org.example.apigateway.dto.PlayerDTO;
import org.example.apigateway.dto.GameDetailsDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/composite")
public class GameCompositeController {

    private final WebClient.Builder webClientBuilder;

    public GameCompositeController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/game-details/{gameId}")
    public Mono<GameDetailsDTO> getGameDetails(@PathVariable Long gameId) {
        Mono<GameDTO> gameMono = webClientBuilder.build()
                .get().uri("http://game/games/" + gameId)
                .retrieve().bodyToMono(GameDTO.class)
                .onErrorResume(e -> {
                    System.out.println("Game service error: " + e.getMessage());
                    return Mono.empty();
                });

        Mono<List<PlayerDTO>> playersMono = webClientBuilder.build()
                .get().uri("http://player/players/by-game/" + gameId)
                .retrieve().bodyToFlux(PlayerDTO.class).collectList()
                .onErrorResume(e -> {
                    System.out.println("Player service error: " + e.getMessage());
                    return Mono.just(List.of());
                });

        return Mono.zip(gameMono, playersMono)
                .map(tuple -> new GameDetailsDTO(tuple.getT1(), tuple.getT2()));
    }
}
