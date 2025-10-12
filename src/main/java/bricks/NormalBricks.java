package bricks;

import java.awt.*;

/** Gạch thường: 1 hit là vỡ. Màu xanh lá. */
public class NormalBricks extends AbstractBrick {
    public NormalBricks(int x, int y, int w, int h) {
        super(x, y, w, h, 1);
    }

    @Override
    protected Color getColor() { return new Color(46, 204, 113); }
}
