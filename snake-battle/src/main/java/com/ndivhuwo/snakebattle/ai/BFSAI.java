package com.ndivhuwo.snakebattle.ai;

import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import java.util.*;

/**
 * BFS-based AI that finds the shortest path to the nearest food.
 */
public class BFSAI implements SnakeAI {

    @Override
    public Direction nextMove(GameState state, Snake snake) {
        Position start = snake.head();

        Queue<Position> queue = new LinkedList<>();
        Map<Position, Position> cameFrom = new HashMap<>();
        Set<Position> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        Position target = null;

        // BFS search
        while (!queue.isEmpty()) {
            Position current = queue.poll();

            if (state.hasFoodAt(current)) {
                target = current;
                break;
            }

            for (Position next : state.grid().neighbors(current)) {
                if (visited.contains(next)) continue;
                if (!state.isSafe(next) && !next.equals(start)) continue;

                visited.add(next);
                cameFrom.put(next, current);
                queue.add(next);
            }
        }

        // No food reachable → random safe move
        if (target == null) {
            return randomSafeMove(state, snake);
        }

        // Backtrack to find first step
        Position step = target;
        while (!cameFrom.get(step).equals(start)) {
            step = cameFrom.get(step);
        }

        return directionFromTo(start, step);
    }

    private Direction randomSafeMove(GameState state, Snake snake) {
        List<Direction> safeMoves = new ArrayList<>();

        for (Direction d : Direction.values()) {
            Position next = snake.head().move(d);
            if (state.isSafe(next)) {
                safeMoves.add(d);
            }
        }

        if (safeMoves.isEmpty()) {
            return Direction.UP; // fallback
        }

        return safeMoves.get(new Random().nextInt(safeMoves.size()));
    }

    private Direction directionFromTo(Position from, Position to) {
        if (to.x() > from.x()) return Direction.RIGHT;
        if (to.x() < from.x()) return Direction.LEFT;
        if (to.y() > from.y()) return Direction.DOWN;
        return Direction.UP;
    }
}
