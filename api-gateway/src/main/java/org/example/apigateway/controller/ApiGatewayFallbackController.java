package org.example.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class ApiGatewayFallbackController {

    @GetMapping("/game")
    public ResponseEntity<String> gameFallback() {
        return ResponseEntity.ok("Game-Service temporär nicht erreichbar.");
    }

    @GetMapping("/player")
    public ResponseEntity<String> playerFallback() {
        return ResponseEntity.ok("Player-Service temporär nicht erreichbar.");
    }

    @GetMapping("/ship")
    public ResponseEntity<String> shipFallback() {
        return ResponseEntity.ok("Ship-Service temporär nicht erreichbar.");
    }
}
