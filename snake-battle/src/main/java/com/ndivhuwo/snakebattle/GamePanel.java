package com.ndivhuwo.snakebattle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

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

    // DFS Visualization
    private DFSVisualizer dfsVisualizer;

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

        // Initialize DFS visualizer
        dfsVisualizer = new DFSVisualizer(this);
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

        initSnake();
        newApple();

        // Start timer based on mode
        if (gameMode.equals("DFS")) {
            timer = new Timer(DFS_DELAY, this);
            dfsVisualizer.startDFS();
        } else {
            timer = new Timer(DELAY, this);
        }

        timer.start();
        requestFocusInWindow();

        System.out.println("Game started successfully");
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw DFS visualization if in DFS mode
            if (gameMode.equals("DFS")) {
                drawDFSVisualization(g);
            }

            // Draw apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    // Head of snake
                    g.setColor(Color.GREEN);
                } else {
                    // Body of snake
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                // Draw border around each segment
                g.setColor(Color.BLACK);
                g.drawRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            drawHUD(g);

        } else {
            gameOver(g);
        }
    }

    private void drawDFSVisualization(Graphics g) {
        if (dfsVisualizer != null) {
            // Draw visited cells
            for (Point cell : dfsVisualizer.getVisitedCells()) {
                g.setColor(new Color(30, 30, 150, 100));
                g.fillRect(cell.x * UNIT_SIZE, cell.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw current path
            java.util.Stack<Point> path = dfsVisualizer.getCurrentPath();
            if (!path.isEmpty()) {
                for (Point cell : path) {
                    g.setColor(new Color(0, 200, 0, 150));
                    g.fillRect(cell.x * UNIT_SIZE, cell.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                }
            }
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

        // Draw controls
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        if (gameMode.equals("HUMAN")) {
            g.drawString("Arrow Keys: Move | SPACE: Restart | ESC: Menu", 10, SCREEN_HEIGHT - 10);
        }
    }

    private void gameOver(Graphics g) {
        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2,
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

    public void newApple() {
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

    public void moveTo(Point gridPoint) {
        int targetX = gridPoint.x * UNIT_SIZE;
        int targetY = gridPoint.y * UNIT_SIZE;

        // Determine direction based on current head position
        int headX = x[0];
        int headY = y[0];

        if (targetX > headX) direction = 'R';
        else if (targetX < headX) direction = 'L';
        else if (targetY > headY) direction = 'D';
        else if (targetY < headY) direction = 'U';

        move();
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();

            // If in DFS mode, restart DFS after eating apple
            if (gameMode.equals("DFS")) {
                dfsVisualizer.startDFS();
            }
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                System.out.println("Collision with body at segment " + i);
            }
        }

        // Check if head touches borders
        if (x[0] < 0) {
            running = false;
            System.out.println("Hit left wall");
        }
        if (x[0] >= SCREEN_WIDTH) {
            running = false;
            System.out.println("Hit right wall");
        }
        if (y[0] < 0) {
            running = false;
            System.out.println("Hit top wall");
        }
        if (y[0] >= SCREEN_HEIGHT) {
            running = false;
            System.out.println("Hit bottom wall");
        }

        if (!running) {
            if (timer != null) {
                timer.stop();
            }
            if (gameMode.equals("DFS") && dfsVisualizer != null) {
                dfsVisualizer.stop();
            }
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
            // In DFS mode, movement is handled by DFSVisualizer
            repaint();
        }
    }

    public void restartGame() {
        System.out.println("Restarting game...");

        // Stop existing timers
        if (timer != null) {
            timer.stop();
        }
        if (gameMode.equals("DFS") && dfsVisualizer != null) {
            dfsVisualizer.stop();
        }

        // Reset game state
        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';
        running = true;

        initSnake();
        newApple();

        // Restart timers
        if (gameMode.equals("DFS")) {
            timer = new Timer(DFS_DELAY, this);
            dfsVisualizer.startDFS();
        } else {
            timer = new Timer(DELAY, this);
        }

        timer.start();
        requestFocusInWindow();
    }

    // Getters for DFSVisualizer
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
                    if (gameMode.equals("DFS") && dfsVisualizer != null) {
                        dfsVisualizer.stop();
                    }
                    parentFrame.showMenu();
                    break;
            }
        }
    }
}