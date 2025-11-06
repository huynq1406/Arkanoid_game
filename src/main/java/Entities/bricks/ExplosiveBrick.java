package Entities.bricks;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ExplosiveBrick extends AbstractBrick {
    public ExplosiveBrick(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
        // gạch đã bị phá
        destroyed = true;
        return;
    }

    @Override
    public boolean takeHit(List<AbstractBrick> allBricks) {
        if (!destroyed) {
            destroyed = true;
            explode(allBricks);
            return true;
        }
        return false;
    }

    // Dành cho xử lý lan nổ
    public List<AbstractBrick> explode(List<AbstractBrick> allBricks) {
        List<AbstractBrick> explodedBricks = new ArrayList<>();
        double explosionRange = width + 50; // bán kính nổ

        for (AbstractBrick b : allBricks) {
            if (b == this || b.isDestroyed()) continue;

            double dx = Math.abs(b.getX() - this.x); //khoang cach giua vien gach kiem tra va vien gach phat no
            double dy = Math.abs(b.getY() - this.y);

            if (dx <= explosionRange && dy <= explosionRange) {
                boolean justDestroyed = b.takeHit(explodedBricks); //nổ không liên hoàn
                if (justDestroyed) explodedBricks.add(b);
            }
        }
        return explodedBricks;
    }

    // Nếu bạn muốn có hiệu ứng update riêng (optional)
    @Override
    public void update(double dt) {
        if (destroyed) return;
    }

    @Override
    public Color getBrickColor() {
        return Color.INDIANRED;
    }

    @Override
    public void render(GraphicsContext g) {
        if(destroyed) return;
        g.setFill(getBrickColor());
        g.fillRect(x, y, width, height);

        g.setStroke(Color.BLACK);
        g.strokeRect(x, y, width, height);
    }
}
