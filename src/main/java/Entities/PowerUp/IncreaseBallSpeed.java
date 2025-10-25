package Entities.PowerUp;

import Entities.Ball;

public class IncreaseBallSpeed extends PowerUp implements IPowerUp {
    private final Ball ball;
    private final double speedMultiplier = 1.5;

    public IncreaseBallSpeed(float x, float y, Ball ball) {
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

    @Override
    public void update() {
        if (active) {
            timer++;
            if (timer >= duration) deactivate();
        } else {
            fall();
        }
    }
}

