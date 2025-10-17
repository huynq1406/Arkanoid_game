package Entities.bricks;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class ExplosiveBrick extends AbstractBrick {


    public ExplosiveBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

//    public void update() {
//        // gạch đã bị phá
//        destroyed = true;
//        return;
//    }

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
        int explosionRange = width + 100; // bán kính nổ

        for (AbstractBrick b : allBricks) {
            if (b == this || b.isDestroyed()) continue;

            int dx = Math.abs(b.getX() - this.x);
            int dy = Math.abs(b.getY() - this.y);

            if (dx <= explosionRange && dy <= explosionRange) {
                boolean justDestroyed = b.takeHit(explodedBricks);
                if (justDestroyed) explodedBricks.add(b);
            }
        }
        return explodedBricks;
    }

    // Nếu bạn muốn có hiệu ứng update riêng (optional)
    @Override
    public void update(double dt) {
        if (destroyed) return;
        // hiệu ứng chớp nháy hoặc rung nhẹ (nếu muốn)
    }

    @Override
    public Color getBrickColor() {
        return Color.RED;
    }

    @Override
    public void render(Graphics g) {
        if(destroyed) return;
        g.setColor(getBrickColor());
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }
}
