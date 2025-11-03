package Entities.bricks;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
        return Color.CORNFLOWERBLUE;
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

