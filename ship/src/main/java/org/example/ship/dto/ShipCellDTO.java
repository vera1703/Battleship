package org.example.ship.dto;

import org.example.ship.domain.ShipCell;


public class ShipCellDTO {

    private int x;
    private int y;
    private boolean hit;

    public ShipCellDTO() {}

    public ShipCellDTO(int x, int y, boolean hit) {
        this.x = x;
        this.y = y;
        this.hit = hit;
    }

    public static ShipCellDTO from(ShipCell cell) {
        return new ShipCellDTO(cell.getX(), cell.getY(), cell.isHit());
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isHit() { return hit; }
}
