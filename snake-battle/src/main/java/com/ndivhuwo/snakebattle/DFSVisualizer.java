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
    private boolean isRunning;

    public DFSVisualizer(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void startDFS() {
        System.out.println("Starting DFS visualization");

        visited = new boolean[gamePanel.getGridWidth()][gamePanel.getGridHeight()];
        path = new Stack<>();
        visitedCells = new ArrayList<>();
        isRunning = true;

        // Start from snake head position
        Point start = gamePanel.getSnakeHead();
        path.push(start);
        visited[start.x][start.y] = true;

        dfsTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDFSStep();
            }
        });
        dfsTimer.start();
    }

    private void performDFSStep() {
        if (!isRunning || path.isEmpty()) {
            if (dfsTimer != null) {
                dfsTimer.stop();
            }
            return;
        }

        Point current = path.peek();

        // Check if we found the apple
        if (current.equals(gamePanel.getApplePosition())) {
            System.out.println("DFS found the apple!");
            executePath();
            return;
        }

        // Get all possible moves
        List<Point> neighbors = getNeighbors(current);
        boolean foundUnvisited = false;

        for (Point neighbor : neighbors) {
            if (!visited[neighbor.x][neighbor.y] && gamePanel.isCellSafe(neighbor)) {
                path.push(neighbor);
                visited[neighbor.x][neighbor.y] = true;
                visitedCells.add(neighbor);
                foundUnvisited = true;
                break;
            }
        }

        // If no unvisited neighbors, backtrack
        if (!foundUnvisited && !path.isEmpty()) {
            path.pop();
        }

        // If path is empty, restart DFS
        if (path.isEmpty()) {
            System.out.println("DFS got stuck, restarting...");
            startDFS();
        }
    }

    private List<Point> getNeighbors(Point point) {
        List<Point> neighbors = new ArrayList<>();

        // Define directions
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

    private void executePath() {
        dfsTimer.stop();

        // Convert stack to path
        List<Point> fullPath = new ArrayList<>(path);
        Collections.reverse(fullPath);

        // Remove the head position
        if (!fullPath.isEmpty()) {
            fullPath.remove(0);
        }

        if (fullPath.isEmpty()) {
            // Already at apple
            gamePanel.newApple();
            startDFS();
            return;
        }

        // Execute path step by step
        Timer executionTimer = new Timer(200, new ActionListener() {
            private int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < fullPath.size()) {
                    gamePanel.moveTo(fullPath.get(index));
                    index++;
                } else {
                    ((Timer)e.getSource()).stop();
                    // Restart DFS after reaching apple
                    SwingUtilities.invokeLater(() -> startDFS());
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
    }
}