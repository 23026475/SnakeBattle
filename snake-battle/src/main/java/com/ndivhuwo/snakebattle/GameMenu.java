package com.ndivhuwo.snakebattle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JPanel {

    private SnakeGame parentFrame;

    public GameMenu(SnakeGame parent) {
        this.parentFrame = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // Title
        JLabel title = new JLabel("SNAKE PATHFINDING", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.CYAN);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Algorithm Visualization", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.BOLD, 24));
        subtitle.setForeground(Color.WHITE);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        add(subtitle, gbc);

        // Human Play Button
        JButton humanButton = createStyledButton("🎮 HUMAN PLAY", new Color(70, 130, 180));
        humanButton.addActionListener(e -> parentFrame.startGame("HUMAN"));

        // DFS Button
        JButton dfsButton = createStyledButton("🔍 DEPTH-FIRST SEARCH (DFS)", new Color(220, 120, 0));
        dfsButton.addActionListener(e -> parentFrame.startGame("DFS"));

        // BFS Button
        JButton bfsButton = createStyledButton("📊 BREADTH-FIRST SEARCH (BFS)", new Color(120, 200, 80));
        bfsButton.addActionListener(e -> parentFrame.startGame("BFS"));

        // A* Button
        JButton aStarButton = createStyledButton("⭐ A* SEARCH (A-STAR)", new Color(180, 80, 220));
        aStarButton.addActionListener(e -> parentFrame.startGame("ASTAR"));

        // Snake Battle Button
        JButton battleButton = createStyledButton("🐍 SNAKE BATTLE", new Color(255, 50, 50));
        battleButton.addActionListener(e -> parentFrame.startGame("BATTLE"));

        // Instructions Panel
        JTextArea instructions = new JTextArea(
                "Algorithm Comparisons:\n" +
                        "─────────────────────────────\n" +
                        "DFS (Depth-First Search):\n" +
                        "• Explores deep first, may backtrack\n" +
                        "• Blue: Visited cells\n" +
                        "• Green: Current path\n" +
                        "\n" +
                        "BFS (Breadth-First Search):\n" +
                        "• Explores level by level\n" +
                        "• Finds shortest path\n" +
                        "• Yellow: Visited cells\n" +
                        "\n" +
                        "A* Search:\n" +
                        "• Uses heuristic + cost\n" +
                        "• Most efficient pathfinding\n" +
                        "• Purple: Visited cells\n" +
                        "\n" +
                        "Controls:\n" +
                        "• Arrow Keys: Move (Human mode)\n" +
                        "• SPACE: Restart\n" +
                        "• ESC: Main Menu"
        );
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 12));
        instructions.setForeground(Color.WHITE);
        instructions.setBackground(new Color(40, 40, 50));
        instructions.setEditable(false);
        instructions.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 150), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(instructions);
        scrollPane.setPreferredSize(new Dimension(450, 250));

        // Add components
        add(humanButton, gbc);
        add(dfsButton, gbc);
        add(bfsButton, gbc);
        add(aStarButton, gbc);
        add(battleButton, gbc);
        add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        add(scrollPane, gbc);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.YELLOW, 2),
                        BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
        });

        return button;
    }
}