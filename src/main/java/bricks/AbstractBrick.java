package bricks;

import Entities.*;

import java.awt.*;

public abstract class AbstractBrick extends GameObject {
        private int hitPoints;
        private boolean destroyed = false;
        private final Color color;

        public AbstractBrick(int x, int y, int width, int height, int hitPoints, Color color) {
            super(x,y,width, height);
            this.hitPoints = hitPoints;
            this.color = color;
        }
        public boolean isDestroyed() {
            return destroyed;
        }

        public void takeHit() {
            if (destroyed) return;
            hitPoints--;
            if (hitPoints <= 0) destroyed = true;
        }

        @Override
        public void render(Graphics g) {
            if (destroyed) return;
            g.setColor(color);
            g.fillRect(x,y,width,height);

            g.setColor(Color.BLACK);
            g.drawRect(x,y,width,height);
        }
    }

