package com.ndivhuwo.snakebattle;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class DFSVisualizer {

    private GamePanel gamePanel;
    private boolean[][] visited;
    private Stack<Point> path;
    private List<Point> visitedCells;
    private Timer dfsTimer;
    private Timer executionTimer;
    private boolean isRunning;

    // Store parent relationships for path reconstruction
    private Map<Point, Point> parentMap;

    public DFSVisualizer(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void startDFS() {
        System.out.println("Starting DFS visualization");

        // Stop any existing timers
        stop();

        visited = new boolean[gamePanel.getGridWidth()][gamePanel.getGridHeight()];
        path = new Stack<>();
        visitedCells = new ArrayList<>();
        parentMap = new HashMap<>();
        isRunning = true;

        // Start from snake head position
        Point start = gamePanel.getSnakeHead();
        path.push(start);
        visited[start.x][start.y] = true;
        parentMap.put(start, null); // Start has no parent

        dfsTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDFSStep();
            }
        });
        dfsTimer.start();
    }

    private void performDFSStep() {
        // Check if game is still running
        if (!gamePanel.isGameRunning()) {
            System.out.println("Game over detected, stopping DFS");
            stop();
            return;
        }

        // Check if path exists before searching
        if (!gamePanel.pathExistsToApple()) {
            System.out.println("DFS: No path exists to apple! Snake is trapped.");
            // Let the game handle the game over state
            return;
        }

        if (!isRunning || path.isEmpty()) {
            if (dfsTimer != null) {
                dfsTimer.stop();
            }
            return;
        }

        Point current = path.peek();

        // Check if we found the apple
        if (current.equals(gamePanel.getApplePosition())) {
            System.out.println("DFS found the apple! Reconstructing optimal path...");
            reconstructAndExecutePath(current);
            return;
        }

        // Get all possible moves in a logical order
        List<Point> neighbors = getNeighbors(current);
        boolean foundUnvisited = false;

        for (Point neighbor : neighbors) {
            if (!visited[neighbor.x][neighbor.y] && gamePanel.isCellSafe(neighbor)) {
                path.push(neighbor);
                visited[neighbor.x][neighbor.y] = true;
                parentMap.put(neighbor, current);
                visitedCells.add(neighbor);
                foundUnvisited = true;
                break;
            }
        }

        // If no unvisited neighbors, backtrack
        if (!foundUnvisited && !path.isEmpty()) {
            path.pop();
        }

        // If DFS gets completely stuck, check if snake is trapped
        if (path.isEmpty()) {
            System.out.println("DFS: Exploration stack empty, checking if trapped...");
            if (gamePanel.isSnakeTrapped()) {
                System.out.println("DFS: Snake is trapped, ending game.");
                // Game over will be detected in next iteration
            } else {
                System.out.println("DFS got stuck, restarting search...");
                startDFS();
            }
        }

        gamePanel.repaint();
    }

    public void debugVisitedCells() {
        System.out.println("=== DFS Debug Info ===");
        System.out.println("Total visited cells: " + visitedCells.size());
        System.out.println("Current path size: " + path.size());

        if (!visitedCells.isEmpty()) {
            System.out.println("Last 5 visited cells:");
            int start = Math.max(0, visitedCells.size() - 5);
            for (int i = start; i < visitedCells.size(); i++) {
                Point p = visitedCells.get(i);
                System.out.println("  Cell " + i + ": (" + p.x + ", " + p.y + ")");
            }
        }

        if (!path.isEmpty()) {
            System.out.println("Current path:");
            for (Point p : path) {
                System.out.println("  (" + p.x + ", " + p.y + ")");
            }
        }
        System.out.println("=====================");
    }

    private List<Point> getNeighbors(Point point) {
        List<Point> neighbors = new ArrayList<>();

        // Get apple position for heuristic
        Point apple = gamePanel.getApplePosition();

        // Create all possible directions
        int[][] allDirections = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        // Sort directions based on distance to apple (heuristic)
        List<int[]> directions = new ArrayList<>(Arrays.asList(allDirections));
        directions.sort((a, b) -> {
            Point neighborA = new Point(point.x + a[0], point.y + a[1]);
            Point neighborB = new Point(point.x + b[0], point.y + b[1]);

            double distA = distance(neighborA, apple);
            double distB = distance(neighborB, apple);

            return Double.compare(distA, distB);
        });

        for (int[] dir : directions) {
            int newX = point.x + dir[0];
            int newY = point.y + dir[1];

            if (newX >= 0 && newX < gamePanel.getGridWidth() &&
                    newY >= 0 && newY < gamePanel.getGridHeight()) {
                neighbors.add(new Point(newX, newY));
            }
        }

        return neighbors;
    }

    private double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private void reconstructAndExecutePath(Point goal) {
        if (dfsTimer != null) {
            dfsTimer.stop();
        }

        // Reconstruct optimal path from goal to start using parentMap
        List<Point> optimalPath = new ArrayList<>();
        Point current = goal;

        while (current != null) {
            optimalPath.add(current);
            current = parentMap.get(current);
        }

        // Reverse to get path from start to goal
        Collections.reverse(optimalPath);

        System.out.println("Optimal path found with " + optimalPath.size() + " steps");

        // Remove the head position (we're already there)
        if (!optimalPath.isEmpty()) {
            optimalPath.remove(0);
        }

        if (optimalPath.isEmpty()) {
            System.out.println("Already at apple!");
            return;
        }

        // Execute the optimal path step by step
        if (executionTimer != null && executionTimer.isRunning()) {
            executionTimer.stop();
        }

        executionTimer = new Timer(200, new ActionListener() {
            private int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if game is still running
                if (!gamePanel.isGameRunning()) {
                    System.out.println("Game over during path execution, stopping...");
                    ((Timer)e.getSource()).stop();
                    return;
                }

                if (index < optimalPath.size()) {
                    Point next = optimalPath.get(index);
                    System.out.println("Moving to step " + (index + 1) + "/" + optimalPath.size() + ": (" + next.x + ", " + next.y + ")");

                    // Move the snake
                    gamePanel.moveTo(next);

                    // Check if we successfully moved and game is still running
                    if (gamePanel.isGameRunning()) {
                        index++;

                        // If we completed the path
                        if (index >= optimalPath.size()) {
                            ((Timer)e.getSource()).stop();
                            System.out.println("Path completed! Restarting DFS search...");

                            // Small delay before restarting DFS
                            Timer restartTimer = new Timer(500, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (gamePanel.isGameRunning()) {
                                        startDFS();
                                    }
                                    ((Timer)e.getSource()).stop();
                                }
                            });
                            restartTimer.setRepeats(false);
                            restartTimer.start();
                        }
                    } else {
                        // Game ended during movement
                        ((Timer)e.getSource()).stop();
                        System.out.println("Game ended during movement");
                    }
                }
            }
        });
        executionTimer.start();
    }

    public List<Point> getVisitedCells() {
        return visitedCells;
    }

    public Stack<Point> getCurrentPath() {
        return path;
    }

    public void stop() {
        isRunning = false;
        if (dfsTimer != null) {
            dfsTimer.stop();
        }
        if (executionTimer != null) {
            executionTimer.stop();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}