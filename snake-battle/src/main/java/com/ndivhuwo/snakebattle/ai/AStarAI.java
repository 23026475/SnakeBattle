package com.ndivhuwo.snakebattle.ai;

import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.*;

/**
 * A* pathfinding AI using Manhattan distance heuristic.
 */
public class AStarAI implements SnakeAI {

    @Override
    public Direction nextMove(GameState state, Snake snake) {
        Position start = snake.head();

        // Find closest food
        Position goal = findClosestFood(state, start);
        if (goal == null) {
            return randomSafeMove(state, snake);
        }

        PriorityQueue<Node> open = new PriorityQueue<>();
        Map<Position, Position> cameFrom = new HashMap<>();
        Map<Position, Integer> gScore = new HashMap<>();

        gScore.put(start, 0);
        open.add(new Node(start, heuristic(start, goal)));

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.pos.equals(goal)) {
                return reconstructMove(start, goal, cameFrom);
            }

            for (Position next : state.grid().neighbors(current.pos)) {
                if (!state.isSafe(next) && !next.equals(start)) continue;

                int tentativeG = gScore.get(current.pos) + 1;

                if (tentativeG < gScore.getOrDefault(next, Integer.MAX_VALUE)) {
                    cameFrom.put(next, current.pos);
                    gScore.put(next, tentativeG);
                    int f = tentativeG + heuristic(next, goal);
                    open.add(new Node(next, f));
                }
            }
        }

        return randomSafeMove(state, snake);
    }

    /* ---------- Helpers ---------- */

    private int heuristic(Position a, Position b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    private Position findClosestFood(GameState state, Position from) {
        Position closest = null;
        int best = Integer.MAX_VALUE;

        for (Position food : state.foodPositions()) {
            int d = heuristic(from, food);
            if (d < best) {
                best = d;
                closest = food;
            }
        }
        return closest;
    }

    private Direction reconstructMove(Position start, Position goal, Map<Position, Position> cameFrom) {
        Position step = goal;
        while (!cameFrom.get(step).equals(start)) {
            step = cameFrom.get(step);
        }
        return directionFromTo(start, step);
    }

    private Direction randomSafeMove(GameState state, Snake snake) {
        List<Direction> safe = new ArrayList<>();
        for (Direction d : Direction.values()) {
            Position next = snake.head().move(d);
            if (state.isSafe(next)) safe.add(d);
        }
        return safe.isEmpty() ? Direction.UP : safe.get(new Random().nextInt(safe.size()));
    }

    private Direction directionFromTo(Position from, Position to) {
        if (to.x() > from.x()) return Direction.RIGHT;
        if (to.x() < from.x()) return Direction.LEFT;
        if (to.y() > from.y()) return Direction.DOWN;
        return Direction.UP;
    }

    /* ---------- Internal Node ---------- */
    private static class Node implements Comparable<Node> {
        Position pos;
        int fScore;

        Node(Position pos, int fScore) {
            this.pos = pos;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fScore, other.fScore);
        }
    }
}
