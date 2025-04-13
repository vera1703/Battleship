package org.example.ship.dto;

import lombok.Getter;
import org.example.ship.domain.Direction;
import org.example.ship.domain.Ship;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class ShipDTO {

    private Long id;
    private int x;
    private int y;
    private int length;
    private boolean sunk;
    private Direction direction;
    private Long playerId;

    private List<ShipCellDTO> cells;

    public ShipDTO() {}

    public ShipDTO(Long id, int x, int y, int length, boolean sunk, Direction direction,
                   Long playerId, List<ShipCellDTO> cells) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.length = length;
        this.sunk = sunk;
        this.direction = direction;
        this.playerId = playerId;
        this.cells = cells;
    }

    public ShipDTO(Long id, int x, int y, int length, boolean sunk, Direction direction, Long playerId) {
    }

    public static ShipDTO from(Ship ship) {
        List<ShipCellDTO> cellDTOs = ship.getCells()
                .stream()
                .map(ShipCellDTO::from)
                .collect(Collectors.toList());

        return new ShipDTO(
                ship.getId(),
                ship.getX(),
                ship.getY(),
                ship.getLength(),
                ship.isSunk(),
                ship.getDirection(),
                ship.getPlayerId(),
                cellDTOs
        );
    }
}
