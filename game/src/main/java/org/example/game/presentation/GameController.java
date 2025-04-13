package org.example.game.presentation;

import org.example.game.application.GameService;
import org.example.game.domain.Game;
import org.example.game.domain.GameStatus;
import org.example.game.dto.GameDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameDTO> createGame() {
        Game created = gameService.createGame();
        return ResponseEntity.ok(GameDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        return gameService.getGame(id)
                .map(GameDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Boolean> isGameFinished(@PathVariable Long id) {
        return gameService.getGame(id)
                .map(game -> ResponseEntity.ok(game.getStatus() == GameStatus.FINISHED))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/can-join")
    public ResponseEntity<Boolean> canJoin(@PathVariable Long id, @RequestParam int playerCount) {
        return gameService.getGame(id)
                .map(game -> ResponseEntity.ok(game.getStatus() == GameStatus.WAITING && playerCount < 2))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/check-start")
    public ResponseEntity<Void> checkStart(@PathVariable Long id, @RequestParam int playerCount) {
        gameService.getGame(id).ifPresent(game -> {
            if (playerCount == 2 && game.getStatus() == GameStatus.WAITING) {
                game.changeGameStatus(GameStatus.IN_PROGRESS);
                gameService.save(game);
                gameService.sendGameStartedEvent(game);
            }
        });
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<Object> finishGame(@PathVariable Long id) {
        return gameService.getGame(id)
                .map(game -> {
                    game.changeGameStatus(GameStatus.FINISHED);
                    gameService.save(game);
                    gameService.sendGameFinishedEvent(game, null);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
