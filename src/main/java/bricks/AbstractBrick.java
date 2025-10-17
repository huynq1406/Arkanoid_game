package bricks;

import Entities.GameObject;
import java.awt.*;

/**
 * Lớp gạch cơ sở có số lần chịu đòn (hitPoints).
 * - hit(): trả về true nếu gạch VỠ ở lần này.
 * - isDestroyed(): đã vỡ chưa.
 * - getColor(): màu hiển thị phụ thuộc subclass & HP còn lại.
 */
public abstract class AbstractBrick extends GameObject {
    protected int hitPoints;
    protected boolean destroyed = false;

    public AbstractBrick(int x, int y, int width, int height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
    }

    /** Gọi khi bóng chạm gạch. Trả về true nếu vỡ ở lần chạm này. */
    public boolean hit() {
        if (destroyed) return false;
        hitPoints--;
        if (hitPoints <= 0) { destroyed = true; return true; }
        return false;
    }

    public boolean isDestroyed() { return destroyed; }

    protected abstract Color getColor();

    @Override
    public void render(Graphics g) {
        if (destroyed) return;
        g.setColor(getColor());
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }
}
//update
