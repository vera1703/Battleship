package org.example.player.dto;

public class PlayerJoinedEvent {
    private final String eventType = "PLAYER_JOINED";
    private final String gameId;
    private final String playerName;

    public PlayerJoinedEvent(String gameId, String playerName) {
        this.gameId = gameId;
        this.playerName = playerName;
    }

    public String getEventType() {
        return eventType;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
