package org.example.game.dto;

import java.io.Serializable;
import java.time.Instant;

public class GameStartedEvent implements Serializable {

    private String eventType;
    private String gameId;
    private String timestamp;

    public GameStartedEvent() {
    }

    public GameStartedEvent(String gameId) {
        this.eventType = "GAME_STARTED";
        this.gameId = gameId;
        this.timestamp = Instant.now().toString();
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

