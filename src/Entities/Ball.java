package Entities.Ball;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import Entities.bricks.*;
import GameManager.*;
import Entities.Paddle.Paddle;


public class Ball extends MovableObject {
    private final int screenWidth, screenHeight;
    private int prevX, prevY;

    public Ball(int x, int y, int size, int screenHeight, int screenWidth, double speed) {
        super(x, y, size, size);
        this.speed = speed;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        setDirection(0.5,-1); //diem xuat phat bong cheo len phai
    }

    private void bounceOnWalls() {
        if (x <= 0) {
            x = 0;
            dirX = Math.abs(dirX);
        }
        if (x + width >= screenWidth) {
            x = screenWidth - width;
            dirX = -Math.abs(dirX);
        }
        if (y <= 0) {
            y = 0;
            dirY = Math.abs(dirY);
        }
    }

    public void checkCollision(Paddle paddle) {
        Rectangle ballRect = getBounds();
        Rectangle padRect = paddle.getBounds();
        if (!ballRect.intersects(padRect)) return;

        Rectangle over = ballRect.intersection(padRect);
        int penX = over.width;
        int penY = over.height;
        if (penX < penY) {
            if (x > prevX) {
                x -= penX;
            } else {
                x += penX;
            }
            dirX = -dirX;
        }
        else {
            if (y > prevY) {
                y -= penY;
                double hitRatio = (this.centerX() - paddle.getX()) / (double) paddle.getWidth() * 2.0 - 1.0;
                if (hitRatio > 1) hitRatio = 1;
                if (hitRatio < -1) hitRatio = -1;
                if (Math.abs(hitRatio) > 0.95) hitRatio = 0.95 * Math.signum(hitRatio);
                this.dirX = hitRatio;
                this.dirY = -Math.abs(dirY);
                normalizeDirection();
                // bảo đảm có thành phần dọc tối thiểu
                if (Math.abs(dirY) < 0.2) {
                    dirY = -0.2;
                    normalizeDirection();
                }
            } else {
                // Đi lên, đập mặt dưới (hiếm)
                y += penY;
                dirY = -dirY;
            }
        }
    }

    public boolean checkCollision(AbstractBrick bricks) {
        if (bricks.isDestroyed()) return false;
        Rectangle r1 = getBounds();
        Rectangle r2 = bricks.getBounds();
        if (!r1.intersects(r2)) return false;

        Rectangle over = r1.intersection(r2);
        int penX = over.width;
        int penY = over.height;

        if (penX < penY) {
            if (x > prevX) x -= penX; else x += penX;
            dirX = -dirX;
        } else {
            if (y > prevY) y -= penY; else y += penY;
            dirY = -dirY;
        }
        bricks.takeHit();
        return true;
    }

    public boolean fellOutBottom() {
        return y > screenHeight;
    }

    @Override
    public void update(double dt) {
        prevX = x;
        prevY = y;
        super.update(dt);
        bounceOnWalls();
    }
    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x,y,width,height);
    }

}