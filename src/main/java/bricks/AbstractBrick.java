package bricks;

import Entities.*;

import java.awt.*;

public abstract class AbstractBrick extends GameObject {
        protected int x;          // tọa độ X
        protected int y;          // tọa độ Y
        protected int width;      // chiều rộng gạch
        protected int height;     // chiều cao gạch
        protected int hitPoints;  // số lần cần đánh để phá

        public AbstractBrick(int x, int y, int width, int height, int hitPoints) {
            super(x,y, width, height);
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
        public void takeHit() {
            if(hitPoints > 0) {
                hitPoints--;
            }
        }

        public void render(Graphics g) {
            g.setColor(Color.PINK);
            g.fillRect(x, y, width, height);
        }
        /** Xử lý khi bị bóng đập vào; với abstract class bricks.AbstractBrick,
         mọi phương thức trong class có thể được dùng trong các class con
         bằng cách kế thừa (extends).*/
    }

