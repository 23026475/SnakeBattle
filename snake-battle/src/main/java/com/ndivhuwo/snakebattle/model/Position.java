package com.ndivhuwo.snakebattle.model;

import java.util.Objects;

/**
 * Immutable coordinate on the grid.
 * Used by Snake, Grid, GameState, and AI to represent locations.
 */
public record Position(int x, int y) {

    /**
     * Returns a new Position moved by a given direction.
     */
    public Position move(Direction d) {
        return new Position(x + d.dx(), y + d.dy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position p)) return false;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
