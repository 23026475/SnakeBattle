package com.ndivhuwo.snakebattle;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class SnakeGame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private GameMenu gameMenu;

    public SnakeGame() {
        initUI();
    }

    private void initUI() {
        setTitle("Snake Game with DFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gameMenu = new GameMenu(this);
        gamePanel = new GamePanel(this);

        mainPanel.add(gameMenu, "MENU");
        mainPanel.add(gamePanel, "GAME");

        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
    }

    public void startGame(String mode) {
        // Validate mode
        if (!Arrays.asList("HUMAN", "DFS", "BFS", "ASTAR", "BATTLE").contains(mode)) {
            mode = "HUMAN";
        }

        gamePanel.setGameMode(mode);
        gamePanel.startGame();
        cardLayout.show(mainPanel, "GAME");
        gamePanel.requestFocusInWindow();
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    public static void main(String[] args) {
        // Properly start on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SnakeGame game = new SnakeGame();
                game.setVisible(true);
            }
        });
    }
}