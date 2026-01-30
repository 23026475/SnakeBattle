package com.ndivhuwo.snakebattle.ai;

import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.Random;

/**
 * RandomAI: chooses a valid random direction each step.
 * Minimal AI for Phase One testing.
 */
public class RandomAI implements SnakeAI {

    private static final Direction[] DIRECTIONS = Direction.values();
    private final Random random = new Random();

    @Override
    public Direction nextMove(GameState state, Snake snake) {
        // Pick random direction until a safe one is found
        for (int i = 0; i < 10; i++) { // try up to 10 times
            Direction dir = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            if (isSafe(state, snake, dir)) {
                return dir;
            }
        }
        // If no safe direction found, just return any
        return DIRECTIONS[random.nextInt(DIRECTIONS.length)];
    }

    /**
     * Returns true if moving in this direction does not immediately hit the wall or snake body
     */
    private boolean isSafe(GameState state, Snake snake, Direction dir) {
        return state.grid().isInside(snake.head().move(dir)) &&
                !state.isOccupied(snake.head().move(dir));
    }
}
