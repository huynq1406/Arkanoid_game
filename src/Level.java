import java.util.ArrayList;
import PowerUp.IPowerUp;
import bricks.NormalBricks;

public class Level {
    private List<NormalBricks> brick;
    private List<IPowerUp> powerUps;
    private Ball ball;
    private Paddle paddle;

    public Level(int levelNum) {
        loadBricks(levelNum);
        paddle = new Paddle(350, 550, 100, 15);
        ball = new Ball(400, 530, 8, 5, -5);
        powerUps = new ArrayList<>();
    }

    public void update() {
        ball.update();
        paddle.update();
        checkCollisions();
        powerUps.forEach(IPowerUp::update);
    }

    public void render(Graphics2D g) {
        brick.forEach(b -> b.render(g));
        ball.render(g);
        paddle.render(g);
        // vẽ các power-up nếu có hiệu ứng bay xuống
    }

    private void checkCollisions() {
        // Va chạm bóng - gạch, bóng - paddle, paddle - powerup
    }

    public boolean isCompleted() {
        return bricks.stream().allMatch(Brick::isDestroyed);
    }

    public boolean isFailed() {
        return ball.getY() > 600; // rơi khỏi màn hình
    }
}

