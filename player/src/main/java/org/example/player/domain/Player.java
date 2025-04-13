package org.example.player.domain;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long gameId;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guess> guesses = new ArrayList<>();

    public Player() {}

    public Player(String name, Long gameId) {
        this.name = name;
        this.gameId = gameId;
    }

    public void addGuess(Guess guess) {
        guesses.add(guess);
    }
}
