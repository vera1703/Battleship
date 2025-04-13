package org.example.apigateway.controller;

import org.example.apigateway.service.GameDetailsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/game-details")
public class GameDetailsController {

    private final GameDetailsService gameDetailsService;

    public GameDetailsController(GameDetailsService gameDetailsService) {
        this.gameDetailsService = gameDetailsService;
    }

    @GetMapping("/{gameId}")
    public Mono<Map<String, Object>> getDetails(@PathVariable Long gameId) {
        return gameDetailsService.getGameDetails(gameId);
    }
}
