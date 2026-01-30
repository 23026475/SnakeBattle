package com.ndivhuwo.snakebattle.ai;

import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.*;

/**
 * Hunter AI: chases the nearest enemy snake head.
 */
public class HunterAI implements SnakeAI {

    @Override
    public Direction nextMove(GameState state, Snake snake) {
        Position start = snake.head();
        Position target = findClosestEnemyHead(state, snake);

        if (target == null) {
            return randomSafeMove(state, snake);
        }

        Queue<Position> queue = new LinkedList<>();
        Map<Position, Position> cameFrom = new HashMap<>();
        Set<Position> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            if (current.equals(target)) {
                return reconstructMove(start, target, cameFrom);
            }

            for (Position next : state.grid().neighbors(current)) {
                if (visited.contains(next)) continue;
                if (!state.isSafe(next) && !next.equals(start)) continue;

                visited.add(next);
                cameFrom.put(next, current);
                queue.add(next);
            }
        }

        return randomSafeMove(state, snake);
    }

    /* ---------- Helpers ---------- */

    private Position findClosestEnemyHead(GameState state, Snake self) {
        Position closest = null;
        int best = Integer.MAX_VALUE;

        for (Snake s : state.snakes()) {
            if (s == self || !s.isAlive()) continue;

            int d = manhattan(self.head(), s.head());
            if (d < best) {
                best = d;
                closest = s.head();
            }
        }
        return closest;
    }

    private int manhattan(Position a, Position b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
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
}
