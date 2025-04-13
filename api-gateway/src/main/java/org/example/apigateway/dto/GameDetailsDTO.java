package org.example.apigateway.dto;

import java.util.List;

public class GameDetailsDTO {
    public GameDTO game;
    public List<PlayerDTO> players;

    public GameDetailsDTO(GameDTO game, List<PlayerDTO> players) {
        this.game = game;
        this.players = players;
    }
}
