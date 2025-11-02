package Entities.bricks;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import Entities.*;

public class IndestructibleBrick extends AbstractBrick {
    public IndestructibleBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
        destroyed = false;
    }

    @Override
    public boolean takeHit(List<AbstractBrick> allBricks) {
        return false;
    }


    public Color getBrickColor() {
        return Color.DARKGRAY;
    }

    public void render(GraphicsContext g) {
        g.setFill(getBrickColor());
        g.fillRect(x,y,width,height);

        g.setStroke(Color.BLACK);
        g.strokeRect(x,y,width,height);
    }
}
