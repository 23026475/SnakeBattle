package com.ndivhuwo.snakebattle.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    enum GameState {
        START,
        RUNNING,
        GAME_OVER
    }

    GameState gameState = GameState.START;

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

    }
    public void gameStarter(Graphics g){
        g.setColor(Color.MAGENTA);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(
                "Snake Battle",
                (getWidth() - fm.stringWidth("Snake Battle")) / 2,
                200
        );

        // Subtitle
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.setColor(Color.WHITE);
        FontMetrics fm2 = g.getFontMetrics();
        g.drawString(
                "Press ENTER to Start",
                (getWidth() - fm2.stringWidth("Press ENTER to Start")) / 2,
                300
        );
    }

    public void startGame() {
        newFood();
        running = true;
        gameState = GameState.RUNNING;
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        switch (gameState) {
            case START:
                gameStarter(g);
                break;

            case RUNNING:
                drawGame(g);
                break;

            case GAME_OVER:
                gameover(g);
                break;
        }
    }
    public void drawGame(Graphics g){

           /* for(int i =0; i < ScreenHeight/UnitSize; i++){
                g.drawLine(i*UnitSize,0,i*UnitSize,ScreenHeight);
                g.drawLine(0,i*UnitSize,ScreenWidth,i*UnitSize);
            }*/
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
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEaten, (ScreenWidth - fm.stringWidth("Score: "+foodEaten))/2,g.getFont().getSize());


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
        if((x[0] == foodX) && (y[0] == foodY)){
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }
    public void checkCollisions(){
        //checks head body collision
        for(int i = bodyParts-1; i >0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
                break;
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
        if (x[0] < 0 || x[0] >= ScreenWidth) {
            running = false;
        }

        if (y[0] < 0 || y[0] >= ScreenHeight) {
            running = false;
        }
        if(!running){
            timer.stop();
            System.out.println("GAME OVER STATE");
            gameState = GameState.GAME_OVER;
        }
    }
    public void gameover(Graphics g){
        //gameover score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics fme = getFontMetrics(g.getFont());
        g.drawString("Score: "+foodEaten, (ScreenWidth - fme.stringWidth("Score: "+foodEaten))/2,g.getFont().getSize());

        //game over text message
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("Game Over", (ScreenWidth - fm.stringWidth("Game Over"))/2,ScreenHeight/2);

        //reset
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics fm2 = getFontMetrics(g.getFont());
        g.drawString(
                "Press Enter to Replay",
                (ScreenWidth - fm2.stringWidth("Press Enter to Replay")) / 2,
                ScreenHeight / 2 + 50
        );

    }
    public void resetGame() {
        bodyParts = 6;
        foodEaten = 0;
        direction = 'R';

        int startX = 0;
        int startY = 0;

        for (int i = 0; i < bodyParts; i++) {
            x[i] = startX - (i * UnitSize);
            y[i] = startY;
        }

        newFood();

        running = true;
        gameState = GameState.RUNNING;

        timer.restart();
        requestFocusInWindow();
        repaint();
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

            if (gameState == GameState.START && e.getKeyCode() == KeyEvent.VK_ENTER) {
                System.out.println("Key pressed: " + e.getKeyCode() + " | State: " + gameState);
                startGame();
                return;
            }

            if (gameState == GameState.GAME_OVER && e.getKeyCode() == KeyEvent.VK_ENTER) {
                resetGame();
                System.out.println("Key pressed: " + e.getKeyCode() + " | State: " + gameState);

                return;
            }

            if (gameState != GameState.RUNNING) return;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
            }
        }

    }
}
