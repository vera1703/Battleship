package org.example.player.presentation;

import org.example.player.application.GuessService;
import org.example.player.domain.Guess;
import org.example.player.dto.GuessDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/guesses")
public class GuessController {

    private final GuessService guessService;

    public GuessController(GuessService guessService) {
        this.guessService = guessService;
    }

    @PostMapping("/{playerId}")
    public ResponseEntity<GuessDTO> makeGuess(@PathVariable Long playerId,
                                              @RequestParam int x,
                                              @RequestParam int y) {
        Guess guess = guessService.makeGuess(playerId, x, y);
        return ResponseEntity.ok(GuessDTO.from(guess));
    }
}
