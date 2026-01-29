package com.ndivhuwo.snakebattle.core;

import com.ndivhuwo.snakebattle.ai.SnakeAI;
import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.List;

public class GameEngine {

    private final GameState state;
    private final List<Snake> snakes;
    private final List<SnakeAI> ais;
    private final int foodSpawnRate;
    private final int maxFood;

    public GameEngine(GameState state, List<Snake> snakes, List<SnakeAI> ais,
                      int foodSpawnRate, int maxFood) {
        this.state = state;
        this.snakes = snakes;
        this.ais = ais;
        this.foodSpawnRate = foodSpawnRate;
        this.maxFood = maxFood;
    }

    public void step() {

        // 1️⃣ Move snakes + stats
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            SnakeAI ai = ais.get(i);

            if (!snake.isAlive()) continue;

            // Track survival
            snake.incrementTurnsSurvived();

            Direction move = ai.nextMove(state, snake);
            snake.move(move, false);

            // Wall collision
            if (!state.isInsideGrid(snake.head())) {
                snake.kill();
                continue;
            }

            // Food collision
            if (state.hasFoodAt(snake.head())) {
                snake.move(Direction.UP, true);
                state.removeFood(snake.head());
                snake.incrementFoodEaten();
            }

            // Snake body collision
            for (Snake other : snakes) {
                if (other == snake || !other.isAlive()) continue;

                if (other.body().contains(snake.head())) {
                    snake.kill();
                    other.incrementSnakesEaten();
                    System.out.println("A snake was eaten at " + snake.head());
                    break;
                }
            }
        }

        // 2️⃣ Head-on collisions
        for (int i = 0; i < snakes.size(); i++) {
            Snake s1 = snakes.get(i);
            if (!s1.isAlive()) continue;

            for (int j = i + 1; j < snakes.size(); j++) {
                Snake s2 = snakes.get(j);
                if (!s2.isAlive()) continue;

                if (s1.head().equals(s2.head())) {
                    s1.kill();
                    s2.kill();
                    s1.incrementSnakesEaten();
                    s2.incrementSnakesEaten();
                    System.out.println("Head-on collision at " + s1.head());
                }
            }
        }

        // 3️⃣ Dynamic food spawning
        while (state.foodPositions().size() < maxFood) {
            for (int f = 0; f < foodSpawnRate; f++) {
                int x = (int) (Math.random() * state.width());
                int y = (int) (Math.random() * state.height());
                Position p = new Position(x, y);

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

                for (Snake snake : snakes) {
                    if (snake.isAlive() && snake.body().contains(p)) {
                        System.out.print("S");
                        printed = true;
                        break;
                    }
                }

                if (!printed) {
                    System.out.print(state.hasFoodAt(p) ? "F" : ".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printStats() {
        System.out.println("=== Snake Stats ===");
        for (int i = 0; i < snakes.size(); i++) {
            Snake s = snakes.get(i);
            System.out.printf(
                    "Snake %d | Alive: %s | Turns: %d | Food: %d | Snakes Eaten: %d%n",
                    i + 1,
                    s.isAlive(),
                    s.getTurnsSurvived(),
                    s.getFoodEaten(),
                    s.getSnakesEaten()
            );
        }
        System.out.println("===================\n");
    }
}
