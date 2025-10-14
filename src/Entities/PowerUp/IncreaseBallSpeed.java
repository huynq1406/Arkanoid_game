package Entities.PowerUp;

import Entities.Ball;

public class IncreaseBallSpeed extends PowerUp implements IPowerUp {
    private Ball ball;
    public IncreaseBallSpeed(float x, float y) {
        super(x, y, 3);
    }

    @Override
    public void activate() {
        if (active) {
            while (timer < duration) {
                ball.setSpeed(ball.getSpeed() * 2);
                active = true;
            }
        }
    }

    public void deactivate() {
        if (timer >= duration) {
            active = false;
        }
    }

    @Override
    public void update() {

    }
}
