package org.example.ship.persistence;

import org.example.ship.domain.Ship;
import org.example.ship.domain.ShipCell;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ShipCellRepository extends JpaRepository<ShipCell, Long> {

    boolean existsByXAndYAndShip_PlayerId(int x, int y, Long playerId);

    Optional<ShipCell> findByXAndYAndShip_PlayerId(int x, int y, Long playerId);

    List<ShipCell> findByShipAndHitFalse(Ship ship);
}
