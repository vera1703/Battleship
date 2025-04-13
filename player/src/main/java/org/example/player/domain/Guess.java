package org.example.player.domain;

import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
public class Guess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;
    private boolean hit;

    @ManyToOne
    private Player player;

    public Guess() {}

    private Guess(int x, int y, boolean hit, Player player) {
        this.x = x;
        this.y = y;
        this.hit = hit;
        this.player = player;
    }

    public static Guess of(Player player, int x, int y, boolean hit) {
        Guess guess = new Guess(x, y, hit, player);
        player.addGuess(guess);
        return guess;
    }
}
