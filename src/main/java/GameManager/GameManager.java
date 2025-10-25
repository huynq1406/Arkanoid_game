package GameManager;

import Entities.Ball;
import Entities.Paddle;
import Entities.bricks.*;
import Levels.TextMapLevel;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    private final int width;
    private final int height;

    private final GamePanel panel;
    private List<AbstractBrick> bricks = new ArrayList<>();
    private final Ball ball;
    private final Paddle paddle;
    private List<Entities.PowerUp.PowerUp> powerUps = new ArrayList<>();

    private TextMapLevel currentLevel;
    private AnimationTimer loop;

    private int score = 0;

    private Image explosionImage;
    private final List<ExplosionEffect> effects = new ArrayList<>();

    private enum CollisionSide { NONE, LEFT, RIGHT, TOP, BOTTOM }

    public GameManager(int width, int height, GamePanel panel, Ball ball, Paddle paddle) {
        this.width = width;
        this.height = height;
        this.panel  = panel;
        this.ball   = ball;
        this.paddle = paddle;

        // try to load explosion image from resources (/images/explosion.png)
        InputStream is = getClass().getResourceAsStream("/images/explosion.png");
        if (is != null) {
            try {
                explosionImage = new Image(is);
            } catch (Exception ex) {
                explosionImage = null;
            }
        } else {
            explosionImage = null;
        }
    }

    public void buildLevel() {
        currentLevel = new TextMapLevel("levels/Map.txt", 1);
        currentLevel.buildFromMap(GamePanel.WIDTH);

        bricks = currentLevel.getBricks();
        panel.setRefs(ball, paddle, bricks);
    }

    public void start() {
        if (loop != null) loop.stop();
        loop = new AnimationTimer() {
            @Override public void handle(long now) { tick(); }
        };
        loop.start();
    }

    private void tick() {
        // If Ball/Paddle have update() methods, call them here
        // e.g. ball.update(); paddle.update();

        checkCollisionWithWalls();
        checkCollisionWithPaddle();
        checkCollisionWithBricks();
        removeDestroyedBricks();

        // render game objects via JavaFX canvas
        panel.render();

        // update and render transient effects (explosions, etc.)
        updateAndRenderEffects();

        // draw HUD (score, lives) on same canvas
        drawHUD();
    }

    /* --- Collision helpers using JavaFX Bounds built from existing getters --- */

    private Bounds boundsFrom(Ball b) {
        return new BoundingBox(b.getX(), b.getY(), b.getWidth(), b.getHeight());
    }
    private Bounds boundsFrom(Paddle p) {
        return new BoundingBox(p.getX(), p.getY(), p.getWidth(), p.getHeight());
    }
    private Bounds boundsFrom(AbstractBrick br) {
        return new BoundingBox(br.getX(), br.getY(), br.getWidth(), br.getHeight());
    }

    private CollisionSide checkCollision(Bounds a, Bounds b) {
        if (!a.intersects(b)) return CollisionSide.NONE;

        double aLeft = a.getMinX(), aRight = a.getMaxX(), aTop = a.getMinY(), aBottom = a.getMaxY();
        double bLeft = b.getMinX(), bRight = b.getMaxX(), bTop = b.getMinY(), bBottom = b.getMaxY();

        double overlapLeft   = aRight - bLeft;   // a chèn vào b từ trái
        double overlapRight  = bRight - aLeft;   // a chèn vào b từ phải
        double overlapTop    = aBottom - bTop;   // a chèn vào b từ trên
        double overlapBottom = bBottom - aTop;   // a chèn vào b từ dưới

        double minHoriz = Math.min(overlapLeft, overlapRight);
        double minVert  = Math.min(overlapTop, overlapBottom);

        if (minHoriz < minVert) {
            return (overlapLeft < overlapRight) ? CollisionSide.LEFT : CollisionSide.RIGHT;
        } else {
            return (overlapTop < overlapBottom) ? CollisionSide.TOP : CollisionSide.BOTTOM;
        }
    }

    private void checkCollisionWithWalls() {
        Bounds br = boundsFrom(ball);
        if (br.getMinX() <= 0 && ball.getDx() < 0)                 reflectBall(1, 0);  // left wall
        if (br.getMaxX() >= width && ball.getDx() > 0)             reflectBall(-1, 0); // right wall
        if (br.getMinY() <= 0 && ball.getDy() < 0)                 reflectBall(0, 1);  // ceiling
        // bottom handled elsewhere (life loss)
    }

    private void checkCollisionWithPaddle() {
        Bounds bBall = boundsFrom(ball);
        Bounds bPaddle = boundsFrom(paddle);
        if (bBall.intersects(bPaddle) && ball.getDy() > 0) {
            reflectBall(0, -1); // bounce up
            double dirX = ball.getDirX(), dirY = ball.getDirY();
            double minAbsY = 0.35;
            if (Math.abs(dirY) < minAbsY) {
                dirY = -minAbsY;
                ball.setDirection(dirX, dirY);
            }
        }
    }

    private void checkCollisionWithBricks() {
        Bounds bBall = boundsFrom(ball);
        for (AbstractBrick brick : bricks) {
            if (brick.isDestroyed()) {
                if (Math.random() < 0.99) {
                    powerUps.add(new Entities.PowerUp.BigPaddlePW(brick.getX(), brick.getY(),paddle));
                }
                continue;
            }
            CollisionSide side = checkCollision(bBall, boundsFrom(brick));
            if (side == CollisionSide.NONE) continue;

            switch (side) {
                case LEFT:  reflectBall(-1, 0); break;
                case RIGHT: reflectBall( 1, 0); break;
                case TOP:   reflectBall( 0,-1); break;
                case BOTTOM:reflectBall( 0, 1); break;
                default: break;
            }

            boolean destroyedNow = brick.takeHit(bricks);
            if (destroyedNow) {
                // spawn explosion effect at brick's position
                effects.add(new ExplosionEffect(brick.getX(), brick.getY(), 30, explosionImage));

                score += (brick instanceof StrongBricks) ? 150 : 50;
                score += (brick instanceof NormalBricks) ? 100 : 0;
                double newSpeed = Math.min(420, ball.getSpeed() * 1.02);
                ball.setSpeed(newSpeed);
            } else if (brick instanceof ExplosiveBrick) {
                ((ExplosiveBrick)brick).takeHit(bricks);
            }
        }
    }

    private void removeDestroyedBricks() {
        Iterator<AbstractBrick> it = bricks.iterator();
        while (it.hasNext()) {
            AbstractBrick b = it.next();
            if (b.isDestroyed()) it.remove();
        }
        panel.setRefs(ball, paddle, bricks);
    }

    private void reflectBall(int flipX, int flipY) {
        double dirX = ball.getDirX();
        double dirY = ball.getDirY();
        if (flipX != 0) dirX = -dirX;
        if (flipY != 0) dirY = -dirY;
        ball.setDirection(dirX, dirY);
    }

    private void drawHUD() {
        GraphicsContext g = panel.getGraphicsContext();
        // draw simple score text on top-left (overlay)
        g.setFill(Color.WHITE);
        g.fillText("Score: " + score, 10, 20);
        // add more HUD drawing here (lives, level, etc.)
    }

    private void updateAndRenderEffects() {
        GraphicsContext g = panel.getGraphicsContext();
        Iterator<ExplosionEffect> it = effects.iterator();
        while (it.hasNext()) {
            ExplosionEffect e = it.next();
            e.update();
            e.render(g);
            if (e.isFinished()) it.remove();
        }
    }

    // simple transient effect for destroyed bricks
    private static class ExplosionEffect {
        private final double x;
        private final double y;
        private int timer;
        private final int duration;
        private final Image img;
        private static final double SIZE = 48;

        ExplosionEffect(double x, double y, int duration, Image img) {
            this.x = x;
            this.y = y;
            this.duration = duration;
            this.img = img;
            this.timer = 0;
        }

        void update() {
            timer++;
        }

        void render(GraphicsContext g) {
            double alpha = 1.0 - ((double) timer / Math.max(1, duration));
            g.save();
            g.setGlobalAlpha(Math.max(0, alpha));
            if (img != null) {
                g.drawImage(img, x, y, SIZE, SIZE);
            } else {
                g.setFill(Color.ORANGE);
                g.fillOval(x, y, SIZE, SIZE);
                g.setStroke(Color.YELLOW);
                g.strokeOval(x, y, SIZE, SIZE);
            }
            g.restore();
        }

        boolean isFinished() {
            return timer >= duration;
        }
    }
}



