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
        setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // Title
        JLabel title = new JLabel("SNAKE GAME", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.GREEN);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        add(title, gbc);

        // Human Play Button
        JButton humanButton = createStyledButton("HUMAN PLAY", new Color(70, 130, 180));
        humanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.startGame("HUMAN");
            }
        });

        // DFS Visualization Button
        JButton dfsButton = createStyledButton("DFS VISUALIZATION", new Color(220, 120, 0));
        dfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.startGame("DFS");
            }
        });

        // Instructions
        JTextArea instructions = new JTextArea(
                "Human Mode:\n" +
                        "• Use arrow keys to control the snake\n" +
                        "• Eat red apples to grow\n" +
                        "• Avoid walls and yourself\n\n" +
                        "DFS Mode:\n" +
                        "• Watch DFS algorithm find paths\n" +
                        "• Blue cells = visited\n" +
                        "• Green cells = current path\n" +
                        "• Red cells = backtracking"
        );
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 12));
        instructions.setForeground(Color.WHITE);
        instructions.setBackground(new Color(50, 50, 50));
        instructions.setEditable(false);
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(instructions);
        scrollPane.setPreferredSize(new Dimension(400, 150));

        // Add components
        add(humanButton, gbc);
        add(dfsButton, gbc);
        add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        add(scrollPane, gbc);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }
}