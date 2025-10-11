package Entities;

import java.awt.Graphics;

public abstract class MovableObject extends GameObject {
    protected double dirX;
    protected double dirY;
    protected double speed;

    public MovableObject(int x, int y, int width, int height) {
        super(x,y,width,height);
    }

    public void setDirection(double dx, double dy) {
        this.dirX = dx;
        this.dirY = dy;
        normalizeDirection();
    }

    public double getSpeed() {return speed;}
    public void setSpeed(double spped) {this.speed = speed;}

    protected void normalizeDirection() {
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        if (len == 0) {
            dirX = 0;
            dirY = 0;
            return;
        }
        dirX /= len; dirY /= len;
    }

    @Override
    public void update(double dt) {
        x += (int) Math.round(dirX * speed *dt);
        y += (int) Math.round(dirY * speed * dt);
    }
}
