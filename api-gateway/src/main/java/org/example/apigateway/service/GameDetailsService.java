package org.example.apigateway.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GameDetailsService {

    private final WebClient.Builder webClientBuilder;

    public GameDetailsService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Map<String, Object>> getGameDetails(Long gameId) {
        Mono<Object> game = webClientBuilder.build()
                .get()
                .uri("http://GAME/games/" + gameId)
                .retrieve()
                .bodyToMono(Object.class);

        Mono<Object> players = webClientBuilder.build()
                .get()
                .uri("http://PLAYER/players/by-game/" + gameId)
                .retrieve()
                .bodyToMono(Object.class);

        return Mono.zip(game, players).map(tuple -> Map.of(
                "game", tuple.getT1(),
                "players", tuple.getT2()
        ));
    }
}
