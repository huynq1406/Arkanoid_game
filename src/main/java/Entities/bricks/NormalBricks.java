package Entities.bricks;

import java.awt.*;

public class NormalBricks extends AbstractBrick {

    public NormalBricks(int x, int y, int width, int height) {
        super(x, y, width, height); // chỉ cần 1 hit để phá
    }

    @Override
    public void update(double dt) {
        // xử lý cập nhật trạng thái gạch, ví dụ:
        if (hitPoints <= 0) {
            // gạch đã bị phá
            return;
        }
    }

    protected Color getBrickColor() {
        return new Color(255, 160, 122);
    }

    @Override
    public void render(Graphics g) {
        if (destroyed) return;
        g.setColor(getBrickColor());
        g.fillRect(x,y,width,height);

        g.setColor(Color.black);
        g.drawRect(x,y,width,height);
    }
}

