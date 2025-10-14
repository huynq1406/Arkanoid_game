package Entities;

import java.awt.*;
import ObjectManager.*;

public class Ball extends MovableObject {
    private final int radius;

    private double speed;   // pixel/giây
    private double dirX;    // hướng chuẩn hóa [-1..1]
    private double dirY;
    private boolean launched = false;

    public Ball(int x, int y, int radius) {
        super(x - radius, y - radius, radius * 2, radius * 2);
        this.radius = radius;
        this.speed = 220;
        setDirection(0.5, -1); // HƯỚNG MẶC ĐỊNH THEO YÊU CẦU
    }

    public void setDirection(double newDirX, double newDirY) {
        double len = Math.hypot(newDirX, newDirY);
        if (len < 1e-6) return;
        dirX = newDirX / len;
        dirY = newDirY / len;
        this.dx = 2f * speed * dirX;
        this.dy = 2f * speed * dirY;
    }

    public double getDirX() { return dirX; }
    public double getDirY() { return dirY; }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        this.dx = 2f * speed * dirX;
        this.dy = 2f * speed * dirY;
    }

    public void resetToPaddle(Paddle paddle) {
        this.launched = false;
        this.x = paddle.getX() + paddle.getWidth()/2 - radius;
        this.y = paddle.getY() - 1 - radius*2;
        this.speed = 220;
        setDirection(0.5, -1); // HƯỚNG MẶC ĐỊNH KHI RESET
    }

    public void update(double dt, Paddle paddle) {
        if (!launched) {
            this.x = paddle.getX() + paddle.getWidth()/2 - radius;
            this.y = paddle.getY() - 1 - radius*2;
            return;
        }
        update(dt);
    }

    @Override
    public void update(double dt) {
        updatePosition(dt);
    }

    public void launch() {
        this.launched = true;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void bounceX() {
        setDirection(-dirX, dirY);
    }

    public void bounceY() { setDirection(dirX, -dirY); }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height);
    }
}