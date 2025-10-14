package GameManager;

import Entities.Ball;
import Entities.Paddle;
import Entities.bricks.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final int width;
    private final int height;

    private final Paddle paddle;
    private final Ball ball;
    private final List<AbstractBrick> bricks = new ArrayList<>();

    private boolean running = true;
    private boolean win = false;
    private int lives = 3;
    private int score = 0;

    public GameManager(int width, int height) {
        this.width = width;
        this.height = height;

        paddle = new Paddle(width/2 - 50, height - 40, 100, 14);
        ball = new Ball(paddle.getX() + paddle.getWidth()/2, paddle.getY() - 10, 10);
        ball.resetToPaddle(paddle);

        buildLevel();
    }

    private void buildLevel() {
        char[][] map = new char[][]{
                {'N','N','S','N','N','S','N','N','S','N'},
                {'N','S','S','N','N','N','S','S','N','N'},
                {'N','N','N','N','S','I','N','N','N','S'},
                {'S','I','N','S','N','N','S','N','N','N'},
                {'N','N','S','N','N','S','N','N','S','N'},
                {'N','E','I','E','I','E','I','E','N','N'}
        };
        int cols = map[0].length, rows = map.length;
        int marginTop = 60, marginSide = 20, gap = 4;
        int cellW = (width - marginSide*2 - (cols-1)*gap) / cols;
        int cellH = 24;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = marginSide + c * (cellW + gap);
                int y = marginTop + r * (cellH + gap);
                if (map[r][c] == 'N') bricks.add(new NormalBricks(x, y, cellW, cellH));
                else if (map[r][c] == 'S') bricks.add(new StrongBricks(x, y, cellW, cellH));
                else if (map[r][c] == 'I') bricks.add(new IndestructibleBrick(x, y, cellW, cellH));
                else if (map[r][c] == 'E') bricks.add(new ExplosiveBrick(x, y, cellW, cellH));
            }
        }
    }

    public void update(double dt) {
        if (!running) return;

        ball.update(dt, paddle);
        checkCollisionWithWalls();
        checkCollisionWithPaddle();
        checkCollisionWithBricks();

        // Bóng rơi khỏi đáy
        if (ball.getY() > height) {
            lives--;
            if (lives <= 0) running = false;
            else ball.resetToPaddle(paddle);
        }

        // Thắng nếu mọi gạch vỡ
        win = bricks.stream().allMatch(AbstractBrick::isDestroyed);
        if (win) running = false;
    }

    public void launchBall() {
//        if (!ball.isLaunched())
        ball.launch();
    }

    public void movePaddleToMouseX(int mouseX) { paddle.setCenterX(mouseX); paddle.clamp(0, width); }
    public void restart() {
        bricks.clear(); lives = 3; score = 0; win = false; running = true;
        paddle.setX(width/2 - paddle.getWidth()/2); paddle.setY(height - 40);
        ball.resetToPaddle(paddle);
        buildLevel();
    }
    public boolean isRunning() { return running; }

    private enum Side { NONE, LEFT, RIGHT, TOP, BOTTOM }

    private void reflectBall(double nx, double ny) {
        double vx = ball.getDirX(), vy = ball.getDirY();
        double dot = vx * nx + vy * ny;
        double rx = vx - 2 * dot * nx;
        double ry = vy - 2 * dot * ny;
        ball.setDirection(rx, ry);
    }

    private Side checkCollision(Rectangle a, Rectangle b) {
        if (!a.intersects(b)) return Side.NONE;

        int aRight = a.x + a.width, aBottom = a.y + a.height;
        int bRight = b.x + b.width, bBottom = b.y + b.height;

        int overlapLeft   = aRight - b.x;   // a chèn vào b từ trái
        int overlapRight  = bRight - a.x;  // a chèn vào b từ phải
        int overlapTop    = aBottom - b.y; // a chèn vào b từ trên
        int overlapBottom = bBottom - a.y; // a chèn vào b từ dưới

        int minHoriz = Math.min(overlapLeft, overlapRight);
        int minVert  = Math.min(overlapTop, overlapBottom);

        if (minHoriz < minVert) {
            // ưu tiên trục X
            return (overlapLeft < overlapRight) ? Side.LEFT : Side.RIGHT;
        } else {
            return (overlapTop < overlapBottom) ? Side.TOP : Side.BOTTOM;
        }
    }

    private void checkCollisionWithWalls() {
        Rectangle br = ball.getBounds();
        if (br.x <= 0 && ball.getDx() < 0)          reflectBall( 1, 0); // tường trái
        if (br.x + br.width >= width && ball.getDx() > 0) reflectBall(-1, 0); // tường phải
        if (br.y <= 0 && ball.getDy() < 0)          reflectBall( 0, 1); // trần
        // đáy xử lý ở update(): mất mạng
    }

    private void checkCollisionWithPaddle() {
        Rectangle side = ball.getBounds();
        if (side.intersects(paddle.getBounds()) && ball.getDy() > 0) {
            reflectBall(0, -1); // bật ngược lên
            // Ép góc nảy tối thiểu theo trục Y (tránh gần-đứng)
            double dirX = ball.getDirX(), dirY = ball.getDirY();
            double minAbsY = 0.35;
            if (Math.abs(dirY) < minAbsY) {
                dirY = -minAbsY;
                ball.setDirection(dirX, dirY);
            }
        }
    }

    private void checkCollisionWithBricks() {
        Rectangle bRect = ball.getBounds();
        for (AbstractBrick brick : bricks) {
            if (brick.isDestroyed()) continue;
            Side side = checkCollision(bRect, brick.getBounds());
            if (side == Side.NONE) continue;

            switch (side) {
                case LEFT:  reflectBall(-1, 0); break;
                case RIGHT: reflectBall( 1, 0); break;
                case TOP:   reflectBall( 0,-1); break;
                case BOTTOM:reflectBall( 0, 1); break;
            }

            boolean destroyedNow = brick.takeHit();
            if (destroyedNow) {
                score += (brick instanceof StrongBricks) ? 150 : 50;
                score += (brick instanceof NormalBricks) ? 100 : 0;
                // tăng tốc nhẹ, có trần
                double newSpeed = Math.min(420, ball.getSpeed() * 1.02);
                ball.setSpeed(newSpeed);
            } else if (brick instanceof ExplosiveBrick) {
                ((ExplosiveBrick)brick).takeHit(bricks);
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(new Color(20, 20, 30));
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(80, 80, 100));
        g.drawRect(0, 0, width-1, height-1);

        for (AbstractBrick br : bricks)
            br.render(g);
        paddle.render(g);
        ball.render(g);

        g.setColor(Color.WHITE);
        g.drawString("Lives: " + lives + "   Score: " + score, 10, 16);
        if (!running) {
            String text = (win ? "YOU WIN!" : "GAME OVER");
            g.setFont(g.getFont().deriveFont(Font.BOLD, 24f));
            g.drawString(text, width/2 - 70, height/2);
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
            g.drawString("Click để chơi lại", width/2 - 55, height/2 + 20);
        } else if (!ball.isLaunched()) {
            g.drawString("Click để bắn bóng", width/2 - 55, height - 10);
        }
    }
}
