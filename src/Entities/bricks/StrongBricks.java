package Entities.bricks;

import java.awt.*;

public class StrongBricks extends AbstractBrick {

    public StrongBricks(int x, int y, int width, int height) {
        super(x, y, width, height); // cần 3 hit để phá
        this.hitPoints = 3;
    }

    public void update(double dt) {
        // xử lý cập nhật trạng thái gạch, ví dụ:
        if (hitPoints <= 0) {
            // gạch đã bị phá
            return;
        }
    }

    @Override
    protected Color getBrickColor() {
        switch (hitPoints) {
            case 3:
                return new Color(139, 0, 0); // đỏ đậm
            case 2:
                return new Color(205, 92, 92); // đỏ nhạt
            case 1:
                return new Color(255, 160, 122); // hồng nhạt
            default:
                return Color.GRAY; // đã vỡ
        }
    }

    @Override
    public void render(Graphics g) {
        if (destroyed) return;
        g.setColor(getBrickColor());
        g.fillRect(x,y,width,height);

        g.setColor(Color.BLACK);
        g.drawRect(x,y,width,height);
    }
}

