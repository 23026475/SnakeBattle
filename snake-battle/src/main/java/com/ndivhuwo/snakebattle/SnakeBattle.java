package com.ndivhuwo.snakebattle;

import com.ndivhuwo.snakebattle.core.*;
import com.ndivhuwo.snakebattle.model.*;
import com.ndivhuwo.snakebattle.ai.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Phase One console demo: runs a simple snake battle with RandomAI
 */
public class SnakeBattle {

    public static void main(String[] args) throws InterruptedException {

        // 1️⃣ Create a 10x10 grid
        Grid grid = new Grid(10, 10);

        // 2️⃣ Create two snakes at different positions
        Snake snake1 = new Snake(new Position(2, 2));
        Snake snake2 = new Snake(new Position(7, 7));
        List<Snake> snakes = List.of(snake1, snake2);

        // 3️⃣ Create the GameState
        GameState state = new GameState(grid, snakes);

        // 4️⃣ Add some initial food
        state.addFood(new Position(5, 5));
        state.addFood(new Position(3, 8));

        // 5️⃣ Create RandomAI for each snake
        List<SnakeAI> ais = new ArrayList<>();
        for (int i = 0; i < snakes.size(); i++) {
            ais.add(new RandomAI());
        }

        // 6️⃣ Create the game engine
        GameEngine engine = new GameEngine(state, snakes, ais);

        // 7️⃣ Run game loop for 20 steps
        for (int step = 0; step < 20; step++) {
            System.out.println("Step " + step);
            engine.render();  // show current state
            engine.step();    // advance one step

            Thread.sleep(500); // wait half a second for console readability
        }

        System.out.println("Game Over!");
    }
}
