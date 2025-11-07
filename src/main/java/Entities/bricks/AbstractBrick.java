package Entities.bricks;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ObjectManager.GameObject;


public abstract class AbstractBrick extends GameObject {
    protected int hitPoints;
    protected boolean destroyed = false;

    /**
     * Constructor for AbstractBrick
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public AbstractBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.hitPoints = 1;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Handle when the brick is hit
     * @param allBricks
     * @return true if the brick is destroyed
     */
    public boolean takeHit(List<AbstractBrick> allBricks) {
        if (destroyed) return false;
        hitPoints--;
        if (hitPoints <= 0) {
            destroyed = true;
            return true;
        }
        return false;
    }

    protected abstract Color getBrickColor();

    @Override
    public abstract void render(GraphicsContext g);

}

