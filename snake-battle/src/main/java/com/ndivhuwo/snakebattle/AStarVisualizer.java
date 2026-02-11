package com.ndivhuwo.snakebattle;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AStarVisualizer {

    private GamePanel gamePanel;
    private boolean[][] visited;
    private List<Point> visitedCells;
    private Timer aStarTimer;
    private boolean isRunning;

    // A* data structures
    private PriorityQueue<Node> openSet;
    private Set<Point> closedSet;
    private Map<Point, Point> cameFrom;
    private Map<Point, Double> gScore;
    private Map<Point, Double> fScore;

    public class Node implements Comparable<Node> {
        Point point;
        double fScore;

        Node(Point point, double fScore) {
            this.point = point;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fScore, other.fScore);
        }
    }

    public AStarVisualizer(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void startAStar() {
        System.out.println("Starting A* visualization");

        stop();

        visited = new boolean[gamePanel.getGridWidth()][gamePanel.getGridHeight()];
        visitedCells = new ArrayList<>();
        openSet = new PriorityQueue<>();
        closedSet = new HashSet<>();
        cameFrom = new HashMap<>();
        gScore = new HashMap<>();
        fScore = new HashMap<>();
        isRunning = true;

        Point start = gamePanel.getSnakeHead();
        Point goal = gamePanel.getApplePosition();

        // Initialize scores
        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));

        openSet.add(new Node(start, fScore.get(start)));

        aStarTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAStarStep(goal);
            }
        });
        aStarTimer.start();
    }

    private void performAStarStep(Point goal) {
        if (!gamePanel.isGameRunning()) {
            System.out.println("Game over detected, stopping A*");
            stop();
            return;
        }

        // Check if path exists before searching
        if (!gamePanel.pathExistsToApple()) {
            System.out.println("A*: No path exists to apple! Snake is trapped.");
            return;
        }

        if (!isRunning || openSet.isEmpty()) {
            if (aStarTimer != null) {
                aStarTimer.stop();
            }

            // Check if snake is trapped when open set is empty
            if (isRunning && openSet.isEmpty() && gamePanel.isSnakeTrapped()) {
                System.out.println("A*: Open set empty and snake trapped!");
            } else if (isRunning && openSet.isEmpty()) {
                System.out.println("A* couldn't find path, restarting...");
                startAStar();
            }
            return;
        }

        // Get node with lowest fScore
        Node current = openSet.poll();
        visited[current.point.x][current.point.y] = true;
        visitedCells.add(current.point);

        // Check if we found the goal
        if (current.point.equals(goal)) {
            System.out.println("A* found the apple! Reconstructing optimal path...");
            reconstructAndExecutePath(current.point);
            return;
        }

        closedSet.add(current.point);

        // Explore neighbors
        for (Point neighbor : getNeighbors(current.point)) {
            if (closedSet.contains(neighbor) || !gamePanel.isCellSafe(neighbor)) {
                continue;
            }

            // Calculate tentative gScore
            double tentativeGScore = gScore.getOrDefault(current.point, Double.MAX_VALUE) + 1;

            if (!openSetContains(neighbor) || tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                cameFrom.put(neighbor, current.point);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));

                if (!openSetContains(neighbor)) {
                    openSet.add(new Node(neighbor, fScore.get(neighbor)));
                }
            }
        }

        gamePanel.repaint();
    }

    private boolean openSetContains(Point point) {
        for (Node node : openSet) {
            if (node.point.equals(point)) {
                return true;
            }
        }
        return false;
    }

    public List<Point> getOpenSetPoints() {
        List<Point> points = new ArrayList<>();
        if (openSet != null) {
            for (Node node : openSet) {
                points.add(node.point);
            }
        }
        return points;
    }

    private double heuristic(Point a, Point b) {
        // Manhattan distance for grid
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
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
        aStarTimer.stop();

        // Reconstruct path
        List<Point> path = new ArrayList<>();
        Point current = goal;

        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }

        Collections.reverse(path);

        System.out.println("A* optimal path found with " + path.size() + " steps");

        // Remove the head position
        if (!path.isEmpty()) {
            path.remove(0);
        }

        if (path.isEmpty()) {
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

                if (index < path.size()) {
                    gamePanel.moveTo(path.get(index));
                    index++;

                    if (index >= path.size()) {
                        ((Timer)e.getSource()).stop();
                        // Restart A*
                        SwingUtilities.invokeLater(() -> startAStar());
                    }
                }
            }
        });
        executionTimer.start();
    }

    public List<Point> getVisitedCells() {
        return visitedCells;
    }

    public PriorityQueue<Node> getOpenSet() {
        return openSet;
    }

    public void stop() {
        isRunning = false;
        if (aStarTimer != null) {
            aStarTimer.stop();
        }
    }
}