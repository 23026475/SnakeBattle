package com.ndivhuwo.snakebattle;

import com.ndivhuwo.snakebattle.ai.GreedyAI;
import com.ndivhuwo.snakebattle.ai.RandomAI;
import com.ndivhuwo.snakebattle.ai.SnakeAI;
import com.ndivhuwo.snakebattle.core.GameEngine;
import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.List;

public class SnakeBattle {

    public static void main(String[] args) throws InterruptedException {
        GameState state = new GameState(10, 10);

        Snake snake1 = new Snake(new Position(0, 0));
        Snake snake2 = new Snake(new Position(9, 0));
        Snake snake3 = new Snake(new Position(0, 9));

        SnakeAI ai1 = new RandomAI();
        SnakeAI ai2 = new GreedyAI();
        SnakeAI ai3 = new RandomAI();

        int foodSpawnRate = 1;
        int maxFood = 5;

        GameEngine engine = new GameEngine(
                state,
                List.of(snake1, snake2, snake3),
                List.of(ai1, ai2, ai3),
                foodSpawnRate,
                maxFood
        );

        // Seed initial food
        state.addFood(new Position(5, 5));
        state.addFood(new Position(3, 7));

        for (int i = 0; i < 50; i++) {
            engine.render();
            engine.step();
            Thread.sleep(500);
        }
    }

}
