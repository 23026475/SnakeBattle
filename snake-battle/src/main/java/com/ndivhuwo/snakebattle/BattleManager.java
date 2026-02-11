package com.ndivhuwo.snakebattle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BattleManager {
    private GamePanel gamePanel;
    private javax.swing.Timer battleTimer;  // Use fully qualified name
    private boolean battleRunning;

    // Battle snakes
    private List<BattleSnake> snakes;
    private Map<Integer, BattleSnake> snakesById;

    // Food
    private Point food;

    // Battle grid
    private int gridWidth;
    private int gridHeight;

    // Statistics
    private int round;
    private BattleSnake winner;
    private Random random;

    public BattleManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.gridWidth = gamePanel.getGridWidth();
        this.gridHeight = gamePanel.getGridHeight();
        this.snakes = new ArrayList<>();
        this.snakesById = new HashMap<>();
        this.random = new Random();
    }

    public void startBattle() {
        System.out.println("Starting Snake Battle!");

        stopBattle();

        // Reset battle state
        snakes.clear();
        snakesById.clear();
        battleRunning = true;
        round = 0;
        winner = null;

        // Create battling snakes with different algorithms
        createBattleSnakes();

        // Place initial food
        placeFood();

        // Start battle timer - Using Swing Timer with fully qualified name
        battleTimer = new javax.swing.Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performBattleStep();
            }
        });
        battleTimer.start();
    }

