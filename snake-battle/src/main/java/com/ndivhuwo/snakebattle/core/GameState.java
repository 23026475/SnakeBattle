package com.ndivhuwo.snakebattle.core;

import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the current state of the game:
 * - The grid
 * - Positions of food
 * - All snakes
 */
public class GameState {

    private final Grid grid;
    private final Set<Position> food = new HashSet<>();
    private final List<Snake> snakes;

    public GameState(Grid grid, List<Snake> snakes) {
        this.grid = grid;
        this.snakes = snakes;
    }

    public Grid grid() {
        return grid;
    }

    /**
     * Checks if a position contains food.
     */
    public boolean hasFoodAt(Position p) {
        return food.contains(p);
    }

    /**
     * Adds a food item at the given position.
     */
    public void addFood(Position p) {
        food.add(p);
    }

    /**
     * Removes a food item from the given position.
     */
    public void removeFood(Position p) {
        food.remove(p);
    }

    /**
     * Returns all current food positions.
     */
    public Set<Position> foodPositions() {
        return food;
    }

    /**
     * Returns true if any snake occupies this position.
     */
    public boolean isOccupied(Position p) {
        return snakes.stream()
                .filter(Snake::isAlive)
                .anyMatch(s -> s.body().contains(p));
    }

    /**
     * Returns the list of snakes in the game.
     */
    public List<Snake> snakes() {
        return snakes;
    }
}
