package Entities.PowerUp;

public class ExtraLifePowerUp extends PowerUp {
    public ExtraLifePowerUp(double x, double y) {
        super(x, y, 4, "/elPW.png");
    }

    @Override
    public void activate() {
        System.out.println("Added 1 life");
    }

    @Override
    public void deactivate() {
        // Extra life is instant; nothing to deactivate
    }
}


