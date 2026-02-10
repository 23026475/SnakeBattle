package com.ndivhuwo.snakebattle;

import javax.swing.JFrame;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        initUI();
    }

    private void initUI() {
        add(new GamePanel());

        setTitle("Classic Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            JFrame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}