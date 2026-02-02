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
        g.setColor(Color.white);
        g.fillOval(foodX,foodY,UnitSize,UnitSize);

        for(int i = 0; i < bodyParts; i++){
            if(i == 0){
                g.setColor(Color.MAGENTA);
                g.fillOval(x[i],y[i],UnitSize,UnitSize);
            }
            else{
                g.setColor(Color.MAGENTA);
                g.fillRect(x[i],y[i],UnitSize,UnitSize);
            }
        }
    }
    public void newFood(){
        foodX = random.nextInt(ScreenWidth/UnitSize)*UnitSize;
        foodY = random.nextInt(ScreenHeight/UnitSize)*UnitSize;
    }
    public void move(){
        for(int i =bodyParts-1; i >0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case 'U': y[0] = y[0] - UnitSize;
                break;
            case 'D': y[0] = y[0] + UnitSize;
                break;
            case 'L': x[0] = x[0] - UnitSize;
                break;
            case 'R': x[0] = x[0] + UnitSize;
                break;
        }
    }
    public void checkFood(){

    }
    public void checkCollisions(){
        //checks head body collision
        for(int i = bodyParts; i >0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //check if head touches left and top border
        if(x[0]<0){
            running = false;
        }
        if(y[0]<0){
            running = false;
        }
        //check if head touches right and bottom border
        if(x[0]>ScreenWidth){
            running = false;
        }
        if(y[0]>ScreenHeight){
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameover(){

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

        }
    }
}
