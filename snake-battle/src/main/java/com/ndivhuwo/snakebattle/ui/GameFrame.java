package com.ndivhuwo.snakebattle.ui;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame(){
        this.add(new GamePanel());
        this.setTitle("SnakeBattle");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
