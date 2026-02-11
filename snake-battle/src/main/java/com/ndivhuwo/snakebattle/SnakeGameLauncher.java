package com.ndivhuwo.snakebattle;

public class SnakeGameLauncher {
    public static void main(String[] args) {
        // Use SwingUtilities to ensure EDT
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SnakeGame().setVisible(true);
            }
        });
    }
}