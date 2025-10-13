package Entities;

import java.awt.Color;
import java.awt.Graphics;
import ObjectManager.MovableObject;
import Entities.PowerUp.*;

public class Paddle extends MovableObject {
    protected BigPaddlePW bp = new BigPaddlePW(21,23);

    public Paddle(int x, int y, int width, int height) {
        super(x,y,width,height);
        this.dx = 0;
        this.dy = 0;
    }
    public void setCenterX(int mouseX) {
        this.x = mouseX - width / 2;
    }

    public void clamp(int minX, int maxX) {
        if (x < minX) x = minX;
        if (x + width > maxX) x = maxX - width;
    }

    @Override
    public void update(double dt){
        if (bp.activate())
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
