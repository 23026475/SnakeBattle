package com.ndivhuwo.snakebattle.ai;

public class GridNode {
    public final int x;
    public final int y;

    public GridNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GridNode)) return false;
        GridNode other = (GridNode) o;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
