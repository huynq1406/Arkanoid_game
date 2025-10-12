package bricks;

import java.awt.*;

/** Gạch mạnh: 2 hit mới vỡ. Vàng (2HP) → Xám nhạt (1HP). */
public class StrongBricks extends AbstractBrick {
    public StrongBricks(int x, int y, int w, int h) {
        super(x, y, w, h, 2);
    }

    @Override
    protected Color getColor() {
        if (destroyed) return new Color(0,0,0,0);
        return (hitPoints >= 2) ? new Color(241, 196, 15) : new Color(189, 195, 199);
    }
}
