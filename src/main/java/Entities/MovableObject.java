package Entities;

import java.awt.Graphics;

public abstract class MovableObject extends GameObject {
    protected int dx;
    protected int dy;

    public MovableObject(int x, int y, int width, int height, int dx, int dy) {
        super(x,y,width,height);
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public abstract void update();
    @Override
    public abstract void render(Graphics g);

    public int getDy() {return dy;}
    public void setDy(int dy) {this.dy = dy;}

    public int getDx() {return dx;}
    public void setDx(int dx) {this.dx = dx;}

    public void reverseX() {
        this.dx = -dx;
    }
    public void reverseY() {
        this.dy = -dy;
    }
}
