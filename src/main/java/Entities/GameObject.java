package Entities;

import java.awt.*;
import java.awt.Graphics; //import thu vien de ve moi thu
import java.awt.Rectangle; //import thu vien de ve hinh chu nhat

/**
 * Lop Object cho toan bo doi tuong
 * Lop ke thua: MovableObject, Brick
 */
public abstract class GameObject {
    protected int x, y;
    protected int width, height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x,y,width,height);
    }

    public int centerX() {return x + width / 2;}
    public int centerY() {return y + width / 2;}

    public abstract void update(double dt);
    public abstract void render(Graphics g);

    public int getX() {return x;}
    public void setX(int x) {this.x = x;}

    public int getY() {return y;}
    public void setY(int y) {this.y = y;}

    public int getWidth() {return width;}
    public void setWidth(int width) {this.width = width;}

    public int getHeight() {return height;}
    public void setHeight(int height) {this.height = height;}
}
