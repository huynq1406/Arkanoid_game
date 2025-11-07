package Entities.PowerUp;

import Entities.Paddle;

public class BigPaddlePW extends PowerUp {
    private final Paddle paddle;
    private final double scale = 1.8;
    private double originalWidth = -1;

    public BigPaddlePW(double x, double y, Paddle paddle) {
        super(x, y, 1, "/bpsPW.png");
        this.paddle = paddle;
        this.duration = 300;
    }

    @Override
    public void activate() {
        if (!active && paddle != null) {
            originalWidth = paddle.getWidth();
            paddle.setWidth((int) (originalWidth * scale));
            active = true;
            timer = 0;
        }
    }

    @Override
    public void deactivate() {
        if (active && paddle != null && originalWidth > 0) {
            paddle.setWidth((int) originalWidth);
            active = false;
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
