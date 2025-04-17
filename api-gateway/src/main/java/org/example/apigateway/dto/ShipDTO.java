package org.example.apigateway.dto;

import java.util.List;

public record ShipDTO(
        Long id,
        int x,
        int y,
        int length,
        boolean sunk,
        String direction,
        Long playerId,
        List<ShipCellDTO> cells
) {}
