package com.ndivhuwo.snakebattle.ui;

import com.ndivhuwo.snakebattle.core.GameState;
import com.ndivhuwo.snakebattle.model.Position;
import com.ndivhuwo.snakebattle.model.Snake;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Responsible ONLY for rendering the game state.
 * No game logic lives here.
 */
public class GamePanel extends JPanel {

    private final GameState state;
    private static final int CELL_SIZE = 25;

    public GamePanel(GameState state) {
        this.state = state;

        int width = state.grid().width() * CELL_SIZE;
        int height = state.grid().height() * CELL_SIZE;
        setPreferredSize(new Dimension(width, height));

        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGrid(g);
        drawFood(g);
        drawSnakes(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);

        for (int x = 0; x <= state.grid().width(); x++) {
            g.drawLine(
                    x * CELL_SIZE,
                    0,
                    x * CELL_SIZE,
                    state.grid().height() * CELL_SIZE
            );
        }

        for (int y = 0; y <= state.grid().height(); y++) {
            g.drawLine(
                    0,
                    y * CELL_SIZE,
                    state.grid().width() * CELL_SIZE,
                    y * CELL_SIZE
            );
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);

        for (Position p : state.foodPositions()) {
            g.fillOval(
                    p.x() * CELL_SIZE,
                    p.y() * CELL_SIZE,
                    CELL_SIZE,
                    CELL_SIZE
            );
        }
    }

    private void drawSnakes(Graphics g) {
        for (Snake snake : state.snakes()) {
            if (!snake.isAlive()) continue;

            g.setColor(Color.GREEN);

            for (Position p : snake.body()) {
                g.fillRect(
                        p.x() * CELL_SIZE,
                        p.y() * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE
                );
            }
        }
    }
}
