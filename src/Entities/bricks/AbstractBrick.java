package Entities.bricks;

import ObjectManager.GameObject;

import java.awt.*;

public abstract class AbstractBrick extends GameObject {
    protected int hitPoints;
    protected boolean destroyed;

    public AbstractBrick(int x, int y, int width, int height) {
        super(x,y,width, height);
        this.hitPoints = 1;
        this.destroyed = false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean takeHit() {
        hitPoints--;
        if (destroyed) {
            return false;
        }

        if (hitPoints <= 0) {
            destroyed = true;
            return true;
        }

        return false;
    }

    protected abstract Color getBrickColor();

    @Override
    public abstract void render(Graphics g);
}

