package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ObjectManager.MovableObject;

public class Paddle extends MovableObject {

    public Paddle(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }

    public void setCenterX(double mouseX) {
        this.x = mouseX - width / 2;
    }

    public void clamp(int minX, int maxX) {
        if (x < minX) x = minX;
        if (x + width > maxX) x = maxX - width;
    }

    @Override
    public void update(double dt) {}

    public void render(GraphicsContext g) {
        g.setFill(Color.web("#28aaff"));
        g.fillRoundRect(x, y, width, height, 12, 12);

        g.setStroke(Color.web("#145078"));
        g.strokeRoundRect(x, y, width, height, 12, 12);

        g.setFill(Color.rgb(255, 255, 255, 0.2));
        g.fillRect(x + width / 2.0 - 2, y + 3, 4, height - 6);
    }
}
