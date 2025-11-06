package Entities.PowerUp;

import Entities.Ball;

public class IncreaseBallSpeed extends PowerUp  {
    private final Ball ball;
    private final double speedMultiplier = 1.5;

    public IncreaseBallSpeed(double x, double y, Ball ball) {
        super(x, y, 3, "/ibsPW.png");
        this.ball = ball;
        this.duration = 180; // e.g. 3 seconds at 60fps
    }

    @Override
    public void activate() {
        if (!active && ball != null) {
            ball.setSpeed(ball.getSpeed() * speedMultiplier);
            active = true;
            timer = 0;
        }
    }

    @Override
    public void deactivate() {
        if (active && ball != null) {
            ball.setSpeed(ball.getSpeed() / speedMultiplier);
            active = false;
        }
    }
}

