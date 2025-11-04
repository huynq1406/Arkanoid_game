package ObjectManager;

public class MovableObject extends GameObject {
    protected double dx;
    protected double dy;

    public MovableObject(int x, int y, int width, int height) {
        super(x,y,width,height);
    }

    protected void updatePosition(double dt) {
        x += (int) Math.round(dx * dt);
        y += (int) Math.round(dy * dt);
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
