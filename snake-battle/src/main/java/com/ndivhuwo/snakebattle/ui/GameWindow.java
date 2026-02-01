package com.ndivhuwo.snakebattle.ui;

import com.ndivhuwo.snakebattle.core.GameEngine;
import com.ndivhuwo.snakebattle.core.GameState;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main game window.
 * Creates a JFrame, adds the GamePanel, and updates it via a timer.
 */
public class GameWindow extends JFrame {

    public GameWindow(GameEngine engine) {
        setTitle("Snake Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pass the GameState to the panel
        GamePanel panel = new GamePanel(engine.getGameState());
        add(panel);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

