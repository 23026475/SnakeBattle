package com.ndivhuwo.snakebattle;

import com.ndivhuwo.snakebattle.core.GameEngine;
import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.core.Grid;
import com.ndivhuwo.snakebattle.model.Direction;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;
import com.ndivhuwo.snakebattle.ui.GameWindow;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class SnakeBattle {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // 1️⃣ Grid
            Grid grid = new Grid(20, 20);

            // 2️⃣ Snakes
            List<Snake> snakes = new ArrayList<>();
            snakes.add(new Snake(new Position(5, 5)));
            snakes.add(new Snake(new Position(10, 10)));

            // 3️⃣ GameState
            GameState state = new GameState(grid, snakes);

            // 4️⃣ GameEngine
            GameEngine engine = new GameEngine(state, snakes);

            // 5️⃣ GameWindow
            new GameWindow(engine);
        });
    }
}
