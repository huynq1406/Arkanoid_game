package Levels;

import java.util.ArrayList;
import java.util.List;
import Entities.PowerUp.*;
import Entities.bricks.*;
import Entities.Ball;
import Entities.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;

public class BaseLevel {
    private List<AbstractBrick> bricks;
    private List<IPowerUp> powerUps;
    private Ball ball;
    private Paddle paddle;
    private Canvas canvas = new Canvas(400, 300);

    public BaseLevel(){}
    public BaseLevel(GraphicsContext gc, int levelNum) {
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        loadBricks(levelNum);
        paddle = new Paddle(350, 550, 100, 15);
        ball = new Ball(400, 530, 8);
    }

    public void loadBricks(int levelNum) {
//        for (int row = 0; row < 3; row++) {
//            for (int col = 0; col < 5; col++) {
//                int x = 60 + row * 60; // int x = startX + col * (brickWidth + hSpacing);
//                int y = 60 + col * 30; // int y = startY + row * (brickHeight + vSpacing);
//                NormalBricks brick = new NormalBricks(x, y, 20, 30);
//                bricks.add(brick);
//            }
//        }
        if (bricks == null) {
            bricks = new ArrayList<>();
        }
        bricks.clear();
        int rows = 3, cols = 5;
        int brickW = 60, brickH = 30;
        int startX = 100, startY = 80;
        int hGap = 8, vGap = 6;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + c * (brickW + hGap);
                int y = startY + r * (brickH + vGap);
                bricks.add(new NormalBricks(x, y, brickW, brickH));
                }
        }
    }

    public void update() {
        ball.update(0.1f); //chưa biết điền gì
        paddle.update(0.1f); //chưa biêt điền gì
        checkCollisions();
        powerUps.forEach(IPowerUp::update);
    }

    public void render() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        bricks.forEach(b -> b.render(g));
        ball.render(g);
        paddle.render(g);
        // vẽ các power-up nếu có hiệu ứng bay xuống
    }

    private void checkCollisions() {
    }

    public boolean isCompleted() {
        return bricks.stream().allMatch(AbstractBrick::isDestroyed);
    }

    public boolean isFailed() {
        return ball.getY() > 600; // rơi khỏi màn hình
    }
}