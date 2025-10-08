package Entities;

import java.awt.Color;
import java.awt.Graphics;

public class Paddle extends MovableObject {
    private int speed;
    private String currentPowerUp;
    private int screenWidth; //chieu rong man hinh de gioi han di chuyen cua paddle

    public Paddle(int x,int y, int width, int height, int spped, int screenWidth) {
        super(x,y,width,height,0,0);
        this.screenWidth = screenWidth;
        this.speed = speed;
        this.currentPowerUp = "None";
    }

    public void moveRight() {
        x += speed;
        if (x + width > screenWidth) x = screenWidth - width;
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
    }

    public void applyPowerUp(String powerUpType) {
        this.currentPowerUp = powerUpType;
    }

    @Override
    public void update() {}
    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x,y,width,height);
    }

    public int getSpeed() {return speed;}
    public void setSpeed(int speed) {this.speed = speed;}

    public String getCurrentPowerUp() {return currentPowerUp;}
    public void setCurrentPowerUp(String currentPowerUp) {this.currentPowerUp = currentPowerUp;
    }

    public int getScreenWidth() {return screenWidth;}
}
