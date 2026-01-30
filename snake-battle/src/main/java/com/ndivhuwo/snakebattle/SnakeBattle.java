package com.ndivhuwo.snakebattle;

import com.ndivhuwo.snakebattle.core.GameEngine;
import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.core.Grid;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.List;

/**
 * Main class to test Phase One: all AI snakes compete on the grid.
 */
public class SnakeBattle {

    public static void main(String[] args) {

        // 1️⃣ Create a grid
        Grid grid = new Grid(10, 10);

        // 2️⃣ Spawn snakes at different positions
        List<Snake> snakes = List.of(
                new Snake(new Position(1, 1)),
                new Snake(new Position(8, 1)),
                new Snake(new Position(1, 8)),
                new Snake(new Position(8, 8)),
                new Snake(new Position(4, 4)),
                new Snake(new Position(5, 5))
        );

        // 3️⃣ Create the game state
        GameState state = new GameState(grid, snakes);

        // 4️⃣ Create the engine (AI list initialized internally)
        GameEngine engine = new GameEngine(state, snakes);

        // 5️⃣ Run the game for a fixed number of steps
        int totalSteps = 50;

        for (int step = 1; step <= totalSteps; step++) {
            System.out.println("Step " + step);
            engine.step();
            engine.render();
            engine.printStats();

            // Optional: pause for readability
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }
        engine.printStats();
        System.out.println("Simulation finished!");
    }
}
