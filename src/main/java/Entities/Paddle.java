package Entities;

import java.awt.Color;
import java.awt.Graphics;

public class Paddle extends MovableObject {
    private final int screenWidth;
    private final int floorY; //du co dinh paddle duoi mep

    public Paddle(int x,int y, int width, int height, int screenWidth, int floorY) {
        super(x,floorY - height, width, height);
        this.screenWidth = screenWidth;
        this.floorY = floorY;
        this.y = floorY - height;
    }

    public void setXClamped(int newX) {
        if (newX < 0) newX = 0;
        int maxX = screenWidth - width;
        if (newX > maxX) newX = maxX;
        this.x = newX;
        this.y = floorY - height;
    }

    public void setCenterX(int centerX) {
        setXClamped(centerX - width / 2);
    }

    @Override
    public void update(double dt) {
        this.y = floorY - height;
    }
    @Override
    public void render(Graphics g) {
        g.setColor(new Color(40,170,255));
        g.fillRoundRect(x,y,width,height,12,12);

        g.setColor(new Color(20, 80, 120));
        g.drawRoundRect(x, y, width, height, 12, 12);

        g.setColor(new Color(255, 255, 255, 40));
        g.fillRect(x + width/2 - 2, y + 3, 4, height - 6);
    }
}
