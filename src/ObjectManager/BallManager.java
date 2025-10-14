package ObjectManager;

// ObjectManager hoặc BallManager
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;
import Entities.Ball;
import Entities.Paddle;


public class BallManager {
    private List<Ball> balls = new ArrayList<>();

    public void addBall(Ball b) {
        balls.add(b);
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void updateAll(double dt, Paddle paddle) {
        for (Ball b : balls) {
            b.update(dt, paddle);
        }
    }

    public void renderAll(Graphics g) {
        for (Ball b : balls) {
            b.render(g);
        }
    }
}

