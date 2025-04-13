package org.example.game.application;

import org.example.game.dto.GameCreatedEvent;
import org.example.game.dto.GameFinishedEvent;
import org.example.game.dto.GameStartedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GameEventSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${battleship.exchange}")
    private String exchange;

    @Value("${battleship.routingkey}")
    private String routingKey;

    public GameEventSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendGameCreatedEvent(String gameId) {
        GameCreatedEvent event = new GameCreatedEvent(gameId);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        System.out.println("Event sent: " + event.getEventType() + " – gameID: " + gameId);
    }

    public void sendGameStartedEvent(String gameId) {
        GameStartedEvent event = new GameStartedEvent(gameId);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        System.out.println("Event sent: " + event.getEventType() + " – gameID: " + gameId);
    }

    public void sendGameFinishedEvent(String gameId, String winnerPlayerId) {
        GameFinishedEvent event = new GameFinishedEvent(gameId, winnerPlayerId);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        System.out.println("Event sent: " + event.getEventType() + " – gameID: " + gameId + " – Winner: " + winnerPlayerId);
    }

}

