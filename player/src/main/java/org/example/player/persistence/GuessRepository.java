package org.example.player.persistence;

import org.example.player.domain.Guess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuessRepository extends JpaRepository<Guess, Long> {
}
