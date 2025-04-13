package org.example.game.application;

import org.example.game.domain.Game;
import org.example.game.persistence.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameEventSender gameEventSender;
    private final RestTemplate restTemplate;

    public GameService(GameRepository gameRepository, GameEventSender gameEventSender, RestTemplate restTemplate) {
        this.gameRepository = gameRepository;
        this.gameEventSender = gameEventSender;
        this.restTemplate = restTemplate;
    }

    public void notifyShipService(Long gameId) {
        try {
            restTemplate.postForObject("http://ship/ships/setup?gameId=" + gameId, null, Void.class);
            System.out.println("GameService: ShipService called.");
        } catch (Exception ex) {
            System.out.println("error calling ShipService: " + ex.getMessage());
        }
    }

    public Game createGame() {
        Game game = gameRepository.save(new Game());
        gameEventSender.sendGameCreatedEvent(game.getId().toString());
        notifyShipService(game.getId());
        return game;
    }

    public Optional<Game> getGame(Long id) {
        return gameRepository.findById(id);
    }

    public void save(Game game) {
        gameRepository.save(game);
    }

    public void sendGameStartedEvent(Game game) {
        gameEventSender.sendGameStartedEvent(game.getId().toString());
    }

    public void sendGameFinishedEvent(Game game, Long winnerPlayerId) {
        String winnerId = (winnerPlayerId != null) ? winnerPlayerId.toString() : "unknown";
        gameEventSender.sendGameFinishedEvent(game.getId().toString(), winnerId);
    }
}
