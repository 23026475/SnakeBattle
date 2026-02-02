package com.ndivhuwo.snakebattle.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int ScreenWidth = 600;
    static final int ScreenHeight = 600;
    static final int UnitSize = 25;
    static final int GameUnits = (ScreenWidth*ScreenHeight)/UnitSize;
    static final int Delay = 75;
    final int x[] = new int[GameUnits];
    final int y[] = new int[GameUnits];
    int bodyParts = 6;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        timer = new Timer(Delay,this);
        startGame();
    }

    public void startGame(){
        newFood();
        running = true;

        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        for(int i =0; i < ScreenHeight/UnitSize; i++){
            g.drawLine(i*UnitSize,0,i*UnitSize,ScreenHeight);
            g.drawLine(0,i*UnitSize,ScreenWidth,i*UnitSize);

        }
    }
    public void newFood(){

    }
    public void move(){

    }
    public void checkFood(){

    }
    public void checkCollisions(){

    }
    public void gameover(){

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public class MyKeyAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

        }
    }
}
