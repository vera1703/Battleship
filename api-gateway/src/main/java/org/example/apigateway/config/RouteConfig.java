package org.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // game-service → passt zu @RequestMapping("/games")
                .route("game-service", r -> r
                        .path("/games/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("gameServiceCB")
                                        .setFallbackUri("forward:/fallback/game"))
                        )
                        .uri("lb://game-service")
                )

                // player-service (Spieler) → @RequestMapping("/players")
                .route("player-service", r -> r
                        .path("/players/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("playerServiceCB")
                                        .setFallbackUri("forward:/fallback/player"))
                        )
                        .uri("lb://player-service")
                )

                // player-service (Guesses) → @RequestMapping("/guesses")
                .route("guesses-service", r -> r
                        .path("/guesses/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("guessesServiceCB")
                                        .setFallbackUri("forward:/fallback/player"))
                        )
                        .uri("lb://player-service")
                )

                // ship-service → @RequestMapping("/ships")
                .route("ship-service", r -> r
                        .path("/ships/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("shipServiceCB")
                                        .setFallbackUri("forward:/fallback/ship"))
                        )
                        .uri("lb://ship-service")
                )

                .build();
    }
}
