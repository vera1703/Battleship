package org.example.game.dto;

import lombok.Getter;
import org.example.game.domain.Game;
import org.example.game.domain.GameStatus;


@Getter
public class GameDTO {
    private final Long id;
    private final GameStatus status;

    public GameDTO(Long id, GameStatus status) {
        this.id = id;
        this.status = status;
    }

    public static GameDTO from(Game game) {
        return new GameDTO(game.getId(), game.getStatus());
    }
}
