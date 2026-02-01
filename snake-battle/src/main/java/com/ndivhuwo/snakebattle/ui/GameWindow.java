package com.ndivhuwo.snakebattle.ui;

import javax.swing.JFrame;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Snake Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and add the panel
        GamePanel panel = new GamePanel();
        add(panel);

        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }
}
