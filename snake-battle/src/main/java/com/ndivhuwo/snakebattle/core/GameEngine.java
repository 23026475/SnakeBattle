package com.ndivhuwo.snakebattle.core;

import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;
import com.ndivhuwo.snakebattle.ai.*;
import com.ndivhuwo.snakebattle.model.SnakeStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Game engine with dynamic food, snake-eating logic, and score tracking (Phase Two)
 */
public class GameEngine {

    private final GameState state;
    private final List<Snake> snakes;
    private final List<SnakeAI> ais;  // initialized internally
    private final List<SnakeStats> stats; // tracks food eaten and steps survived

    private final int foodSpawnRate = 1; // how many new food per step
    private final int maxFood = 5;       // max total food

    /**
     * Constructs GameEngine and automatically assigns AI to snakes.
     * The AI order matches the snake order.
     */
    public GameEngine(GameState state, List<Snake> snakes) {
        this.state = state;
        this.snakes = snakes;

        // Initialize AI list in the same order as snakes
        this.ais = List.of(
                new RandomAI(),
                new GreedyAI(),
                new BFSAI(),
                new AStarAI(),
                new HunterAI(),
                new SurvivalAI()
        );

        // initialize stats
        this.stats = new ArrayList<>();
        for (Snake s : snakes) {
            stats.add(new SnakeStats(s));
        }
    }
    public GameState getGameState() {
        return state;
    }

    public void step() {
        // 1️⃣ Move snakes and check collisions
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            SnakeStats snakeStat = stats.get(i);

            if (!snake.isAlive()) {
                snakeStat.die();
                continue;
            }

            snakeStat.stepSurvived();

            Direction move = ais.get(i).nextMove(state, snake);
            snake.move(move, false);

            // Wall collision
            if (!state.grid().isInside(snake.head())) {
                snake.kill();
                snakeStat.die();
            }

            // Food collision
            if (state.hasFoodAt(snake.head())) {
                snake.move(move, true); // grow
                state.removeFood(snake.head());
                snakeStat.addFood(); // increment food eaten
            }

            // Check collisions with other snakes' bodies
            for (Snake other : snakes) {
                if (other == snake || !other.isAlive()) continue;

                if (other.body().contains(snake.head())) {
                    snake.kill();
                    snakeStat.die();
                    System.out.println("Snake died by running into another snake at " + snake.head());
                    break;
                }
            }
        }

        // 2️⃣ Head-on collisions
        for (int i = 0; i < snakes.size(); i++) {
            Snake s1 = snakes.get(i);
            SnakeStats stat1 = stats.get(i);
            if (!s1.isAlive()) continue;

            for (int j = i + 1; j < snakes.size(); j++) {
                Snake s2 = snakes.get(j);
                SnakeStats stat2 = stats.get(j);
                if (!s2.isAlive()) continue;

                if (s1.head().equals(s2.head())) {
                    s1.kill();
                    s2.kill();
                    stat1.die();
                    stat2.die();
                    System.out.println("Head-on collision at " + s1.head() + " — both snakes died!");
                }
            }
        }

        // 3️⃣ Spawn new food dynamically
        while (state.foodPositions().size() < maxFood) {
            for (int f = 0; f < foodSpawnRate; f++) {
                int x = (int) (Math.random() * state.grid().width());
                int y = (int) (Math.random() * state.grid().height());
                Position p = new Position(x, y);

                boolean occupied = snakes.stream()
                        .filter(Snake::isAlive)
                        .anyMatch(s -> s.body().contains(p));

                if (!occupied) {
                    state.addFood(p);
                }
            }
        }
    }

    public void render() {
        for (int y = 0; y < state.grid().height(); y++) {
            for (int x = 0; x < state.grid().width(); x++) {
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

    /** Prints the current stats of all snakes */
    public void printStats() {
        System.out.println("=== Snake Stats ===");
        for (SnakeStats s : stats) {
            System.out.println(s);
        }
        System.out.println("===================");
    }
}
