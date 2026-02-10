package com.ndivhuwo.snakebattle.ai;

import java.util.*;

public class Search {

    /* =========================
       GAME SNAPSHOT
       ========================= */

    public static class State {
        public final int width;
        public final int height;
        public final Set<GridNode> obstacles;

        public State(int width, int height, Set<GridNode> obstacles) {
            this.width = width;
            this.height = height;
            this.obstacles = obstacles;
        }

        public boolean isInside(GridNode n) {
            return n.x >= 0 && n.y >= 0 && n.x < width && n.y < height;
        }

        public boolean isFree(GridNode n) {
            return !obstacles.contains(n);
        }
    }

    /* =========================
       COMMON NEIGHBORS
       ========================= */

    private static List<GridNode> neighbors(GridNode n) {
        List<GridNode> list = new ArrayList<>();
        for (Direction d : Direction.values()) {
            list.add(new GridNode(n.x + d.dx, n.y + d.dy));
        }
        return list;
    }

    /* =========================
       BFS
       ========================= */

    public static List<GridNode> bfs(State state, GridNode start, GridNode goal) {
        Queue<GridNode> queue = new ArrayDeque<>();
        Map<GridNode, GridNode> parent = new HashMap<>();

        queue.add(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            GridNode current = queue.poll();

            if (current.equals(goal))
                return reconstructPath(parent, goal);

            for (GridNode next : neighbors(current)) {
                if (!state.isInside(next) || !state.isFree(next)) continue;
                if (parent.containsKey(next)) continue;

                parent.put(next, current);
                queue.add(next);
            }
        }
        return List.of();
    }

    /* =========================
       DFS
       ========================= */

    public static List<GridNode> dfs(State state, GridNode start, GridNode goal) {
        Stack<GridNode> stack = new Stack<>();
        Map<GridNode, GridNode> parent = new HashMap<>();

        stack.push(start);
        parent.put(start, null);

        while (!stack.isEmpty()) {
            GridNode current = stack.pop();

            if (current.equals(goal))
                return reconstructPath(parent, goal);

            for (GridNode next : neighbors(current)) {
                if (!state.isInside(next) || !state.isFree(next)) continue;
                if (parent.containsKey(next)) continue;

                parent.put(next, current);
                stack.push(next);
            }
        }
        return List.of();
    }

    /* =========================
       GREEDY BFS
       ========================= */

    public static List<GridNode> greedy(State state, GridNode start, GridNode goal) {
        PriorityQueue<GridNode> pq = new PriorityQueue<>(
                Comparator.comparingInt(n -> heuristic(n, goal))
        );
        Map<GridNode, GridNode> parent = new HashMap<>();

        pq.add(start);
        parent.put(start, null);

        while (!pq.isEmpty()) {
            GridNode current = pq.poll();

            if (current.equals(goal))
                return reconstructPath(parent, goal);

            for (GridNode next : neighbors(current)) {
                if (!state.isInside(next) || !state.isFree(next)) continue;
                if (parent.containsKey(next)) continue;

                parent.put(next, current);
                pq.add(next);
            }
        }
        return List.of();
    }

    /* =========================
       A*
       ========================= */

    public static List<GridNode> aStar(State state, GridNode start, GridNode goal) {
        PriorityQueue<GridNode> open = new PriorityQueue<>(
                Comparator.comparingInt(n -> gScore.getOrDefault(n, Integer.MAX_VALUE)
                        + heuristic(n, goal))
        );

        Map<GridNode, GridNode> parent = new HashMap<>();
        gScore = new HashMap<>();

        gScore.put(start, 0);
        parent.put(start, null);
        open.add(start);

        while (!open.isEmpty()) {
            GridNode current = open.poll();

            if (current.equals(goal))
                return reconstructPath(parent, goal);

            for (GridNode next : neighbors(current)) {
                if (!state.isInside(next) || !state.isFree(next)) continue;

                int tentativeG = gScore.get(current) + 1;
                if (tentativeG < gScore.getOrDefault(next, Integer.MAX_VALUE)) {
                    gScore.put(next, tentativeG);
                    parent.put(next, current);
                    open.add(next);
                }
            }
        }
        return List.of();
    }

    private static Map<GridNode, Integer> gScore;

    /* =========================
       HELPERS
       ========================= */

    private static int heuristic(GridNode a, GridNode b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static List<GridNode> reconstructPath(
            Map<GridNode, GridNode> parent, GridNode goal) {

        List<GridNode> path = new ArrayList<>();
        for (GridNode cur = goal; cur != null; cur = parent.get(cur)) {
            path.add(cur);
        }
        Collections.reverse(path);
        return path;
    }
}
