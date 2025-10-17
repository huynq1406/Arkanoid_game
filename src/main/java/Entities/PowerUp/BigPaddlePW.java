package Entities.PowerUp;

import Entities.Paddle;

public class BigPaddlePW extends PowerUp implements IPowerUp {
    protected Paddle pd;

    public BigPaddlePW(float x, float y) {
        super(x, y, 1);
        this.duration = 20; // số frame tồn tại
        this.timer = 0;
        this.active = true;
    }

    @Override
    public void activate() {
        // logic: phóng to paddle
        if (active) {
            while (timer < duration) {
              pd.setWidth(pd.getWidth() * 2);
            }
        }
    }

    @Override
    public void deactivate() {
        if (timer < duration) {
            active = false;
        }
    }

    @Override
    public void update() {
        if (active) {
            timer++;
            if (timer > duration) {
                active = false;
                // reset paddle về kích thước ban đầu
            }
        }
        fall(); // cho power-up rơi xuống
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
