package Manage;

import Entities.Ball;
import Entities.Paddle;
import bricks.AbstractBrick;
import bricks.NormalBricks;
import bricks.StrongBricks;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {

    public final int width;
    public final int height;

    private final Paddle paddle;
    private final Ball ball;
    private final List<AbstractBrick> bricks = new ArrayList<>();

    // input flags (GamePanel sẽ set)
    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean spacePressed = false;

    public GameManager(int width, int height) {
        this.width = width;
        this.height = height;

        // tạo paddle & ball
        paddle = new Paddle(width / 2 - 50, height - 40, 100, 14, 8, width);
        ball   = new Ball(0, 0, 14, 5, 0.7, -1, width, height);
        ball.attachToPaddle(paddle);
        ball.resetOnPaddle(paddle); // bóng nằm trên paddle lúc bắt đầu

        // tạo level đơn giản dạng grid
        buildLevel();
    }

    private void buildLevel() {
        int cols = 10;
        int rows = 5;
        int brickW = 64;
        int brickH = 24;
        int startX = (width - cols * brickW) / 2;
        int startY = 80;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + c * brickW;
                int y = startY + r * brickH;

                // ví dụ: hàng chẵn normal, hàng lẻ strong
                if (r % 2 == 0) {
                    bricks.add(new NormalBricks(x, y, brickW, brickH));
                } else {
                    bricks.add(new StrongBricks(x, y, brickW, brickH));
                }
            }
        }
    }

    /** cập nhật một frame */
    public void update() {
        // input → paddle
        if (leftPressed)  paddle.moveLeft();
        if (rightPressed) paddle.moveRight();

        // thả bóng khi nhấn Space và bóng đang đứng yên
        if (spacePressed && ball.getSpeed() == 0) {
            ball.setSpeed(5);
            ball.setDirection(0, -1);
        }

        ball.update();
        ball.checkCollision(paddle);
        ball.checkCollision(bricks);

        // xoá gạch đã vỡ
        Iterator<AbstractBrick> it = bricks.iterator();
        while (it.hasNext()) {
            if (it.next().isDestroyed()) it.remove();
        }
    }

    /** vẽ toàn cảnh */
    public void render(Graphics g) {
        // nền đen
        g.setColor(Color.PINK);
        g.fillRect(0, 0, width, height);

        // vẽ gạch
        for (AbstractBrick b : bricks) {
            b.render(g);
        }

        // vẽ paddle & ball
        paddle.render(g);
        ball.render(g);

        // HUD đơn giản
        g.setColor(Color.WHITE);
        g.drawString("Bricks: " + bricks.size(), 10, 20);
        if (ball.getSpeed() == 0) {
            g.drawString("Press SPACE to launch", width/2 - 70, height - 10);
        }
    }
}
