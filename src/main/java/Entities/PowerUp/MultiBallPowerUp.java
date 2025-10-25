package Entities.PowerUp;

import Entities.Ball;

import java.util.List;

public class MultiBallPowerUp extends PowerUp implements IPowerUp {
    private final List<Ball> balls;

    public MultiBallPowerUp(float x, float y, List<Ball> balls) {
        super(x, y, 3, "/mbsPW.png");
        this.balls = balls;
        this.active = false;
    }

    @Override
    public void activate() {
        if (balls == null || balls.isEmpty()) return;
        Ball original = balls.get(0);
        Ball newBall = new Ball((int) original.getX(), (int) original.getY(), original.getWidth() / 2);
        newBall.setSpeed(original.getSpeed());
        newBall.setDirection(-original.getDirX(), original.getDirY());
        newBall.launch();
        balls.add(newBall);
        active = true;
    }

    @Override
    public void deactivate() {
        active = false;
    }

    @Override
    public void update() {
        fall();
    }
}

