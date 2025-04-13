package org.example.player.persistence;

import org.example.player.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    long countByGameId(Long gameId);
}
