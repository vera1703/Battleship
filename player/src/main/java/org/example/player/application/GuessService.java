package org.example.player.application;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.player.domain.Guess;
import org.example.player.domain.Player;
import org.example.player.persistence.GuessRepository;
import org.example.player.persistence.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GuessService {

    private final GuessRepository guessRepository;
    private final PlayerRepository playerRepository;
    private final RestTemplate restTemplate;

    public GuessService(GuessRepository guessRepository,
                        PlayerRepository playerRepository,
                        RestTemplate restTemplate) {
        this.guessRepository = guessRepository;
        this.playerRepository = playerRepository;
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "gameService", fallbackMethod = "fallbackGuess")
    public Guess makeGuess(Long playerId, int x, int y) {
        validateCoordinates(x, y);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Long gameId = player.getGameId();

        Boolean isFinished = restTemplate.getForObject(
                "http://game/games/" + gameId + "/status",
                Boolean.class
        );

        if (Boolean.TRUE.equals(isFinished)) {
            throw new IllegalStateException("The game is already finished.");
        }

        Boolean hit = restTemplate.getForObject(
                "http://ship/ships/check-hit?x=" + x + "&y=" + y + "&playerId=" + playerId,
                Boolean.class
        );

        Guess guess = Guess.of(player, x, y, Boolean.TRUE.equals(hit));
        guessRepository.save(guess);

        Boolean opponentLost = restTemplate.getForObject(
                "http://ship/ships/opponent-defeated?playerId=" + playerId,
                Boolean.class
        );

        if (Boolean.TRUE.equals(opponentLost)) {
            restTemplate.postForEntity(
                    "http://game/games/" + gameId + "/finish",
                    null,
                    Void.class
            );
        }

        return guess;
    }

    private void validateCoordinates(int x, int y) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10) {
            throw new IllegalArgumentException("Coordinates must be between 0 and 9.");
        }
    }
}

