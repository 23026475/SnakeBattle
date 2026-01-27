package com.ndivhuwo.snakebattle;

import java.util.Deque;
import java.util.LinkedList;

public class Snake {

    private final Deque<Position> body = new LinkedList<>();
    private boolean alive = true;

    public Snake(Position startPosition) {
        body.addFirst(startPosition);
    }

    public Position head() {
        return body.peekFirst();
    }

    public Deque<Position> body() {
        return body;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }

    /**
     * Moves the snake one step in the given direction.
     * If grow is false, the tail is removed.
     */
    public void move(Direction direction, boolean grow) {
        Position newHead = head().move(direction);
        body.addFirst(newHead);

        if (!grow) {
            body.removeLast();
        }
    }
}
