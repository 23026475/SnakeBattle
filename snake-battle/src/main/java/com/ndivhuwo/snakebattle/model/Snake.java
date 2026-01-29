package com.ndivhuwo.snakebattle.model;

import java.util.Deque;
import java.util.LinkedList;

public class Snake {

    private final Deque<Position> body = new LinkedList<>();
    private boolean alive = true;
    private int foodEaten = 0;
    private int snakesEaten = 0;
    private int turnsSurvived = 0;

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
    public void incrementFoodEaten() {
        foodEaten++;
    }

    public void incrementSnakesEaten() {
        snakesEaten++;
    }

    public void incrementTurnsSurvived() {
        turnsSurvived++;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public int getSnakesEaten() {
        return snakesEaten;
    }

    public int getTurnsSurvived() {
        return turnsSurvived;
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
