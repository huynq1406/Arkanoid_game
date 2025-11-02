package Entities.bricks;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
                return Color.DARKRED; // đỏ đậm
            case 2:
                return Color.RED; // đỏ nhạt
            case 1:
                return Color.LIGHTPINK; // hồng nhạt
            default:
                return Color.GRAY; // đã vỡ
        }
    }

    @Override
    public void render(GraphicsContext g) {
        if (destroyed) return;
        g.setFill(getBrickColor());
        g.fillRect(x,y,width,height);

        g.setStroke(Color.BLACK);
        g.strokeRect(x,y,width,height);
    }
}

