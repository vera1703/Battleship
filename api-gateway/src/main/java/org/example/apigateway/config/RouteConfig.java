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

                .route("game", r -> r.path("/games/**")
                        .filters(f -> f.circuitBreaker(c -> c
                                .setName("gameServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallback/game")))
                        .uri("lb://game"))

                .route("player-players", r -> r.path("/players/**")
                        .filters(f -> f.circuitBreaker(c -> c
                                .setName("playerServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallback/player")))
                        .uri("lb://player"))

                .route("player-guesses", r -> r.path("/guesses/**")
                        .filters(f -> f.circuitBreaker(c -> c
                                .setName("guessServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallback/player")))
                        .uri("lb://player"))

                .route("ship", r -> r.path("/ships/**")
                        .filters(f -> f.circuitBreaker(c -> c
                                .setName("shipServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallback/ship")))
                        .uri("lb://ship"))

                .route("openapi", r -> r.path("/v3/api-docs/**")
                        .uri("lb://api-gateway"))
                .route("swagger-ui", r -> r.path("/swagger-ui/**")
                        .uri("lb://api-gateway"))

                .build();
    }
}
