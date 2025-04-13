package org.example.game.dto;

public class GameFinishedEvent {

    private final String eventType = "GAME_FINISHED";
    private String gameId;
    private String winnerPlayerId;

    public GameFinishedEvent() {
    }

    public GameFinishedEvent(String gameId, String winnerPlayerId) {
        this.gameId = gameId;
        this.winnerPlayerId = winnerPlayerId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getWinnerPlayerId() {
        return winnerPlayerId;
    }

    public void setWinnerPlayerId(String winnerPlayerId) {
        this.winnerPlayerId = winnerPlayerId;
    }
}

