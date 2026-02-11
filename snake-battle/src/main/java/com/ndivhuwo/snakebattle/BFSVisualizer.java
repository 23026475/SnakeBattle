package com.ndivhuwo.snakebattle;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BFSVisualizer {

    private GamePanel gamePanel;
    private boolean[][] visited;
    private List<Point> visitedCells;
    private Timer bfsTimer;
    private boolean isRunning;

    // BFS data structures
    private Queue<Point> queue;
    private Map<Point, Point> parentMap;
    private Point current;

    public BFSVisualizer(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void startBFS() {
        System.out.println("Starting BFS visualization");

        stop();

        visited = new boolean[gamePanel.getGridWidth()][gamePanel.getGridHeight()];
        visitedCells = new ArrayList<>();
        queue = new LinkedList<>();
        parentMap = new HashMap<>();
        isRunning = true;

        // Start from snake head
        Point start = gamePanel.getSnakeHead();
        queue.add(start);
        visited[start.x][start.y] = true;
        parentMap.put(start, null);

        bfsTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performBFSStep();
            }
        });
        bfsTimer.start();
    }

    private void performBFSStep() {
        if (!gamePanel.isGameRunning()) {
            System.out.println("Game over detected, stopping BFS");
            stop();
            return;
        }

        // Check if path exists before searching
        if (!gamePanel.pathExistsToApple()) {
            System.out.println("BFS: No path exists to apple! Snake is trapped.");
            return;
        }

        if (!isRunning || queue.isEmpty()) {
            if (bfsTimer != null) {
                bfsTimer.stop();
            }

            // If queue is empty and we're still running, snake might be trapped
            if (isRunning && gamePanel.isSnakeTrapped()) {
                System.out.println("BFS: Queue empty and snake trapped!");
            }
            return;
        }

        // Process one node from the queue
        current = queue.poll();
        visitedCells.add(current);

        // Check if we found the apple
        if (current.equals(gamePanel.getApplePosition())) {
            System.out.println("BFS found the apple! Reconstructing shortest path...");
            reconstructAndExecutePath(current);
            return;
        }

        // Explore neighbors
        for (Point neighbor : getNeighbors(current)) {
            if (!visited[neighbor.x][neighbor.y] && gamePanel.isCellSafe(neighbor)) {
                visited[neighbor.x][neighbor.y] = true;
                queue.add(neighbor);
                parentMap.put(neighbor, current);
            }
        }

        gamePanel.repaint();
    }

    private List<Point> getNeighbors(Point point) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

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

    private void reconstructAndExecutePath(Point goal) {
        bfsTimer.stop();

        // Reconstruct shortest path (BFS guarantees shortest)
        List<Point> shortestPath = new ArrayList<>();
        Point current = goal;

        while (current != null) {
            shortestPath.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(shortestPath);

        System.out.println("BFS shortest path found with " + shortestPath.size() + " steps");

        // Remove the head position
        if (!shortestPath.isEmpty()) {
            shortestPath.remove(0);
        }

        if (shortestPath.isEmpty()) {
            System.out.println("Already at apple!");
            return;
        }

        // Execute the path
        Timer executionTimer = new Timer(150, new ActionListener() {
            private int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gamePanel.isGameRunning()) {
                    ((Timer)e.getSource()).stop();
                    return;
                }

                if (index < shortestPath.size()) {
                    gamePanel.moveTo(shortestPath.get(index));
                    index++;

                    if (index >= shortestPath.size()) {
                        ((Timer)e.getSource()).stop();
                        // Restart BFS
                        SwingUtilities.invokeLater(() -> startBFS());
                    }
                }
            }
        });
        executionTimer.start();
    }

    public List<Point> getVisitedCells() {
        return visitedCells;
    }

    public Point getCurrentNode() {
        return current;
    }

    public void stop() {
        isRunning = false;
        if (bfsTimer != null) {
            bfsTimer.stop();
        }
    }
}