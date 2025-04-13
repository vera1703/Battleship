package org.example.ship.domain;

import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
public class ShipCell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;
    private boolean hit = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id")
    private Ship ship;

    public ShipCell() {}

    public ShipCell(int x, int y) {
        validatePosition(x, y);
        this.x = x;
        this.y = y;
    }

    public void markAsHit() {
        this.hit = true;
    }

    public void assignToShip(Ship ship) {
        this.ship = ship;
    }

    public static void validatePosition(int x, int y) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10) {
            throw new IllegalArgumentException("Invalid ship cell position: (" + x + ", " + y + ")");
        }
    }
}
