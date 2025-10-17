package Entities.bricks;

import java.awt.*;
import java.util.List;

import Entities.Ball;

public class IndestructibleBrick extends AbstractBrick {
    public IndestructibleBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
            // gạch không bao giờ bị phá
        destroyed = false;
    }

    @Override
    public boolean takeHit(List<AbstractBrick> allBricks) {
        // Gạch không bao giờ bị phá
        return false;
    }


    public Color getBrickColor() {
        return Color.DARK_GRAY;
    }

    public void render(Graphics g) {
        g.setColor(getBrickColor());
        g.fillRect(x,y,width,height);

        g.setColor(Color.BLACK);
        g.drawRect(x,y,width,height);
    }
}
