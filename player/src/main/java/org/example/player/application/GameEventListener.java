package org.example.player.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GameEventListener {

    private final ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(queues = "${battleship.queue}")
    public void handleGameEvents(Message message) throws Exception {
        String json = new String(message.getBody());
        JsonNode root = mapper.readTree(json);
        String eventType = root.get("eventType").asText();

        if ("GAME_CREATED".equals(eventType)) {
            String gameId = root.get("gameId").asText();
            System.out.println("Game created received: " + gameId);
        }

        if ("GAME_STARTED".equals(eventType)) {
            String gameId = root.get("gameId").asText();
            System.out.println("Game started received: " + gameId);
        }
    }
}
