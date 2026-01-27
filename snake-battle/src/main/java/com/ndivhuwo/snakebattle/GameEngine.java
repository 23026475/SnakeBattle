package com.ndivhuwo.snakebattle;

import java.util.List;

public class GameEngine {

    private final GameState state;
    private final List<Snake> snakes;
    private final List<SnakeAI> ais;
    private final int foodSpawnRate; // how many new food pieces per step
    private final int maxFood;       // optional: limit total food


    public GameEngine(GameState state, List<Snake> snakes, List<SnakeAI> ais, int foodSpawnRate, int maxFood) {
        this.state = state;
        this.snakes = snakes;
        this.ais = ais;
        this.foodSpawnRate = foodSpawnRate;
        this.maxFood = maxFood;
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

            // Check collisions with other snakes (snake eating)
            for (Snake other : snakes) {
                if (other == snake || !other.isAlive()) continue;

                if (other.body().contains(snake.head())) {
                    snake.kill();  // the moving snake dies
                    System.out.println("A snake was eaten at " + snake.head());
                    break;
                }
            }
        }

        // --- Spawn new food dynamically ---
        int foodSpawnRate = 1; // number of new food per step
        int maxFood = 5;       // maximum total food on the grid

        while (state.foodPositions().size() < maxFood) {
            for (int f = 0; f < foodSpawnRate; f++) {
                int x = (int) (Math.random() * state.width());
                int y = (int) (Math.random() * state.height());
                Position p = new Position(x, y);

                // Only add food if no snake occupies the position
                boolean occupied = false;
                for (Snake s : snakes) {
                    if (s.isAlive() && s.body().contains(p)) {
                        occupied = true;
                        break;
                    }
                }

                if (!occupied) {
                    state.addFood(p);
                }
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