//    private void stopBattle() {
//        battleRunning = false;
//        if (battleTimer != null && battleTimer.isRunning()) {
//            battleTimer.stop();
//        }
//    }

    private void createBattleSnakes() {
        // Create 3 snakes with different algorithms and colors
        // Position them in different corners of the grid

        // DFS Snake (Blue) - Top-left corner
        BattleSnake dfsSnake = new BattleSnake(1,
                new Color(30, 30, 180),
                "DFS",
                new Point(5, 5));

        // BFS Snake (Yellow) - Top-right corner
        BattleSnake bfsSnake = new BattleSnake(2,
                new Color(220, 220, 50),
                "BFS",
                new Point(gridWidth - 6, 5));

        // A* Snake (Purple) - Bottom-left corner
        BattleSnake aStarSnake = new BattleSnake(3,
                new Color(180, 50, 220),
                "ASTAR",
                new Point(5, gridHeight - 6));

        snakes.add(dfsSnake);
        snakes.add(bfsSnake);
        snakes.add(aStarSnake);

        for (BattleSnake snake : snakes) {
            snakesById.put(snake.getId(), snake);
        }

        System.out.println("Created " + snakes.size() + " battle snakes");
    }

    private void placeFood() {
        // Find a position not occupied by any snake
        int attempts = 0;
        int maxAttempts = gridWidth * gridHeight * 2;

        do {
            food = new Point(
                    random.nextInt(gridWidth),
                    random.nextInt(gridHeight)
            );
            attempts++;

            if (attempts >= maxAttempts) {
                // Emergency placement - find any free cell
                for (int x = 0; x < gridWidth; x++) {
                    for (int y = 0; y < gridHeight; y++) {
                        Point cell = new Point(x, y);
                        if (isCellFree(cell)) {
                            food = cell;
                            return;
                        }
                    }
                }
                // No free cells - battle is over
                endBattle();
                return;
            }
        } while (!isCellFree(food));

        System.out.println("Placed food at: (" + food.x + ", " + food.y + ")");
    }

    private boolean isCellFree(Point cell) {
        // Check if cell is within bounds
        if (cell.x < 0 || cell.x >= gridWidth || cell.y < 0 || cell.y >= gridHeight) {
            return false;
        }

        // Check if cell is occupied by any snake
        for (BattleSnake snake : snakes) {
            if (snake.contains(cell)) {
                return false;
            }
        }

        return true;
    }

    private void performBattleStep() {
        if (!battleRunning) {
            battleTimer.stop();
            return;
        }

        round++;
        System.out.println("Battle Round: " + round);

        // Move each alive snake
        for (BattleSnake snake : snakes) {
            if (snake.isAlive()) {
                moveSnake(snake);
            }
        }

        // Check for food consumption
        checkFoodConsumption();

        // Check for collisions
        checkCollisions();

        // Check battle end conditions
        checkBattleEnd();

        // Repaint game panel
        gamePanel.repaint();
    }

    private void moveSnake(BattleSnake snake) {
        Point currentHead = snake.getHead();
        if (currentHead == null) {
            snake.die();
            return;
        }

        // Simple AI: move towards food
        Point foodDirection = getDirectionToFood(currentHead);
        Point nextPosition = calculateNextPosition(currentHead, foodDirection, snake);

        // If the calculated position is invalid, try alternative moves
        if (!isMoveValid(nextPosition, snake.getId())) {
            nextPosition = findAlternativeMove(currentHead, snake);
        }

        // Move snake
        snake.move(nextPosition);
    }

    private Point getDirectionToFood(Point from) {
        int dx = food.x - from.x;
        int dy = food.y - from.y;

        // Prioritize the larger distance
        if (Math.abs(dx) > Math.abs(dy)) {
            return new Point(dx > 0 ? 1 : -1, 0);
        } else {
            return new Point(0, dy > 0 ? 1 : -1);
        }
    }

    private Point calculateNextPosition(Point current, Point direction, BattleSnake snake) {
        return new Point(current.x + direction.x, current.y + direction.y);
    }

    private boolean isMoveValid(Point position, int snakeId) {
        // Check bounds
        if (position.x < 0 || position.x >= gridWidth ||
                position.y < 0 || position.y >= gridHeight) {
            return false;
        }

        // Check collision with other snakes
        for (BattleSnake snake : snakes) {
            if (snake.getId() != snakeId && snake.contains(position)) {
                return false;
            }
        }

        return true;
    }

    private Point findAlternativeMove(Point currentHead, BattleSnake snake) {
        // Try all four directions
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            Point nextPos = new Point(currentHead.x + dir[0], currentHead.y + dir[1]);
            if (isMoveValid(nextPos, snake.getId())) {
                return nextPos;
            }
        }

        // No valid moves - snake is trapped
        snake.die();
        return currentHead; // Stay in place (will die next check)
    }

    private void checkFoodConsumption() {
        for (BattleSnake snake : snakes) {
            if (snake.isAlive() && snake.getHead().equals(food)) {
                System.out.println(snake.getAlgorithm() + " snake ate the food!");
                snake.grow();
                placeFood();
                break; // Only one snake can eat the food
            }
        }
    }

    private void checkCollisions() {
        // Check for snake-to-snake collisions
        for (BattleSnake snake : snakes) {
            if (!snake.isAlive()) continue;

            Point head = snake.getHead();

            // Check collision with other snakes' bodies
            for (BattleSnake otherSnake : snakes) {
                if (otherSnake.getId() == snake.getId()) continue;

                List<Point> otherBody = otherSnake.getBody();
                // Skip the head of the other snake for head-on collision check
                for (int i = 0; i < otherBody.size(); i++) {
                    if (head.equals(otherBody.get(i))) {
                        // Head-on collision if both heads hit
                        if (i == 0 && otherSnake.isAlive()) {
                            // Both snakes die in head-on collision
                            snake.die();
                            otherSnake.die();
                            System.out.println("Head-on collision between " +
                                    snake.getAlgorithm() + " and " + otherSnake.getAlgorithm());
                        } else {
                            // Hit body of other snake
                            snake.die();
                            System.out.println(snake.getAlgorithm() +
                                    " hit body of " + otherSnake.getAlgorithm());
                        }
                        break;
                    }
                }
            }

            // Check wall collision
            if (head.x < 0 || head.x >= gridWidth ||
                    head.y < 0 || head.y >= gridHeight) {
                snake.die();
                System.out.println(snake.getAlgorithm() + " hit the wall");
            }
        }
    }

    private void checkBattleEnd() {
        int aliveCount = 0;
        BattleSnake lastAlive = null;

        for (BattleSnake snake : snakes) {
            if (snake.isAlive()) {
                aliveCount++;
                lastAlive = snake;
            }
        }

        if (aliveCount <= 1) {
            if (aliveCount == 1) {
                winner = lastAlive;
                System.out.println("Battle winner: " + winner.getAlgorithm() +
                        " with score: " + winner.getScore());
            } else {
                System.out.println("All snakes died! No winner.");
            }
            endBattle();
        }

        // Also end after too many rounds
        if (round >= 500) {
            System.out.println("Battle timed out after 500 rounds");
            // Determine winner by score
            determineWinnerByScore();
            endBattle();
        }
    }

    private void determineWinnerByScore() {
        BattleSnake highestScorer = null;
        int maxScore = -1;

        for (BattleSnake snake : snakes) {
            if (snake.getScore() > maxScore) {
                maxScore = snake.getScore();
                highestScorer = snake;
            }
        }

        winner = highestScorer;
        System.out.println("Time's up! Winner by score: " +
                (winner != null ? winner.getAlgorithm() : "None") +
                " with score: " + maxScore);
    }

    private void endBattle() {
        battleRunning = false;
        if (battleTimer != null) {
            battleTimer.stop();
        }
        gamePanel.repaint();
    }

    public void stopBattle() {
        battleRunning = false;
        if (battleTimer != null) {
            battleTimer.stop();
        }
    }

    public boolean isBattleRunning() {
        return battleRunning;
    }

    public List<BattleSnake> getSnakes() {
        return new ArrayList<>(snakes);
    }

    public Point getFood() {
        return food;
    }

    public BattleSnake getWinner() {
        return winner;
    }

    public int getRound() {
        return round;
    }

    public int getAliveCount() {
        int count = 0;
        for (BattleSnake snake : snakes) {
            if (snake.isAlive()) count++;
        }
        return count;
    }
}