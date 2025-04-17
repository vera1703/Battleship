package org.example.player.application;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.player.domain.Player;
import org.example.player.persistence.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final RestTemplate restTemplate;
    private final PlayerEventSender playerEventSender;

    public PlayerService(PlayerRepository playerRepository,
                         RestTemplate restTemplate,
                         PlayerEventSender playerEventSender) {
        this.playerRepository = playerRepository;
        this.restTemplate = restTemplate;
        this.playerEventSender = playerEventSender;
    }

    @CircuitBreaker(name = "gameService", fallbackMethod = "fallbackAddPlayer")
    public Player addPlayer(Long gameId, String playerName) {
        long currentPlayers = playerRepository.countByGameId(gameId);

        boolean allowed = restTemplate.getForObject(
                "http://game/games/" + gameId + "/can-join?playerCount=" + currentPlayers,
                Boolean.class
        );

        if (!allowed) {
            throw new IllegalStateException("Game is full or already started.");
        }

        Player player = new Player(playerName, gameId);
        playerRepository.save(player);
        playerEventSender.sendPlayerJoinedEvent(gameId, playerName);

        restTemplate.postForEntity(
                "http://game/games/" + gameId + "/check-start?playerCount=" + (currentPlayers + 1),
                null,
                Void.class
        );

        return player;
    }

    public Long getGameIdForPlayer(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        return player.getGameId();
    }

    public Long findOpponentId(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Long gameId = player.getGameId();

        return playerRepository.findAll().stream()
                .filter(p -> !p.getId().equals(playerId) && p.getGameId().equals(gameId))
                .map(Player::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Opponent not found"));
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
}
