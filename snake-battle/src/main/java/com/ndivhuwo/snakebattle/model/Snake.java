package com.ndivhuwo.snakebattle.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a snake in the game.
 * Tracks its body positions, alive status, and provides movement logic.
 */
public class Snake {

    private final LinkedList<Position> body = new LinkedList<>();
    private boolean alive = true;

    /**
     * Creates a new snake with its head at the starting position.
     */
    public Snake(Position start) {
        body.add(start);
    }

    /**
     * Moves the snake in the given direction.
     * If grow is true, the tail is not removed (snake grows).
     */
    public void move(Direction dir, boolean grow) {
        Position newHead = body.getFirst().move(dir);
        body.addFirst(newHead);
        if (!grow) {
            body.removeLast();
        }
    }

    /**
     * Returns the current head position.
     */
    public Position head() {
        return body.getFirst();
    }

    /**
     * Returns the full body positions of the snake.
     */
    public List<Position> body() {
        return body;
    }

    /**
     * Returns whether the snake is alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Kills the snake.
     */
    public void kill() {
        alive = false;
    }
}
