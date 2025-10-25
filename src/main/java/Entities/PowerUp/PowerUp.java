// java
package Entities.PowerUp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.InputStream;

public abstract class PowerUp {
    protected double x, y;
    protected int type;
    protected int duration = 180; // default frames
    protected int timer = 0;
    protected boolean active = false;
    protected Image image;
    protected static final double WIDTH = 32;
    protected static final double HEIGHT = 32;
    protected static final double FALL_SPEED = 2.5;

    public PowerUp(float x, float y, int type, String imagePath) {
        this.x = x;
        this.y = y;
        this.type = type;
        if (imagePath != null) {
            try (InputStream is = getClass().getResourceAsStream(imagePath)) {
                if (is != null) image = new Image(is);
            } catch (Exception ignored) { image = null; }
        }
    }

    public int getType() { return type; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return WIDTH; }
    public double getHeight() { return HEIGHT; }

    // default falling behaviour
    public void fall() {
        this.y += FALL_SPEED;
    }

    public void render(GraphicsContext g) {
        if (image != null) {
            g.drawImage(image, x, y, WIDTH, HEIGHT);
        } else {
            g.setFill(Color.LIGHTGREEN);
            g.fillRect(x, y, WIDTH, HEIGHT);
            g.setStroke(Color.BLACK);
            g.strokeRect(x, y, WIDTH, HEIGHT);
        }
    }

    public boolean isActive() { return active; }

    // subclasses must implement
    public abstract void activate();
    public abstract void deactivate();
    public abstract void update();

    // simple helper for outside code to decide removal
    public boolean isOffScreen(double screenHeight) {
        return y > screenHeight + 50;
    }
}

