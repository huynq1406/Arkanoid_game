package Entities.bricks;

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
    }

