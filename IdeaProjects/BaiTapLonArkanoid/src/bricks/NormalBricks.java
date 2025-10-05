package bricks;

    public class NormalBricks extends AbstractBrick {

        public NormalBricks(int x, int y, int width, int height) {
            super(x, y, width, height, 1); // chỉ cần 1 hit để phá
        }

        @Override
        public void onHit() {
            hitPoints--; // Giảm máu
            System.out.println("Normal Brick bị đánh! Trạng thái: "
                    + (isDestroyed() ? "Đã vỡ" : "Còn nguyên"));
        }
    }

