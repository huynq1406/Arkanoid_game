package Entities.bricks;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class ExplosiveBrick extends AbstractBrick {

    // Bán kính của vụ nổ, bạn có thể chỉnh sửa giá trị này
    private static final int EXPLOSION_RADIUS = 80;

    public ExplosiveBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
        // Gạch nổ sẽ bị phá hủy sau 1 lần va chạm
        this.hitPoints = 1; 
    }

    @Override
    protected Color getBrickColor() {
        // Màu đỏ để dễ nhận biết
        return new Color(220, 50, 50); 
    }

    @Override
    public void render(Graphics g) {
        if (destroyed) {
            return;
        }
        
        // Vẽ thân gạch
        g.setColor(getBrickColor());
        g.fillRect(x, y, width, height);

        // Vẽ một hình tròn nhỏ ở giữa để làm dấu hiệu nhận biết
        g.setColor(Color.YELLOW);
        g.fillOval(x + width / 2 - 5, y + height / 2 - 5, 10, 10);

        // Vẽ viền
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    /**
     * Kích hoạt vụ nổ, phá hủy các viên gạch lân cận.
     * @param allBricks Danh sách tất cả các viên gạch trong màn chơi.
     */
    public void explode(List<AbstractBrick> allBricks) {
        // Tính tọa độ tâm của viên gạch nổ
        double centerX = this.x + (double)this.width / 2;
        double centerY = this.y + (double)this.height / 2;

        // Duyệt qua tất cả các viên gạch để kiểm tra khoảng cách
        for (AbstractBrick otherBrick : allBricks) {
            // Bỏ qua chính nó và các gạch đã bị phá hủy hoặc không thể phá hủy
            if (otherBrick == this || otherBrick.isDestroyed() || otherBrick instanceof IndestructibleBrick) {
                continue;
            }

            // Tính tọa độ tâm của viên gạch "hàng xóm"
            double otherCenterX = otherBrick.getX() + (double)otherBrick.getWidth() / 2;
            double otherCenterY = otherBrick.getY() + (double)otherBrick.getHeight() / 2;
            
            // Tính khoảng cách giữa hai tâm
            double distance = Math.sqrt(Math.pow(centerX - otherCenterX, 2) + Math.pow(centerY - otherCenterY, 2));

            // Nếu gạch hàng xóm nằm trong bán kính vụ nổ, phá hủy nó
            if (distance <= EXPLOSION_RADIUS) {
                // Gọi takeHit() cho đến khi nó bị phá hủy hoàn toàn
                while (!otherBrick.isDestroyed()) {
                    otherBrick.takeHit();
                }
            }
        }
    }
}