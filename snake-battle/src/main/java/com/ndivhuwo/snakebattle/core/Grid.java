package com.ndivhuwo.snakebattle.core;

import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board boundaries and basic grid logic.
 */
public class Grid {

    private final int width;
    private final int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    /**
     * Checks if a position is inside the grid boundaries.
     */
    public boolean isInside(Position p) {
        return p.x() >= 0 && p.x() < width
                && p.y() >= 0 && p.y() < height;
    }

    /**
     * Returns all valid neighboring positions.
     */
    public List<Position> neighbors(Position p) {
        List<Position> result = new ArrayList<>();

        for (Direction d : Direction.values()) {
            Position next = p.move(d);
            if (isInside(next)) {
                result.add(next);
            }
        }
        return result;
    }
}

