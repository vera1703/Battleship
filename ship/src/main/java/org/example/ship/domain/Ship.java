package org.example.ship.domain;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;
    private int length;
    private boolean sunk = false;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    private Long playerId;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipCell> cells = new ArrayList<>();

    public Ship() {}

    public Ship(int x, int y, int length, Direction direction, Long playerId) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.direction = direction;
        this.playerId = playerId;
    }

    public void sink() {
        this.sunk = true;
    }
}
