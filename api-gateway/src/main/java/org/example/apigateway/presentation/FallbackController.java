package org.example.apigateway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/game")
    public ResponseEntity<String> gameFallback() {
        return ResponseEntity.ok("Game-Service ist momentan nicht verfügbar. Bitte später erneut versuchen.");
    }

    @GetMapping("/player")
    public ResponseEntity<String> playerFallback() {
        return ResponseEntity.ok("Player-Service ist momentan nicht verfügbar.");
    }

    @GetMapping("/ship")
    public ResponseEntity<String> shipFallback() {
        return ResponseEntity.ok("Ship-Service ist momentan nicht erreichbar.");
    }
}
