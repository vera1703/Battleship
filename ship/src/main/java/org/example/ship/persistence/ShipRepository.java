package org.example.ship.persistence;

import org.example.ship.domain.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {
    List<Ship> findByPlayerIdAndSunkFalse(Long playerId);
}
