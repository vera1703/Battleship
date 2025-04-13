package org.example.player.dto;

import lombok.Getter;
import org.example.player.domain.Player;


@Getter
public class PlayerDTO {

    private Long id;
    private String name;
    private Long gameId;

    public PlayerDTO() {}

    public PlayerDTO(Long id, String name, Long gameId) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
    }

    public static PlayerDTO from(Player player) {
        return new PlayerDTO(
                player.getId(),
                player.getName(),
                player.getGameId()
        );
    }
}
