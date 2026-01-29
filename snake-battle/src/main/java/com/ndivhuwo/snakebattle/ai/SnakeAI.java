package com.ndivhuwo.snakebattle.ai;

import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Snake;

public interface SnakeAI {

    /**
     * Decide the next move direction for a snake
     */
    Direction nextMove(GameState gameState, Snake snake);
}
