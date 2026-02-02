package com.ndivhuwo.snakebattle;

import javax.swing.*;
import java.awt.*;

public class SnakeBattle {

    public static void main(String[] args) {
        JPanel redpanel = new JPanel();
        JPanel bluepanel = new JPanel();
        JPanel pinkpanel = new JPanel();

        redpanel.setBackground(Color.red);
        redpanel.setBorder(BorderFactory.createLineBorder(Color.black));
        redpanel.setBounds(1, 1, 245,245);

        bluepanel.setBackground(Color.green);
        bluepanel.setBorder(BorderFactory.createLineBorder(Color.white));
        bluepanel.setBounds(0, 245, 245, 245);

        pinkpanel.setBackground(Color.pink);
        pinkpanel.setBorder(BorderFactory.createLineBorder(Color.black));
        pinkpanel.setBounds(0, 245, 245, 245);
    }
}
