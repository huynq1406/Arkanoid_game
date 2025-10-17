package Entities.PowerUp;

import Entities.Ball;
import Entities.Paddle;
import java.util.List;
import java.awt.Graphics;
import java.awt.Color;

import static java.awt.Color.CYAN;

public class MultiBallPowerUp extends PowerUp implements IPowerUp {
    private List<Ball> balls; // tham chiếu đến danh sách bóng hiện tại

    public MultiBallPowerUp(float x, float y, List<Ball> balls) {
        super(x, y, 3); // 3 = loại multiball
        this.balls = balls;
        this.active = false;
    }

    @Override
    public void activate() {
        if (balls.isEmpty()) return;

        // Chọn bóng đầu tiên làm gốc
        Ball original = balls.get(0);
        Ball newBall = new Ball(
                (int) original.getX(),
                (int) original.getY(),
                original.getWidth() / 2
        );

        // Sao chép tốc độ, nhưng đổi hướng chút
        newBall.setSpeed(original.getSpeed());
        newBall.setDirection(-original.getDirX(), original.getDirY());
        newBall.launch();

        balls.add(newBall);
        active = true;
    }

    @Override
    public void deactivate() {
        // MultiBall là hiệu ứng tức thời, không cần deactivate.
        active = false;
    }

    @Override
    public void update() {
        fall(); // rơi xuống như vật phẩm bình thường
    }
}
