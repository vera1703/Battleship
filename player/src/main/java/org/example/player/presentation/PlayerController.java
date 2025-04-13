package org.example.player.presentation;

import org.example.player.application.PlayerService;
import org.example.player.domain.Player;
import org.example.player.dto.PlayerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/{gameId}/{playerName}")
    public ResponseEntity<PlayerDTO> addPlayer(@PathVariable Long gameId, @PathVariable String playerName) {
        Player player = playerService.addPlayer(gameId, playerName);
        return ResponseEntity.ok(PlayerDTO.from(player));
    }

    @GetMapping("/{playerId}/gameId")
    public ResponseEntity<Long> getGameIdByPlayer(@PathVariable Long playerId) {
        Long gameId = playerService.getGameIdForPlayer(playerId);
        return ResponseEntity.ok(gameId);
    }

    @GetMapping("/{playerId}/opponent")
    public ResponseEntity<Long> getOpponentId(@PathVariable Long playerId) {
        Long opponentId = playerService.findOpponentId(playerId);
        return ResponseEntity.ok(opponentId);
    }
}
