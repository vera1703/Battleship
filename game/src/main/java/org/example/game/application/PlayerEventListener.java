package org.example.game.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PlayerEventListener {

    private final ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(queues = "${battleship.queue}")
    public void handlePlayerJoined(Message message) throws Exception {
        String json = new String(message.getBody());
        JsonNode root = mapper.readTree(json);

        String eventType = root.get("eventType").asText();

        if ("PLAYER_JOINED".equals(eventType)) {
            String gameId = root.get("gameId").asText();
            String playerName = root.get("playerName").asText();
            System.out.println("Player joined: " + playerName + " GameID: " + gameId);
        }
    }
}
