package GameTest;

import java.util.*;
import Entities.bricks.AbstractBrick;
import Entities.bricks.NormalBricks;
import Entities.bricks.StrongBricks;

public class Test {
        public static void main(String[] args) {
            List<AbstractBrick> bricks = new ArrayList<>();
            NormalBricks nb = new NormalBricks(10, 10, 50, 20);

            // Tạo một gạch thường và một gạch bền
            bricks.add(new NormalBricks(10, 10, 50, 20));
            bricks.add(new StrongBricks(70, 10, 50, 20));

            // Giả sử bóng đập trúng từng gạch 3 lần
            for (int i = 0; i < 3; i++) {
                System.out.println("----- Lượt đánh " + (i+1) + " -----");
                for (AbstractBrick brick : bricks) {
                    brick.takeHit(bricks); // Gọi Đa hình
                }
            }
        }
    }


