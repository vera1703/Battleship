package org.example.apigateway.dto;

public record PlayerDTO(
        Long id,
        String name,
        Long gameId
) {}
