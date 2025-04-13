package org.example.player.application;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlayerEventSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${battleship.exchange}")
    private String exchange;

    @Value("${battleship.routingkey}")
    private String routingKey;

    public PlayerEventSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPlayerJoinedEvent(Long gameId, String playerName) {
        Map<String, String> event = new HashMap<>();
        event.put("eventType", "PLAYER_JOINED");
        event.put("gameId", gameId.toString());
        event.put("playerName", playerName);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        System.out.println("Player joined: " + playerName + " GameID: " + gameId);
    }

}
