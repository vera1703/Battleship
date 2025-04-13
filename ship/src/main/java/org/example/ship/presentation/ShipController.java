package org.example.ship.presentation;

import org.example.ship.application.ShipService;
import org.example.ship.domain.Direction;
import org.example.ship.dto.ShipDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ships")
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
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
}
