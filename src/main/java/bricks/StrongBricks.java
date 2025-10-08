package bricks;

import java.awt.*;

public class StrongBricks extends AbstractBrick {

        public StrongBricks(int x, int y, int width, int height) {
            super(x, y, width, height, 3); // cần 3 hit để phá
        }

        @Override
        public void onHit() {
            if (!isDestroyed()) {
                hitPoints--; // Giảm máu
                if (isDestroyed()) {
                    System.out.println("Strong Brick đã vỡ!");
                } else {
                    System.out.println("Strong Brick bị đánh! Còn lại: " + hitPoints);
                }
            }
        }
    @Override
    public void update() {
        // xử lý cập nhật trạng thái gạch, ví dụ:
        if (hitPoints <= 0) {
            // gạch đã bị phá
        }
    }

    @Override
    public void render(Graphics g) {
    }
    }

