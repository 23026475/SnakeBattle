package com.ndivhuwo.snakebattle.ai;

import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;
import com.ndivhuwo.snakebattle.core.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI implements SnakeAI {

    private final Random random = new Random();

    @Override
    public Direction nextMove(GameState gameState, Snake snake) {
        List<Direction> possible = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Position nextPos = snake.head().move(dir);
            if (gameState.isInsideGrid(nextPos)) {
                possible.add(dir);
            }
        }

        if (possible.isEmpty()) {
            return Direction.UP; // default fallback
        }

        return possible.get(random.nextInt(possible.size()));
    }
}
