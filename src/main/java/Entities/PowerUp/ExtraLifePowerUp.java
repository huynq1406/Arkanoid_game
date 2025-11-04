package Entities.PowerUp;

public class ExtraLifePowerUp extends PowerUp implements IPowerUp {
    public ExtraLifePowerUp(float x, float y) {
        super(x, y, 4, "/elPW.png");
    }

    @Override
    public void activate() {
        active = true;
        // TODO: integrate with player/lives system: player.addLife();
        System.out.println("Added 1 life");
    }

    @Override
    public void deactivate() {
        // Extra life is instant; nothing to deactivate
    }

    @Override
    public void update() {
        // Extra life item falls until collected
        fall();
    }
}


