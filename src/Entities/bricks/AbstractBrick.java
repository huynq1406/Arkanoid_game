package Entities.bricks;

public abstract class AbstractBrick {
        protected int x;          // tọa độ X
        protected int y;          // tọa độ Y
        protected int width;      // chiều rộng gạch
        protected int height;     // chiều cao gạch
        protected int hitPoints;  // số lần cần đánh để phá

        public AbstractBrick(int x, int y, int width, int height, int hitPoints) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.hitPoints = hitPoints;
        }

        // Getter
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public boolean isDestroyed() {
            return hitPoints <= 0;
        }

        // Setter
        public void setHitPoints(int hp) {
            this.hitPoints = hp;
        }



        // Abstract method (bắt buộc lớp con phải cài đặt) ----
        public abstract void onHit();
        /** Xử lý khi bị bóng đập vào; với abstract class bricks.AbstractBrick,
         mọi phương thức trong class có thể được dùng trong các class con
         bằng cách kế thừa (extends).*/
    }

