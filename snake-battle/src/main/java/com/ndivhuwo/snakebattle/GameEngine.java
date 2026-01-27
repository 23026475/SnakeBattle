package com.ndivhuwo.snakebattle;

import java.util.List;

public class GameEngine {

    private final GameState state;
    private final List<Snake> snakes;
    private final List<SnakeAI> ais;

    public GameEngine(GameState state, List<Snake> snakes, List<SnakeAI> ais) {
        this.state = state;
        this.snakes = snakes;
        this.ais = ais;
    }

    public void step() {
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            SnakeAI ai = ais.get(i);

            if (!snake.isAlive()) continue;

            Direction move = ai.nextMove(state, snake);
            snake.move(move, false);

            // Check collisions with walls
            if (!state.isInsideGrid(snake.head())) {
                snake.kill();
            }

            // Check collisions with food
            if (state.hasFoodAt(snake.head())) {
                snake.move(Direction.UP, true); // grow one extra step
                state.removeFood(snake.head());
            }
        }
    }

    public void render() {
        for (int y = 0; y < state.height(); y++) {
            for (int x = 0; x < state.width(); x++) {
                Position p = new Position(x, y);
                boolean printed = false;

                // Check snakes
                for (Snake snake : snakes) {
                    if (snake.body().contains(p) && snake.isAlive()) {
                        System.out.print("S");
                        printed = true;
                        break;
                    }
                }

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
