package com.ndivhuwo.snakebattle.core;

import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;
import com.ndivhuwo.snakebattle.ai.SnakeAI;

import java.util.List;

/**
 * The main engine of the game.
 * Handles:
 * - Snake movement
 * - Food consumption
 * - Boundary collisions
 * - Console rendering (Phase One)
 */
public class GameEngine {

    private final GameState state;
    private final List<Snake> snakes;
    private final List<SnakeAI> ais;

    public GameEngine(GameState state, List<Snake> snakes, List<SnakeAI> ais) {
        this.state = state;
        this.snakes = snakes;
        this.ais = ais;
    }

    /**
     * Perform one game step:
     * - Each snake moves according to its AI
     * - Check for wall collisions
     * - Check for food consumption
     */
    public void step() {
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            if (!snake.isAlive()) continue;

            // Get next move from AI
            Direction move = ais.get(i).nextMove(state, snake);

            // Move snake
            snake.move(move, false);

            // Check wall collision
            if (!state.grid().isInside(snake.head())) {
                snake.kill();
            }

            // Check food consumption
            if (state.hasFoodAt(snake.head())) {
                snake.move(move, true); // grow
                state.removeFood(snake.head());
            }
        }
    }

    /**
     * Simple console renderer for Phase One.
     * Prints the grid with snakes (S) and food (F).
     */
    public void render() {
        for (int y = 0; y < state.grid().height(); y++) {
            for (int x = 0; x < state.grid().width(); x++) {
                Position p = new Position(x, y);
                boolean printed = false;

                // Print snakes
                for (Snake snake : snakes) {
                    if (snake.isAlive() && snake.body().contains(p)) {
                        System.out.print("S");
                        printed = true;
                        break;
                    }
                }

                // Print food or empty space
                if (!printed) {
                    if (state.hasFoodAt(p)) {
                        System.out.print("F");
                    } else {
                        System.out.print(".");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
