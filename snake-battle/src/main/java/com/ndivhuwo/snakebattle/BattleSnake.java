package com.ndivhuwo.snakebattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class BattleSnake {
    // Snake properties
    private int id;
    private Color color;
    private String algorithm;
    private List<Point> body;
    private Direction direction;
    private boolean alive;
    private int score;

    // Algorithm visualizer
    private Object algorithmVisualizer; // Can be DFS, BFS, or AStar

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public BattleSnake(int id, Color color, String algorithm, Point startPosition) {
        this.id = id;
        this.color = color;
        this.algorithm = algorithm;
        this.alive = true;
        this.score = 0;
        this.direction = Direction.RIGHT;

        // Initialize snake with 3 segments
        body = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            body.add(new Point(startPosition.x - i, startPosition.y));
        }

        // Initialize appropriate algorithm
        initializeAlgorithm();
    }

    private void initializeAlgorithm() {
        // Note: We'll need a GamePanel reference to create visualizers
        // This will be handled in BattleManager
    }

    public void move(Point nextPosition) {
        if (!alive) return;

        // Move snake: add new head, remove tail
        body.add(0, nextPosition);
        body.remove(body.size() - 1);
    }

    public void grow() {
        if (!alive || body.isEmpty()) return;

        // Add a new segment at the tail position
        Point tail = body.get(body.size() - 1);
        body.add(new Point(tail.x, tail.y));
        score++;
    }

    public boolean contains(Point point) {
        return body.contains(point);
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    public Point getHead() {
        return body.isEmpty() ? null : body.get(0);
    }

    public List<Point> getBody() {
        return new ArrayList<>(body);
    }

    public Color getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getId() {
        return id;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Object getAlgorithmVisualizer() {
        return algorithmVisualizer;
    }

    public void setAlgorithmVisualizer(Object visualizer) {
        this.algorithmVisualizer = visualizer;
    }
}