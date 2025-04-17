package org.example.ship.presentation;

import org.example.ship.application.ShipService;
import org.example.ship.domain.Direction;
import org.example.ship.domain.Ship;
import org.example.ship.dto.ShipCellDTO;
import org.example.ship.dto.ShipDTO;
import org.example.ship.persistence.ShipRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ships")
public class ShipController {

    private final ShipService shipService;
    private final ShipRepository shipRepository;

    public ShipController(ShipService shipService, ShipRepository shipRepository) {
        this.shipService = shipService;
        this.shipRepository = shipRepository;
    }

    @PostMapping("/{playerId}")
    public ResponseEntity<ShipDTO> placeShip(@PathVariable Long playerId,
                                             @RequestParam int x,
                                             @RequestParam int y,
                                             @RequestParam int length,
                                             @RequestParam Direction direction) {
        ShipDTO shipDTO = shipService.placeShip(playerId, x, y, length, direction);
        return ResponseEntity.ok(shipDTO);
    }

    @GetMapping("/check-hit")
    public ResponseEntity<Boolean> checkHit(@RequestParam int x,
                                            @RequestParam int y,
                                            @RequestParam Long playerId) {
        boolean hit = shipService.checkHitOnOpponent(x, y, playerId);
        return ResponseEntity.ok(hit);
    }

    @GetMapping("/opponent-defeated")
    public ResponseEntity<Boolean> opponentDefeated(@RequestParam Long playerId) {
        boolean defeated = shipService.isOpponentDefeated(playerId);
        return ResponseEntity.ok(defeated);
    }

    @PostMapping("/setup")
    public ResponseEntity<Void> setupShipsForGame(@RequestParam Long gameId) {
        System.out.println("ShipService: Setup for game " + gameId + " started.");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<ShipDTO> getAllShips() {
        List<Ship> ships = shipRepository.findAll();
        return ships.stream()
                .map(this::toDTO)
                .toList();
    }

    private ShipDTO toDTO(Ship ship) {
        List<ShipCellDTO> cellDTOs = ship.getCells().stream()
                .map(cell -> new ShipCellDTO(cell.getX(), cell.getY(), cell.isHit()))
                .toList();

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
