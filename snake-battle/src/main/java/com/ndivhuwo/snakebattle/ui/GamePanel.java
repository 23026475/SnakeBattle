package com.ndivhuwo.snakebattle.ui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class GamePanel extends JPanel {

    public GamePanel() {
        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Nothing drawn yet — Phase 1 complete
    }
}
