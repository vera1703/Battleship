package org.example.player.dto;

import lombok.Getter;
import org.example.player.domain.Guess;


@Getter
public class GuessDTO {

    private Long id;
    private int x;
    private int y;
    private boolean hit;
    private Long playerId;

    public GuessDTO() {}

    public GuessDTO(Long id, int x, int y, boolean hit, Long playerId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.hit = hit;
        this.playerId = playerId;
    }

    public static GuessDTO from(Guess guess) {
        return new GuessDTO(
                guess.getId(),
                guess.getX(),
                guess.getY(),
                guess.isHit(),
                guess.getPlayer().getId()
        );
    }
}
