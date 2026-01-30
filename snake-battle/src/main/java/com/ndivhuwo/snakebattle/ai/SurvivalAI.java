package com.ndivhuwo.snakebattle.ai;

import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SurvivalAI: prefers safe moves and avoids collisions.
 */
public class SurvivalAI implements SnakeAI {

    private final Random random = new Random();

    @Override
    public Direction nextMove(GameState state, Snake snake) {
        List<Direction> safeMoves = new ArrayList<>();

        // Check all four directions for safety
        for (Direction d : Direction.values()) {
            Position next = snake.head().move(d);
            if (state.isSafe(next)) {
                safeMoves.add(d);
            }
        }

        // No safe moves? pick something anyway (will likely die)
        if (safeMoves.isEmpty()) {
            return Direction.UP;
        }

        // Randomly pick among safe moves to avoid predictability
        return safeMoves.get(random.nextInt(safeMoves.size()));
    }
}
