package Entities.bricks;

import java.util.List;
import java.awt.*;

public class ExplosiveBrick extends AbstractBrick {

    public ExplosiveBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
        // gạch đã bị phá
        destroyed = true;
        return;
    }

    public boolean takeHit(List<AbstractBrick> allBricks) {
        if (!destroyed) {
            destroyed = true; // Gạch này vỡ
            explode(allBricks); // Kích hoạt nổ lan
            return true;
        }
        return false;
    }

    private void explode(List<AbstractBrick> allBricks) {
        int explosionRange = width + 4; // bán kính nổ

        for (AbstractBrick b : allBricks) {
            if (b == this || b.isDestroyed()) continue;

            int dx = Math.abs(b.getX() - this.x);
            int dy = Math.abs(b.getY() - this.y);

            // Nếu nằm trong vùng nổ
            if (dx <= explosionRange && dy <= explosionRange) {
                b.takeHit(); // phá gạch gần kề
            }
        }
    }

    public Color getBrickColor() {
        return Color.RED;
    }

    public void render(Graphics g) {
        g.setColor(getBrickColor());
        g.fillRect(x,y,width,height);

        g.setColor(Color.BLACK);
        g.drawRect(x,y,width,height);
    }
}
