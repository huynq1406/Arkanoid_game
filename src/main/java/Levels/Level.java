package Levels;

import java.util.ArrayList;
import java.util.List;
import Entities.PowerUp.*;
import Entities.bricks.*;
import Entities.Ball;
import Entities.Paddle;
import java.awt.Graphics2D;

public class Level {
    private List<AbstractBrick> bricks;
    private List<IPowerUp> powerUps;
    private Ball ball;
    private Paddle paddle;

    public Level(){}
    public Level(int levelNum) {
        loadBricks(levelNum);
        paddle = new Paddle(350, 550, 100, 15);
        ball = new Ball(400, 530, 8);
        powerUps = new ArrayList<>();
    }

    public void loadBricks(int levelNum) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                int x = 60 + row * 60; // int x = startX + col * (brickWidth + hSpacing);
                int y = 60 + col * 30; // int y = startY + row * (brickHeight + vSpacing);
                NormalBricks brick = new NormalBricks(x, y, 20, 30);
                bricks.add(brick);
            }
        }
    }

    public void update() {
        ball.update(0.1f); //chưa biết điền gì
        paddle.update(0.1f); //chưa biêt điền gì
        checkCollisions();
        powerUps.forEach(IPowerUp::update);
    }

    public void render(Graphics2D g) {
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