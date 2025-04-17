package org.example.apigateway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/game")
    public Mono<ResponseEntity<Map<String, String>>> gameServiceFallback() {
        return buildResponse("Game service is currently unavailable");
    }

    @GetMapping("/player")
    public Mono<ResponseEntity<Map<String, String>>> playerServiceFallback() {
        return buildResponse("Player service is currently unavailable");
    }

    @GetMapping("/ship")
    public Mono<ResponseEntity<Map<String, String>>> shipServiceFallback() {
        return buildResponse("Ship service is currently unavailable");
    }

    private Mono<ResponseEntity<Map<String, String>>> buildResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return Mono.just(ResponseEntity.ok(response));
    }
}
