package com.ndivhuwo.snakebattle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.Queue;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    // Game constants
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GRID_WIDTH = SCREEN_WIDTH / UNIT_SIZE;
    private static final int GRID_HEIGHT = SCREEN_HEIGHT / UNIT_SIZE;
    private static final int DELAY = 100;
    private static final int DFS_DELAY = 150;

    // Game state
    private String gameMode = "HUMAN";
    private SnakeGame parentFrame;

    // Snake properties
    private final int[] x = new int[GRID_WIDTH * GRID_HEIGHT];
    private final int[] y = new int[GRID_WIDTH * GRID_HEIGHT];
    private int bodyParts = 3; // Start with smaller snake
    private int applesEaten = 0;

    // Apple position
    private int appleX;
    private int appleY;

    // Direction
    private char direction = 'R';
    private boolean running = false;
    private boolean gameStarted = false;

    // Timer
    private Timer timer;
    private Random random;

    // Algorithm visualizers
    private DFSVisualizer dfsVisualizer;
    private BFSVisualizer bfsVisualizer;
    private AStarVisualizer aStarVisualizer;

    // Battle mode
    private BattleManager battleManager;

    public GamePanel(SnakeGame parent) {
        this.parentFrame = parent;
        random = new Random();

        // Set panel properties
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        // Initialize snake position
        initSnake();
        newApple();

        // Add key listener
        this.addKeyListener(new MyKeyAdapter());

        // Initialize algorithm visualizers
        dfsVisualizer = new DFSVisualizer(this);
        bfsVisualizer = new BFSVisualizer(this);
        aStarVisualizer = new AStarVisualizer(this);

        // Initialize battle manager
        battleManager = new BattleManager(this);
    }

    public boolean isGameRunning() {
        return running;
    }

    private void initSnake() {
        // Start snake in the middle
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (GRID_WIDTH / 2 - i) * UNIT_SIZE;
            y[i] = (GRID_HEIGHT / 2) * UNIT_SIZE;
        }
    }

    public void setGameMode(String mode) {
        this.gameMode = mode;
        System.out.println("Game mode set to: " + mode);
    }

    public void startGame() {
        if (gameStarted) {
            restartGame();
            return;
        }

        System.out.println("Starting game in mode: " + gameMode);

        // Reset game state
        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';
        running = true;
        gameStarted = true;

        // Stop any existing algorithm
        stopAllAlgorithms();

        // Stop any existing battle
        if (battleManager != null) {
            battleManager.stopBattle();
        }

        // Start appropriate mode
        switch (gameMode) {
            case "DFS":
                initSnake();
                newApple();
                timer = new Timer(DFS_DELAY, this);
                dfsVisualizer.startDFS();
                break;
            case "BFS":
                initSnake();
                newApple();
                timer = new Timer(DFS_DELAY, this);
                bfsVisualizer.startBFS();
                break;
            case "ASTAR":
                initSnake();
                newApple();
                timer = new Timer(DFS_DELAY, this);
                aStarVisualizer.startAStar();
                break;
            case "BATTLE":
                // Battle mode doesn't use the regular snake
                running = true;
                battleManager.startBattle();
                // No timer needed for battle mode
                timer = null;
                break;
            default: // HUMAN
                initSnake();
                newApple();
                timer = new Timer(DELAY, this);
                break;
        }

        if (timer != null) {
            timer.start();
        }
        requestFocusInWindow();
        System.out.println("Game started successfully");
    }

    private void stopAllAlgorithms() {
        if (dfsVisualizer != null) dfsVisualizer.stop();
        if (bfsVisualizer != null) bfsVisualizer.stop();
        if (aStarVisualizer != null) aStarVisualizer.stop();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (gameMode.equals("BATTLE")) {
            drawBattle(g);
        } else if (running) {
            // Draw algorithm visualization if not in human mode
            if (!gameMode.equals("HUMAN")) {
                drawAlgorithmVisualization(g);
            }

            // Draw apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw apple glow effect
            g.setColor(new Color(255, 100, 100, 100));
            g.fillOval(appleX - 5, appleY - 5, UNIT_SIZE + 10, UNIT_SIZE + 10);

            // Draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    // Head of snake
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                    // Draw eyes
                    g.setColor(Color.BLACK);
                    if (direction == 'R') {
                        g.fillRect(x[i] + UNIT_SIZE - 6, y[i] + 5, 4, 4);
                        g.fillRect(x[i] + UNIT_SIZE - 6, y[i] + UNIT_SIZE - 9, 4, 4);
                    } else if (direction == 'L') {
                        g.fillRect(x[i] + 2, y[i] + 5, 4, 4);
                        g.fillRect(x[i] + 2, y[i] + UNIT_SIZE - 9, 4, 4);
                    } else if (direction == 'U') {
                        g.fillRect(x[i] + 5, y[i] + 2, 4, 4);
                        g.fillRect(x[i] + UNIT_SIZE - 9, y[i] + 2, 4, 4);
                    } else if (direction == 'D') {
                        g.fillRect(x[i] + 5, y[i] + UNIT_SIZE - 6, 4, 4);
                        g.fillRect(x[i] + UNIT_SIZE - 9, y[i] + UNIT_SIZE - 6, 4, 4);
                    }
                } else {
                    // Body of snake with gradient color
                    Color bodyColor = new Color(
                            0,
                            150 + (i * 10) % 100,
                            0
                    );
                    g.setColor(bodyColor);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                    // Body pattern
                    g.setColor(new Color(0, 100, 0));
                    g.drawRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Draw score and mode
            drawHUD(g);

        } else {
            gameOver(g);
        }
    }

    private void drawBattle(Graphics g) {
        // Draw battle background
        g.setColor(new Color(20, 20, 30));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Draw grid lines (optional)
        g.setColor(new Color(50, 50, 70));
        for (int i = 0; i <= GRID_WIDTH; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        }
        for (int i = 0; i <= GRID_HEIGHT; i++) {
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        // Draw food
        Point food = battleManager.getFood();
        if (food != null) {
            g.setColor(Color.RED);
            g.fillOval(food.x * UNIT_SIZE, food.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

            // Draw food glow
            g.setColor(new Color(255, 100, 100, 100));
            g.fillOval(food.x * UNIT_SIZE - 5, food.y * UNIT_SIZE - 5, UNIT_SIZE + 10, UNIT_SIZE + 10);

            // Draw target symbol
            g.setColor(Color.YELLOW);
            g.drawLine(food.x * UNIT_SIZE, food.y * UNIT_SIZE,
                    food.x * UNIT_SIZE + UNIT_SIZE, food.y * UNIT_SIZE + UNIT_SIZE);
            g.drawLine(food.x * UNIT_SIZE + UNIT_SIZE, food.y * UNIT_SIZE,
                    food.x * UNIT_SIZE, food.y * UNIT_SIZE + UNIT_SIZE);
        }

        // Draw snakes
        for (BattleSnake snake : battleManager.getSnakes()) {
            if (snake.isAlive()) {
                drawBattleSnake(g, snake);
            } else {
                drawDeadSnake(g, snake);
            }
        }

        // Draw battle HUD
        drawBattleHUD(g);

        // If battle is over, show results
        if (!battleManager.isBattleRunning()) {
            drawBattleResults(g);
        }
    }

    private void drawBattleSnake(Graphics g, BattleSnake snake) {
        Color snakeColor = snake.getColor();
        List<Point> body = snake.getBody();

        // Draw snake body
        for (int i = 0; i < body.size(); i++) {
            Point segment = body.get(i);

            if (i == 0) {
                // Draw head with gradient
                g.setColor(snakeColor.brighter());
                g.fillRect(segment.x * UNIT_SIZE, segment.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

                // Draw head border
                g.setColor(snakeColor.darker());
                g.drawRect(segment.x * UNIT_SIZE, segment.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

                // Draw algorithm initial on head
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 12));
                String initial = snake.getAlgorithm().substring(0, 1);
                FontMetrics fm = g.getFontMetrics();
                int textX = segment.x * UNIT_SIZE + (UNIT_SIZE - fm.stringWidth(initial)) / 2;
                int textY = segment.y * UNIT_SIZE + (UNIT_SIZE + fm.getAscent()) / 2 - 2;
                g.drawString(initial, textX, textY);
            } else {
                // Draw body segments with gradient
                Color segmentColor = new Color(
                        Math.max(0, snakeColor.getRed() - i * 5),
                        Math.max(0, snakeColor.getGreen() - i * 5),
                        Math.max(0, snakeColor.getBlue() - i * 5)
                );
                g.setColor(segmentColor);
                g.fillRect(segment.x * UNIT_SIZE, segment.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

                // Draw segment border
                g.setColor(segmentColor.darker());
                g.drawRect(segment.x * UNIT_SIZE, segment.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    private void drawDeadSnake(Graphics g, BattleSnake snake) {
        Color deadColor = new Color(100, 100, 100, 150); // Gray, semi-transparent
        List<Point> body = snake.getBody();

        for (Point segment : body) {
            g.setColor(deadColor);
            g.fillRect(segment.x * UNIT_SIZE, segment.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

            // Draw X mark on dead snake
            g.setColor(Color.RED);
            g.drawLine(segment.x * UNIT_SIZE, segment.y * UNIT_SIZE,
                    segment.x * UNIT_SIZE + UNIT_SIZE, segment.y * UNIT_SIZE + UNIT_SIZE);
            g.drawLine(segment.x * UNIT_SIZE + UNIT_SIZE, segment.y * UNIT_SIZE,
                    segment.x * UNIT_SIZE, segment.y * UNIT_SIZE + UNIT_SIZE);
        }
    }

    private void drawBattleHUD(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // Draw title
        String title = "SNAKE BATTLE ARENA";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (SCREEN_WIDTH - fm.stringWidth(title)) / 2, 30);

        // Draw round counter
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String roundText = "Round: " + battleManager.getRound();
        g.drawString(roundText, 10, 30);

        // Draw alive count
        String aliveText = "Alive: " + battleManager.getAliveCount() + "/3";
        g.drawString(aliveText, SCREEN_WIDTH - fm.stringWidth(aliveText) - 10, 30);

        // Draw snake scores
        g.setFont(new Font("Arial", Font.BOLD, 16));
        int yPos = 60;
        for (BattleSnake snake : battleManager.getSnakes()) {
            String status = snake.isAlive() ? "ALIVE" : "DEAD";
            Color statusColor = snake.isAlive() ? Color.GREEN : Color.RED;

            String scoreText = snake.getAlgorithm() + ": " + snake.getScore() + " pts - " + status;

            g.setColor(snake.getColor());
            g.drawString(scoreText, 10, yPos);

            g.setColor(statusColor);
            g.drawString(status, SCREEN_WIDTH - fm.stringWidth(status) - 10, yPos);

            yPos += 25;
        }

        // Draw controls
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("SPACE: Restart Battle | ESC: Main Menu", 10, SCREEN_HEIGHT - 10);
    }

    private void drawBattleResults(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        BattleSnake winner = battleManager.getWinner();

        if (winner != null) {
            // Winner announcement
            g.setColor(winner.getColor());
            g.setFont(new Font("Arial", Font.BOLD, 60));
            String winnerText = winner.getAlgorithm() + " WINS!";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(winnerText, (SCREEN_WIDTH - fm.stringWidth(winnerText)) / 2, SCREEN_HEIGHT / 2 - 50);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String scoreText = "Score: " + winner.getScore();
            fm = g.getFontMetrics();
            g.drawString(scoreText, (SCREEN_WIDTH - fm.stringWidth(scoreText)) / 2, SCREEN_HEIGHT / 2 + 20);
        } else {
            // Draw match
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            String drawText = "DRAW!";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(drawText, (SCREEN_WIDTH - fm.stringWidth(drawText)) / 2, SCREEN_HEIGHT / 2 - 20);
        }

        // Final scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String finalText = "Final Scores:";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(finalText, (SCREEN_WIDTH - fm.stringWidth(finalText)) / 2, SCREEN_HEIGHT / 2 + 80);

        int yPos = SCREEN_HEIGHT / 2 + 130;
        for (BattleSnake snake : battleManager.getSnakes()) {
            g.setColor(snake.getColor());
            String snakeScore = snake.getAlgorithm() + ": " + snake.getScore() + " points";
            fm = g.getFontMetrics();
            g.drawString(snakeScore, (SCREEN_WIDTH - fm.stringWidth(snakeScore)) / 2, yPos);
            yPos += 40;
        }

        // Instructions
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String restartText = "Press SPACE to restart battle";
        fm = g.getFontMetrics();
        g.drawString(restartText, (SCREEN_WIDTH - fm.stringWidth(restartText)) / 2, yPos + 40);

        String menuText = "Press ESC for main menu";
        fm = g.getFontMetrics();
        g.drawString(menuText, (SCREEN_WIDTH - fm.stringWidth(menuText)) / 2, yPos + 80);
    }

    private void drawAlgorithmVisualization(Graphics g) {
        if (!gameMode.equals("HUMAN")) {
            Graphics2D g2d = (Graphics2D) g;
            Composite originalComposite = g2d.getComposite();

            // Set transparency for visualization
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

            switch (gameMode) {
                case "DFS":
                    drawDFSVisualization(g2d);
                    break;
                case "BFS":
                    drawBFSVisualization(g2d);
                    break;
                case "ASTAR":
                    drawAStarVisualization(g2d);
                    break;
            }

            g2d.setComposite(originalComposite);
        }
    }

    private void drawDFSVisualization(Graphics g) {
        if (dfsVisualizer != null) {
            // Draw visited cells
            g.setColor(new Color(30, 30, 180)); // Blue for DFS
            for (Point cell : dfsVisualizer.getVisitedCells()) {
                g.fillRect(cell.x * UNIT_SIZE, cell.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw current path
            g.setColor(new Color(0, 220, 0)); // Green for current path
            Stack<Point> path = dfsVisualizer.getCurrentPath();
            for (Point cell : path) {
                g.fillRect(cell.x * UNIT_SIZE, cell.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    private void drawBFSVisualization(Graphics g) {
        if (bfsVisualizer != null) {
            // Draw visited cells
            g.setColor(new Color(220, 220, 50)); // Yellow for BFS
            for (Point cell : bfsVisualizer.getVisitedCells()) {
                g.fillRect(cell.x * UNIT_SIZE, cell.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw current node being processed
            Point current = bfsVisualizer.getCurrentNode();
            if (current != null) {
                g.setColor(new Color(255, 100, 0)); // Orange for current node
                g.fillRect(current.x * UNIT_SIZE, current.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    private void drawAStarVisualization(Graphics g) {
        if (aStarVisualizer != null) {
            // Draw visited cells (closed set)
            g.setColor(new Color(180, 50, 220)); // Purple for A* visited
            for (Point cell : aStarVisualizer.getVisitedCells()) {
                g.fillRect(cell.x * UNIT_SIZE, cell.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw open set (nodes to be evaluated)
            g.setColor(new Color(255, 150, 50)); // Orange for open set
            // Use the getOpenSetPoints() method instead of accessing Node directly
            for (Point point : aStarVisualizer.getOpenSetPoints()) {
                g.fillRect(point.x * UNIT_SIZE, point.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    // Pathfinding helper methods
    public boolean pathExistsToApple() {
        return pathExists(getSnakeHead(), getApplePosition());
    }

    public boolean pathExists(Point start, Point goal) {
        if (start.equals(goal)) return true;

        boolean[][] visited = new boolean[GRID_WIDTH][GRID_HEIGHT];
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.x][start.y] = true;

        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(goal)) {
                return true;
            }

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (newX >= 0 && newX < GRID_WIDTH &&
                        newY >= 0 && newY < GRID_HEIGHT &&
                        !visited[newX][newY] && isCellSafe(new Point(newX, newY))) {

                    visited[newX][newY] = true;
                    queue.add(new Point(newX, newY));
                }
            }
        }

        return false;
    }

    public boolean canMoveAnywhere() {
        Point head = getSnakeHead();
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        for (int[] dir : directions) {
            int newX = head.x + dir[0];
            int newY = head.y + dir[1];

            if (newX >= 0 && newX < GRID_WIDTH &&
                    newY >= 0 && newY < GRID_HEIGHT &&
                    isCellSafe(new Point(newX, newY))) {
                return true;
            }
        }

        return false;
    }

    public boolean isSnakeTrapped() {
        return !canMoveAnywhere();
    }

    private void placeSmartApple() {
        int attempts = 0;
        int maxAttempts = GRID_WIDTH * GRID_HEIGHT * 2;

        do {
            // Try random placement
            boolean onSnake;
            do {
                onSnake = false;
                appleX = random.nextInt(GRID_WIDTH) * UNIT_SIZE;
                appleY = random.nextInt(GRID_HEIGHT) * UNIT_SIZE;

                // Check if apple spawns on snake
                for (int i = 0; i < bodyParts; i++) {
                    if (x[i] == appleX && y[i] == appleY) {
                        onSnake = true;
                        break;
                    }
                }
            } while (onSnake);

            attempts++;

            // If we can't find a good spot after many attempts, place anywhere safe
            if (attempts >= maxAttempts) {
                System.out.println("Could not find ideal apple spot, placing anywhere safe");
                // Find any safe cell
                for (int x = 0; x < GRID_WIDTH; x++) {
                    for (int y = 0; y < GRID_HEIGHT; y++) {
                        Point cell = new Point(x, y);
                        if (isCellSafe(cell) && !cell.equals(getSnakeHead())) {
                            appleX = x * UNIT_SIZE;
                            appleY = y * UNIT_SIZE;
                            return;
                        }
                    }
                }
                // If no safe cell, place randomly (game might end soon anyway)
                break;
            }
        } while (!pathExistsToApple() || attempts < 10);

        System.out.println("Placed apple at (" + (appleX/UNIT_SIZE) + ", " + (appleY/UNIT_SIZE) + ")");
    }

    // Single newApple method that handles both modes
    public void newApple() {
        if (gameMode.equals("HUMAN")) {
            // For human mode, use random placement
            boolean onSnake;
            do {
                onSnake = false;
                appleX = random.nextInt(GRID_WIDTH) * UNIT_SIZE;
                appleY = random.nextInt(GRID_HEIGHT) * UNIT_SIZE;

                // Check if apple spawns on snake
                for (int i = 0; i < bodyParts; i++) {
                    if (x[i] == appleX && y[i] == appleY) {
                        onSnake = true;
                        break;
                    }
                }
            } while (onSnake);
        } else {
            // For algorithm modes, use smart placement
            placeSmartApple();
        }
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        String scoreText = "Score: " + applesEaten;
        g.drawString(scoreText, 10, 25);

        String modeText = "Mode: " + gameMode;
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(modeText, SCREEN_WIDTH - metrics.stringWidth(modeText) - 10, 25);

        // Draw algorithm info
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        if (!gameMode.equals("HUMAN")) {
            String algorithmInfo = "";
            switch (gameMode) {
                case "DFS": algorithmInfo = "DFS: Depth-First Search"; break;
                case "BFS": algorithmInfo = "BFS: Breadth-First Search"; break;
                case "ASTAR": algorithmInfo = "A*: A-Star Search"; break;
            }
            g.drawString(algorithmInfo, 10, SCREEN_HEIGHT - 50);

            // Draw path existence status
            if (!pathExistsToApple()) {
                g.setColor(Color.RED);
                g.drawString("NO PATH TO APPLE!", 10, SCREEN_HEIGHT - 30);
                g.setColor(Color.WHITE);
            }
        }

        // Draw controls
        g.drawString("SPACE: Restart | ESC: Menu", 10, SCREEN_HEIGHT - 10);
    }

    private void gameOver(Graphics g) {
        // Determine game over reason
        String reason;
        if (isSnakeTrapped()) {
            reason = "Snake Trapped Itself!";
        } else {
            reason = "Game Over";
        }

        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString(reason,
                (SCREEN_WIDTH - metrics1.stringWidth(reason)) / 2,
                SCREEN_HEIGHT / 2 - 50);

        // Score text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,
                (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2,
                SCREEN_HEIGHT / 2 + 20);

        // Instructions
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to restart",
                (SCREEN_WIDTH - metrics3.stringWidth("Press SPACE to restart")) / 2,
                SCREEN_HEIGHT / 2 + 80);

        g.drawString("Press ESC for main menu",
                (SCREEN_WIDTH - metrics3.stringWidth("Press ESC for main menu")) / 2,
                SCREEN_HEIGHT / 2 + 110);
    }

    public void move() {
        // Move body parts
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Move head based on direction
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public boolean moveTo(Point gridPoint) {
        int targetX = gridPoint.x * UNIT_SIZE;
        int targetY = gridPoint.y * UNIT_SIZE;

        // Store current head position
        int headX = x[0];
        int headY = y[0];

        // Calculate direction based on target
        if (targetX > headX) {
            direction = 'R';
        } else if (targetX < headX) {
            direction = 'L';
        } else if (targetY > headY) {
            direction = 'D';
        } else if (targetY < headY) {
            direction = 'U';
        }

        // Move the snake
        move();

        // Check for apple and collisions after moving
        checkApple();
        checkCollisions();

        // Repaint to show movement
        repaint();

        return running; // Return whether game is still running
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();

            // Restart algorithm after eating apple
            switch (gameMode) {
                case "DFS":
                    if (dfsVisualizer != null) dfsVisualizer.startDFS();
                    break;
                case "BFS":
                    if (bfsVisualizer != null) bfsVisualizer.startBFS();
                    break;
                case "ASTAR":
                    if (aStarVisualizer != null) aStarVisualizer.startAStar();
                    break;
            }
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                System.out.println("Collision with body at segment " + i);
                return;
            }
        }

        // Check if head touches borders
        if (x[0] < 0) {
            running = false;
            System.out.println("Hit left wall");
            return;
        }
        if (x[0] >= SCREEN_WIDTH) {
            running = false;
            System.out.println("Hit right wall");
            return;
        }
        if (y[0] < 0) {
            running = false;
            System.out.println("Hit top wall");
            return;
        }
        if (y[0] >= SCREEN_HEIGHT) {
            running = false;
            System.out.println("Hit bottom wall");
            return;
        }

        // Check if snake is trapped (only for algorithm modes)
        if (!gameMode.equals("HUMAN") && isSnakeTrapped()) {
            running = false;
            System.out.println("Snake trapped itself! No possible moves.");
            return;
        }

        if (!running) {
            if (timer != null) {
                timer.stop();
            }
            stopAllAlgorithms();
            System.out.println("Game Over! Final Score: " + applesEaten);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            if (gameMode.equals("HUMAN")) {
                move();
                checkApple();
                checkCollisions();
            }
            // In algorithm modes, movement is handled by visualizers
            repaint();
        }
    }

    public void restartGame() {
        System.out.println("Restarting game...");

        // Stop existing timers and algorithms
        if (timer != null) {
            timer.stop();
        }
        stopAllAlgorithms();

        // Stop battle if running
        if (battleManager != null) {
            battleManager.stopBattle();
        }

        // Reset game state
        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';
        running = true;
        gameStarted = true;

        // Start appropriate mode
        if (gameMode.equals("BATTLE")) {
            battleManager.startBattle();
        } else {
            initSnake();
            newApple();

            // Restart appropriate algorithm
            switch (gameMode) {
                case "DFS":
                    timer = new Timer(DFS_DELAY, this);
                    dfsVisualizer.startDFS();
                    break;
                case "BFS":
                    timer = new Timer(DFS_DELAY, this);
                    bfsVisualizer.startBFS();
                    break;
                case "ASTAR":
                    timer = new Timer(DFS_DELAY, this);
                    aStarVisualizer.startAStar();
                    break;
                default: // HUMAN
                    timer = new Timer(DELAY, this);
                    break;
            }

            timer.start();
        }

        requestFocusInWindow();
        repaint();
    }

    // Getters for visualizers
    public Point getSnakeHead() {
        return new Point(x[0] / UNIT_SIZE, y[0] / UNIT_SIZE);
    }

    public Point getApplePosition() {
        return new Point(appleX / UNIT_SIZE, appleY / UNIT_SIZE);
    }

    public int getGridWidth() {
        return GRID_WIDTH;
    }

    public int getGridHeight() {
        return GRID_HEIGHT;
    }

    public boolean isCellSafe(Point cell) {
        // Check if cell is within bounds
        if (cell.x < 0 || cell.x >= GRID_WIDTH ||
                cell.y < 0 || cell.y >= GRID_HEIGHT) {
            return false;
        }

        // Check if cell contains snake body
        for (int i = 0; i < bodyParts; i++) {
            if (x[i] / UNIT_SIZE == cell.x && y[i] / UNIT_SIZE == cell.y) {
                return false;
            }
        }

        return true;
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("Key pressed: " + e.getKeyCode());

            if (gameMode.equals("HUMAN")) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            }

            // Common controls
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    System.out.println("Space pressed - restarting");
                    restartGame();
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.out.println("ESC pressed - returning to menu");
                    if (timer != null) {
                        timer.stop();
                    }
                    stopAllAlgorithms();
                    if (battleManager != null) {
                        battleManager.stopBattle();
                    }
                    parentFrame.showMenu();
                    break;
            }
        }
    }
}