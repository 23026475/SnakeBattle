package com.ndivhuwo.snakebattle.model;

public class SnakeStats {
    private final Snake snake;
    private int foodEaten = 0;
    private int stepsSurvived = 0;
    private boolean alive = true;

    public SnakeStats(Snake snake) {
        this.snake = snake;
    }

    public Snake getSnake() {
        return snake;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public int getStepsSurvived() {
        return stepsSurvived;
    }

    public boolean isAlive() {
        return alive;
    }

    public void addFood() {
        foodEaten++;
    }

    public void stepSurvived() {
        if (alive) stepsSurvived++;
    }

    public void die() {
        alive = false;
    }

    @Override
    public String toString() {
        return snake + " | Food eaten: " + foodEaten + " | Steps survived: " + stepsSurvived + " | Alive: " + alive;
    }
}
