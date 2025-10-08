package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import bricks.*;


public class Ball extends GameObject {
    private double speed;
    private double dirX;
    private double dirY;
    private final int screenWidth;
    private final int screenHeight;
    private Paddle paddleRef; //tham chieu den paddle de reset Ball len
    private int prevX, prevY;

    public Ball(int x, int y, int size, double speed, double dirX, double dirY, int screenWidth, int screenHeight) {
        super(x, y, size, size);
        this.speed = speed;
        this.dirX = dirX;
        this.dirY = dirY;
        normalizeDirection();
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    /**
     * Chuan hoa vector do dai.
     */
    private void normalizeDirection() {
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        if (length != 0) {
            dirX /= length;
            dirY /= length;
        }
    }

    public void attachToPaddle(Paddle paddle) {
        this.paddleRef = paddle;
    }

    /**
     * Phan xa bong nguoc lai theo huong Y
     */
    public void bounceY() {
        dirY = -dirY;
    }

    /**
     * Bat theo goc (dua vao diem cham)
     * @param newDirX vi tri moi
     */
    public void bounceWithAngle(double newDirX) {
        if (newDirX > 1) newDirX = 1;
        if (newDirX < -1) newDirX = -1;
        this.dirX = newDirX;
        this.dirY = -Math.abs(dirY);
        normalizeDirection();
    }

    /**
     * Khi bong roi, thi dat lai bong len paddle.
     * @param paddle thanh ngang do bong
     */
    public void resetOnPaddle(Paddle paddle) {
        if (paddle == null) return;
        this.x = paddle.getX() + paddle.getWidth() / 2 - this.width / 2;
        this.y = paddle.getY() - this.height - 2;
        this.dirX = 0;
        this.dirY = -1;
        this.speed = 0;
        normalizeDirection();
    }

    /**
     * Check va cham voi thanh ngang choi bong
     * @param paddle thanh ngang player dieu khien
     */
    public void checkCollision(Paddle paddle) {
        Rectangle ballRect = this.getBounds();
        Rectangle padRect = paddle.getBounds();
        if(!ballRect.intersects(padRect)) return;

        Rectangle overLap = ballRect.intersection(padRect);
        int penX = overLap.width;
        int penY = overLap.height; //lay khoang ma ball va cham, lun vao

        if (penX < penY) {
            if (x > prevX) {
                x -= penX;
            } else {
                x += penX;
            }
            dirX = -dirX;
        }
        else {
            if(y > prevY) {
                y -= penY;
                double hitRatio = (this.centerX() - paddle.getX()) / (double) paddle.getWidth() * 2.0 - 1.0;
                if (hitRatio > 1) hitRatio = 1;
                if (hitRatio < -1) hitRatio = 1;
                this.dirX = hitRatio;
                this.dirY = -Math.abs(dirY);
                normalizeDirection();
            }
            else {
                y += penY;
                dirY = -dirY;
            }
        }
    }

    public void checkCollision(List<AbstractBrick> bricks) {
        if (bricks == null) return;
        for (AbstractBrick brick : bricks) {
            if (brick == null || brick.isDestroyed()) continue;
            Rectangle ballRect = this.getBounds();
            Rectangle brickRect = brick.getBounds();
            if (!ballRect.intersects(brickRect)) continue;

            Rectangle overlap = ballRect.intersection(brickRect);
            int penX = overlap.width;
            int penY = overlap.height;

            if (penX < penY) {
                // va chạm ngang
                if (x > prevX) x -= penX; else x += penX;
                dirX = -dirX;
            } else {
                // va chạm dọc
                if (y > prevY) y -= penY; else y += penY;
                dirY = -dirY;
            }

            brick.takeHit();
            break;
        }
    }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public void setDirection(double nx, double ny) {
        this.dirX = nx;
        this.dirY = ny;
        normalizeDirection();
    }

    public double getDirX() { return dirX; }
    public double getDirY() { return dirY; }

    /**
     * Cap nhat vi tri cua ball va khi phan xa voi tuong.
     */
    @Override
    public void update() {
        prevX = x;
        prevY = y;
        x += dirX * speed;
        y += dirY * speed;
        if (x <= 0) { //cham trai/phai
            x = 0;
            dirX = Math.abs(dirX);
        }
        else if (x + width >= screenWidth) {
            x = screenWidth - width;
            dirX = -Math.abs(dirX);
        }
        if (y <= 0) { //cham tran
            y = 0;
            dirY = Math.abs(dirY);
        }
        if (y > screenHeight) { //khi bong roi ra khoi man hinh, khi co gameover hoac updat so luot choi thi fix lai sau
            resetOnPaddle(paddleRef);
        }
    }

    @Override
    public void render (Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x,y,width,height);
    }

}
