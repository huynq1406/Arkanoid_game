package ObjectManager;

public class MovableObject extends GameObject {
    protected double dx;
    protected double dy;

    public MovableObject(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    protected void updatePosition(double dt) {
        x += dx * dt;
        y += dy * dt;
    }

    public void render(javafx.scene.canvas.GraphicsContext g) {
        // Default render method (can be overridden)
        g.setFill(javafx.scene.paint.Color.GRAY);
        g.fillRect(x, y, width, height);
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
}
