package com.ndivhuwo.snakebattle;

public interface SnakeAI {

    /**
     * Decide the next move direction for a snake
     */
    Direction nextMove(GameState gameState, Snake snake);
}
