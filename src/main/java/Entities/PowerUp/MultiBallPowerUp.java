package Entities.PowerUp;

import Entities.Ball;
import ObjectManager.BallManager;

import java.util.List;

public class MultiBallPowerUp extends PowerUp implements IPowerUp {
    private final List<Ball> referenceBalls;
    private BallManager ballManager;

    public MultiBallPowerUp(float x, float y, List<Ball> referenceBalls, BallManager ballManager) {
        super(x, y, 3, "/mbPW.png");
        this.referenceBalls = referenceBalls;
        this.ballManager = ballManager;
    }

    @Override
    public void activate() {
        if (referenceBalls == null || referenceBalls.isEmpty()) return;
        Ball original = referenceBalls.get(0);
//        Ball newBall = new Ball((int) original.getX(), (int) original.getY(), original.getWidth() / 2);
//        newBall.setSpeed(original.getSpeed());
//        newBall.setDirection(-original.getDirX(), original.getDirY());
//        newBall.launch();
//        referenceBalls.add(newBall);
        active = true;
        for (Ball originalBall : referenceBalls) {
            // Tạo 2 bóng mới với hướng khác nhau
            Ball newBall1 = new Ball((int)originalBall.getX(), (int)originalBall.getY(), originalBall.getDiameter()/2);
            newBall1.launch();
            newBall1.setDirection(-originalBall.getDirX(), originalBall.getDirY()); // Hướng ngược lại

            Ball newBall2 = new Ball((int)originalBall.getX(), (int)originalBall.getY(), originalBall.getDiameter()/2);
            newBall2.launch();
            newBall2.setDirection(originalBall.getDirX() * 0.5, originalBall.getDirY() * 1.5); // Hướng chệch

            // Lỗi ở đây: ballManager phải là một trường được truyền vào
            if (ballManager != null) {
                ballManager.addBall(newBall1); // <-- Gọi lỗi NullPointerException
                ballManager.addBall(newBall2); // <-- Gọi lỗi NullPointerException
                System.out.println("MultiBall activated, added 2 balls to manager.");
            }
        }
    }

    @Override
    public void deactivate() {
        active = false;
    }

    @Override
    public void update() {
        fall();
    }
}

