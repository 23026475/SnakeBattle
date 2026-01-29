package com.ndivhuwo.snakebattle.ai;

import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;
import com.ndivhuwo.snakebattle.core.GameState;

import java.util.Comparator;

public class GreedyAI implements SnakeAI {

    @Override
    public Direction nextMove(GameState gameState, Snake snake) {
        if (gameState.foodPositions().isEmpty()) {
            // fallback to random if no food
            return Direction.values()[(int) (Math.random() * Direction.values().length)];
        }

        Position head = snake.head();

        // Find closest food
        Position closest = gameState.foodPositions().stream()
                .min(Comparator.comparingInt(f -> distance(head, f)))
                .orElseThrow();

        // Decide direction to move closer
        if (closest.x() > head.x()) return Direction.RIGHT;
        if (closest.x() < head.x()) return Direction.LEFT;
        if (closest.y() > head.y()) return Direction.DOWN;
        if (closest.y() < head.y()) return Direction.UP;

        // Already on food (fallback)
        return Direction.UP;
    }

    private int distance(Position a, Position b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y()); // Manhattan distance
    }
}
