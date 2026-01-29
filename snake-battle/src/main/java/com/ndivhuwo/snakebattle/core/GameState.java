package com.ndivhuwo.snakebattle.core;

import com.ndivhuwo.snakebattle.model.Position;

import java.util.HashSet;
import java.util.Set;

public class GameState {

    private final int width;
    private final int height;

    private final Set<Position> foodPositions = new HashSet<>();

    public GameState(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Set<Position> foodPositions() {
        return foodPositions;
    }

    public boolean isInsideGrid(Position position) {
        return position.x() >= 0 &&
                position.y() >= 0 &&
                position.x() < width &&
                position.y() < height;
    }

    public boolean hasFoodAt(Position position) {
        return foodPositions.contains(position);
    }

    public void addFood(Position position) {
        if (isInsideGrid(position)) {
            foodPositions.add(position);
        }
    }

    public void removeFood(Position position) {
        foodPositions.remove(position);
    }
}
