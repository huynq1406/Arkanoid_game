package ObjectManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public abstract class GameObject {
    protected int x, y;
    protected int width, height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Dùng Rectangle2D thay vì java.awt.Rectangle
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    // Vẽ bằng JavaFX GraphicsContext
    public abstract void render(GraphicsContext g);

    public void update(double dt) {}

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
}
