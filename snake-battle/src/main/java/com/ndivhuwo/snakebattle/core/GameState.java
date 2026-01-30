package com.ndivhuwo.snakebattle.core;

import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds the current state of the game world.
 * Provides read-only access for AI logic.
 */
public class GameState {

    private final Grid grid;
    private final Set<Position> foodPositions = new HashSet<>();
    private final List<Snake> snakes; // made private for proper encapsulation

    public GameState(Grid grid, List<Snake> snakes) {
        this.grid = grid;
        this.snakes = snakes;
    }

    /* ---------- Grid & Bounds ---------- */

    public Grid grid() {
        return grid;
    }

    public boolean isInsideGrid(Position p) {
        return grid.isInside(p);
    }

    /* ---------- Food ---------- */

    public boolean hasFoodAt(Position p) {
        return foodPositions.contains(p);
    }

    public Set<Position> foodPositions() {
        return foodPositions;
    }

    public void addFood(Position p) {
        foodPositions.add(p);
    }

    public void removeFood(Position p) {
        foodPositions.remove(p);
    }

    /* ---------- Snakes & Occupancy ---------- */

    /**
     * Returns a read-only list of all snakes.
     */
    public List<Snake> snakes() {
        return snakes;
    }

    /**
     * Returns true if any alive snake occupies the position.
     */
    public boolean isOccupied(Position p) {
        for (Snake s : snakes) {
            if (s.isAlive() && s.body().contains(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the position is inside the grid and not occupied.
     */
    public boolean isSafe(Position p) {
        return grid.isInside(p) && !isOccupied(p);
    }
}
