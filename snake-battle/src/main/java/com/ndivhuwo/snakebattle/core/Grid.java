package com.ndivhuwo.snakebattle.core;

import com.ndivhuwo.snakebattle.model.Position;

/**
 * Represents the game grid (width x height) and provides boundary checks.
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
     * Returns true if the position is inside the grid boundaries.
     */
    public boolean isInside(Position p) {
        return p.x() >= 0 && p.x() < width &&
                p.y() >= 0 && p.y() < height;
    }
}
