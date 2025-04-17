package org.example.apigateway.dto;

public class GameDetailsDTO {

    private GameDTO game;
    private PlayerDTO player;

    public GameDetailsDTO() {
    }

    public GameDetailsDTO(GameDTO game, PlayerDTO player) {
        this.game = game;
        this.player = player;
    }

    // Getter & Setter
    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }
}
