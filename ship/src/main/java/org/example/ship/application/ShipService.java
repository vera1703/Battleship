package org.example.ship.application;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.ship.domain.Direction;
import org.example.ship.domain.Ship;
import org.example.ship.domain.ShipCell;
import org.example.ship.dto.ShipCellDTO;
import org.example.ship.dto.ShipDTO;
import org.example.ship.persistence.ShipCellRepository;
import org.example.ship.persistence.ShipRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ShipService {

    private final ShipRepository shipRepository;
    private final ShipCellRepository shipCellRepository;
    private final RestTemplate restTemplate;

    public ShipService(ShipRepository shipRepository,
                       ShipCellRepository shipCellRepository,
                       RestTemplate restTemplate) {
        this.shipRepository = shipRepository;
        this.shipCellRepository = shipCellRepository;
        this.restTemplate = restTemplate;
    }

    public ShipDTO placeShip(Long playerId, int x, int y, int length, Direction direction) {
        Long gameId = getGameIdForPlayer(playerId);

        Boolean isGameFinished = restTemplate.getForObject(
                "http://game/games/" + gameId + "/status",
                Boolean.class
        );

        if (Boolean.TRUE.equals(isGameFinished)) {
            throw new IllegalStateException("The game is already finished.");
        }

        List<ShipCell> shipCells = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int currentX = (direction == Direction.HORIZONTAL) ? x + i : x;
            int currentY = (direction == Direction.VERTICAL) ? y + i : y;

            ShipCell.validatePosition(currentX, currentY);

            if (shipCellRepository.existsByXAndYAndShip_PlayerId(currentX, currentY, playerId)) {
                throw new IllegalArgumentException("A ship already exists at (" + currentX + "," + currentY + ")");
            }

            shipCells.add(new ShipCell(currentX, currentY));
        }

        Ship ship = new Ship(x, y, length, direction, playerId);
        shipRepository.save(ship);

        for (ShipCell cell : shipCells) {
            cell.assignToShip(ship);
            shipCellRepository.save(cell);
        }

        return ShipDTO.from(ship);
    }

    public boolean checkHitOnOpponent(int x, int y, Long playerId) {
        Long opponentId = getOpponentId(playerId);

        Optional<ShipCell> cellOpt = shipCellRepository.findByXAndYAndShip_PlayerId(x, y, opponentId);
        if (cellOpt.isPresent()) {
            ShipCell cell = cellOpt.get();
            if (!cell.isHit()) {
                cell.markAsHit();
                shipCellRepository.save(cell);
            }

            Ship ship = cell.getShip();
            boolean isSunk = shipCellRepository.findByShipAndHitFalse(ship).isEmpty();
            if (isSunk && !ship.isSunk()) {
                ship.sink();
                shipRepository.save(ship);
            }

            return true;
        }

        return false;
    }

    public boolean isOpponentDefeated(Long playerId) {
        Long opponentId = getOpponentId(playerId);
        return shipRepository.findByPlayerIdAndSunkFalse(opponentId).isEmpty();
    }

    @CircuitBreaker(name = "getGameId", fallbackMethod = "fallbackForGameId")
    private Long getGameIdForPlayer(Long playerId) {
        return restTemplate.getForObject(
                "http://player/players/" + playerId + "/gameId",
                Long.class
        );
    }

    @CircuitBreaker(name = "getOpponentId", fallbackMethod = "fallbackForOpponent")
    private Long getOpponentId(Long playerId) {
        return restTemplate.getForObject(
                "http://player/players/" + playerId + "/opponent",
                Long.class
        );
    }

    public Long fallbackForGameId(Long playerId, Throwable t) {
        throw new IllegalStateException("Game-Service not available (fallback): " + t.getMessage());
    }

    public Long fallbackForOpponent(Long playerId, Throwable t) {
        throw new IllegalStateException("Opponent could not be determined (fallback): " + t.getMessage());
    }



}
