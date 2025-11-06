package ObjectManager;

// ObjectManager hoặc BallManager
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.canvas.GraphicsContext;
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
        Iterator<Ball> it = balls.iterator();
        while (it.hasNext()) {
            Ball b = it.next();
            b.update(dt, paddle);

            if (b.isOffScreen(600)) {
                it.remove();
            }
        }
    }

    public void renderAll(GraphicsContext g) {
        for (Ball b : balls) {
            b.render(g);
        }
    }
}

