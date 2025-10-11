package Entities.PowerUp;

public class BigPaddlePW extends PowerUp implements IPowerUp {

    public BigPaddlePW(float x, float y) {
        super(x, y, 1);
        this.duration = 20; // số frame tồn tại
        this.timer = 0;
        this.active = false;
    }

    @Override
    public void activate() {
        // logic: phóng to paddle
        active = true;
        timer = 0;
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
