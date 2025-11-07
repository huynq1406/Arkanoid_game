package Entities.PowerUp;

import Entities.Ball;
import ObjectManager.BallManager;

import java.util.List;

public class MultiBallPowerUp extends PowerUp  {
    private final List<Ball> referenceBalls;
    private BallManager ballManager;

    public MultiBallPowerUp(double x, double y, List<Ball> referenceBalls, BallManager ballManager) {
        super(x, y, 3, "/mbPW.png");
        this.referenceBalls = referenceBalls;
        this.ballManager = ballManager;
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }
}

