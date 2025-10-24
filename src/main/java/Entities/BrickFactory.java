package Entities;

import Entities.bricks.AbstractBrick;
import Entities.bricks.NormalBricks;
import Entities.bricks.StrongBricks;
import Entities.bricks.IndestructibleBrick;
import Entities.bricks.ExplosiveBrick;

public final class BrickFactory {
    private BrickFactory() {}
    public static AbstractBrick fromSymbol(char symbol, int x, int y, int width, int height) {
        switch (symbol) {
            case '1':
                return new NormalBricks(x, y, width, height);
            case '3':
                return new StrongBricks(x, y, width, height);
            case 'x':
                return new IndestructibleBrick(x, y, width, height);
            case 'S':
                return new ExplosiveBrick(x, y, width, height);
            case ' ':
            default:
                return null;
        }
    }


}
