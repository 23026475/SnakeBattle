package com.ndivhuwo.snakebattle;

import javax.swing.JFrame;

public class SnakeBattle extends JFrame {

    public SnakeBattle() {
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
            JFrame game = new SnakeBattle();
            game.setVisible(true);
        });
    }
}