package Entities.PowerUp;

public class ExtraLifePowerUp extends PowerUp implements IPowerUp {
    public ExtraLifePowerUp(float x, float y) {
        super(x, y, 4);
    }

    @Override
    public void activate() {
        System.out.println("Them 1 mang!");
        active = true;
        // TODO: player.addLife();
    }

    @Override
    public void deactivate() {
        System.out.println("Khong gioi han thoi gian");
    }

    @Override
    public void update() {
        // Extra life không có thời gian giới hạn
        fall();
    }

    @Override
    public boolean isActive() {
        return active;
    }
}

