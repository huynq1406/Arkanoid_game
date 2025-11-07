package GameManager;

import Entities.Ball;
import Entities.Paddle;
import Entities.bricks.*;
import Levels.TextMapLevel;
import Entities.PowerUp.*;
import ObjectManager.BallManager;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.function.Consumer;

public class GameManager {
    private final int width;
    private final int height;
    private final GamePanel panel;
    private final GameHUD gameHUD;
    private final Ball ball;
    private final Paddle paddle;
    private String playerName;
    private final Consumer<Integer> onGameOver;
    private MediaPlayer backgroundMusicPlayer;
    private Media backgroundMedia;

    private BallManager ballManager = new BallManager();
    private PowerUpManager powerUpManager = new PowerUpManager();
    private List<AbstractBrick> bricks = new ArrayList<>();

    private TextMapLevel currentLevel;
    private AnimationTimer loop;

    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private int levelIndex = 1;

    private Image explosionImage;
    private final List<ExplosionEffect> effects = new ArrayList<>();

    private enum CollisionSide { NONE, LEFT, RIGHT, TOP, BOTTOM }

    public GameManager(int width, int height, GamePanel panel, Paddle paddle, String playerName, Consumer<Integer> onGameOver, GameHUD gameHUD) {
        this.width = width;
        this.height = height;
        this.panel  = panel;
        this.ball   = new Ball(400, 300, 10);
        ballManager.addBall(this.ball);
        this.paddle = paddle;
        this.playerName = playerName;
        this.onGameOver = onGameOver;
        this.gameHUD = gameHUD;

        // try to load explosion image from resources (/images/explosion.png)
        InputStream is = getClass().getResourceAsStream("/images/explosion.png");
        if (is != null) {
            try {
                explosionImage = new Image(is);
            } catch (Exception ex) {
                explosionImage = null;
            }
        } else {
            explosionImage = null;
        }
        loadBackgroundMusic("/audio/space.mp3");
        if (this.gameHUD != null) {
            this.gameHUD.updateScore(this.score); // 0 điểm
            this.gameHUD.updateLives(this.lives); // 3 mạng
            this.gameHUD.updateLevel(this.levelIndex); // Level 1
        }
    }

    /**
     * Build the current level from map file
     */
    public void buildLevel() {
        GraphicsContext gc = panel.getGraphicsContext();
        currentLevel = new TextMapLevel("levels/Map.txt", levelIndex, gc);
        try {
            currentLevel.buildFromMap(GamePanel.WIDTH);
            bricks = new ArrayList<>(currentLevel.getBricks());
            panel.setRefs(ball, paddle, bricks);
            powerUpManager = new PowerUpManager();
            ballManager.clearAll();
            lives++;
            ball.resetToPaddle(paddle);  // Reset ball khi bắt đầu level mới
            System.out.println("Loaded level " + levelIndex);
        } catch (IllegalArgumentException e) {
            // Xử lý nếu không tìm thấy level (hết level)
            gameOver = true;
            loop.stop();
            
            if (onGameOver != null) {
                onGameOver.accept(this.score);
            }
        }
    }

    /**
     * Start the game loop
     */
    public void start() {
        if (loop != null) loop.stop();
        loop = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double dt = (now - lastTime) / 1_000_000_000.0; // giây
                lastTime = now;
                tick(dt);
            }
        };
        loop.start();
        startMusic();
    }

    public void onMouseMove(double mouseX) {
        paddle.setCenterX((int) mouseX);
        paddle.clamp(0, width);
    }

    public void onMousePress() {
        for (Ball b : ballManager.getBalls()) {
            if (!b.isLaunched()) {
                b.launch();
            }
        }
    }

    /**
     * Game update tick
     * @param dt Delta time in seconds since last tick
     */
    private void tick(double dt) {
        if (gameOver) return;

        ballManager.updateAll(dt, paddle);
        paddle.update(dt);
        powerUpManager.updateAll();
        gameHUD.updateAll(score, lives, levelIndex);

        checkLoseLife();
        checkCollisionWithWalls();
        checkCollisionWithPaddle();
        checkCollisionWithBricks();
        checkCollisionWithPowerUps();
        removeDestroyedBricks();

        boolean breakableBricksLeft = false;
        for (AbstractBrick brick : bricks) {
            if (!(brick instanceof IndestructibleBrick)) {
                breakableBricksLeft = true; // Vẫn còn gạch có thể phá
                break; // Thoát vòng lặp
            }
        }

        // Nếu không còn gạch nào có thể phá (chỉ còn gạch Unbreakable hoặc không còn gạch nào)
        if (!breakableBricksLeft && !gameOver) {
            nextLevel();
        }

        if (currentLevel != null) {
            currentLevel.renderBackground(panel.getGraphicsContext(), width, height);
        } else {
            GraphicsContext g = panel.getGraphicsContext();
            g.setFill(Color.BLACK);
            g.fillRect(0, 0, width, height);
            if (currentLevel != null) {
                currentLevel.renderBackground(g, width, height);
            }
        }
        panel.render();
        ballManager.renderAll(panel.getGraphicsContext());
        powerUpManager.renderAll(panel.getGraphicsContext());
        updateAndRenderEffects();
    }

    private void nextLevel() {
        levelIndex++;
        buildLevel();
        score += 1000;
    }

    /* --- Collision helpers using JavaFX Bounds built from existing getters --- */

    private Bounds boundsFrom(Ball b) {
        return new BoundingBox(b.getX(), b.getY(), b.getWidth(), b.getHeight());
    }
    private Bounds boundsFrom(Paddle p) {
        return new BoundingBox(p.getX(), p.getY(), p.getWidth(), p.getHeight());
    }
    private Bounds boundsFrom(AbstractBrick br) {
        return new BoundingBox(br.getX(), br.getY(), br.getWidth(), br.getHeight());
    }

    private void adjustBallPosition(Ball b, Bounds bBall, Bounds bBrick, CollisionSide side) {
        switch (side) {
            case LEFT:
                b.setX(bBrick.getMinX() - bBall.getWidth() - 1);
                break;
            case RIGHT:
                b.setX(bBrick.getMaxX() + 1);
                break;
            case TOP:
                b.setY(bBrick.getMinY() - bBall.getHeight() - 1);
                break;
            case BOTTOM:
                b.setY(bBrick.getMaxY() + 1);
                break;
            case NONE:
                break;
        }
    }

    private CollisionSide checkCollision(Ball b, Bounds a, Bounds bBrick) {
        if (!a.intersects(bBrick)) return CollisionSide.NONE;

        double aLeft = a.getMinX(), aRight = a.getMaxX(), aTop = a.getMinY(), aBottom = a.getMaxY();
        double bLeft = bBrick.getMinX(), bRight = bBrick.getMaxX(), bTop = bBrick.getMinY(), bBottom = bBrick.getMaxY();

        double overlapLeft   = aRight - bLeft;   // a chèn vào b từ trái
        double overlapRight  = bRight - aLeft;   // a chèn vào b từ phải
        double overlapTop    = aBottom - bTop;   // a chèn vào b từ trên
        double overlapBottom = bBottom - aTop;   // a chèn vào b từ dưới

        double minHoriz = Math.min(overlapLeft, overlapRight);
        double minVert  = Math.min(overlapTop, overlapBottom);

        double threshold = 1.0;  // Nếu gần bằng, coi như góc
        if (Math.abs(minHoriz - minVert) < threshold) {
            // Góc: Trả về side dựa trên velocity
            if (b.getDx() != 0 && b.getDy() != 0) {
                return (Math.abs(b.getDx()) > Math.abs(b.getDy())) ?
                       (overlapLeft < overlapRight ? CollisionSide.LEFT : CollisionSide.RIGHT) :
                       (overlapTop < overlapBottom ? CollisionSide.TOP : CollisionSide.BOTTOM);
            }
        }

        if (minHoriz < minVert) {
            return (overlapLeft < overlapRight) ? CollisionSide.LEFT : CollisionSide.RIGHT;
        } else {
            return (overlapTop < overlapBottom) ? CollisionSide.TOP : CollisionSide.BOTTOM;
        }
    }

    private void checkCollisionWithWalls() {
        for (Ball b : ballManager.getBalls()) {
            if (b.getX() <= 0) {
                reflectBall(b, 1, 0);
                b.setX(0); 
            } 
            else if (b.getX() + b.getWidth() >= width) {
                reflectBall(b, 1, 0);
                b.setX(width - b.getWidth()); 
            }

            if (b.getY() <= 0) {
                reflectBall(b, 0, 1); 
                b.setY(0); 
            }
        }
    }

    private void checkCollisionWithPaddle() {
        Bounds bPaddle = boundsFrom(paddle);
        
        for (Ball b : ballManager.getBalls()) {
            Bounds bBall = boundsFrom(b); 

            // Chỉ kiểm tra va chạm nếu bóng đang RƠI XUỐNG (dy > 0)
            // và va chạm thực sự xảy ra
            if (b.getDy() > 0 && bBall.intersects(bPaddle)) {
                
                double ballCenterX = b.getX() + b.getWidth() / 2;
                double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
                
                // Tính toán vị trí va chạm trên paddle (-1.0 đến 1.0)
                double diff = ballCenterX - paddleCenterX;
                
                // Chuẩn hóa diff (từ -1 đến 1)
                double newDirX = (diff / (paddle.getWidth() / 2.0));
                
                // Giới hạn giá trị, không để góc quá dốc
                newDirX = Math.max(-1.0, Math.min(1.0, newDirX));

                // Luôn nảy bóng lên
                b.setDirection(newDirX, -Math.abs(b.getDirY())); 
                
                // Đẩy nhẹ bóng lên để tránh va chạm kép/dính
                b.setY(paddle.getY() - b.getHeight() - 0.1); 
                
                // playPaddleSound();
            }
        }
    }

    private void checkCollisionWithBricks() {
        
        Iterator<Ball> ballIterator = ballManager.getBalls().iterator();
        while (ballIterator.hasNext()) {
            Ball b = ballIterator.next(); // Lấy bóng hiện tại

            Bounds bBall = boundsFrom(b);
            boolean reflectedHoriz = false;
            boolean reflectedVert = false;

            Iterator<AbstractBrick> brickIterator = bricks.iterator();
            while (brickIterator.hasNext()) {
                AbstractBrick brick = brickIterator.next();
                
                if (brick.isDestroyed()) {
                    continue;
                }

                Bounds bBrick = boundsFrom(brick);

                CollisionSide side = checkCollision(b, bBall, bBrick);
                
                if (side == CollisionSide.NONE) continue;

                // Thu thập các cạnh
                if (side == CollisionSide.LEFT || side == CollisionSide.RIGHT) reflectedHoriz = true;
                if (side == CollisionSide.TOP || side == CollisionSide.BOTTOM) reflectedVert = true;
                
                // Đẩy bóng 'b' ra
                adjustBallPosition(b, bBall, bBrick, side); 

                // Cập nhật lại bBall sau khi đẩy (quan trọng)
                bBall = boundsFrom(b);

                boolean destroyedNow = brick.takeHit(bricks); 

                if (destroyedNow) {
                    playSound("/audio/hitbrick.wav");
                    effects.add(new ExplosionEffect(brick.getX(), brick.getY(), 30, explosionImage));

                // Thêm xác suất rơi power-up khi gạch bị phá
                double rand = Math.random();
                if (rand < 0.8) {
                    PowerUp powerUp;
                    double powerupRand = Math.random();

                    if (powerupRand < 0.3) {
                        powerUp = new BigPaddlePW(brick.getX(), brick.getY(), paddle);
                    } else if (powerupRand < 0.6) {
                        powerUp = new IncreaseBallSpeed(brick.getX(), brick.getY(), ball);
                    } else if (powerupRand < 0.8) {
                        powerUp = new ExtraLifePowerUp(brick.getX(), brick.getY());
                    } else {
                        powerUp = new MultiBallPowerUp(brick.getX(), brick.getY(), Arrays.asList(ball), ballManager);
                    }
                    powerUpManager.addPowerUp(powerUp);
                }

                score += (brick instanceof StrongBricks) ? 150 : 50;
                score += (brick instanceof NormalBricks) ? 100 : 0;
                double newSpeed = Math.min(420, ball.getSpeed() * 1.02);
                ball.setSpeed(newSpeed);
            } else if (brick instanceof ExplosiveBrick) {
                ((ExplosiveBrick)brick).takeHit(bricks);
                playSound("/audio/expode.ogg");
            }
            } // Kết thúc lặp gạch
            
            // Áp dụng reflect cho bóng 'b'
            if (reflectedHoriz) reflectBall(b, 1, 0);
            if (reflectedVert) reflectBall(b, 0, 1);
            
        } // Kết thúc lặp bóng
    }

    private void checkCollisionWithPowerUps() {
        Bounds bPaddle = boundsFrom(paddle);
        for (PowerUp p : powerUpManager.getPowerUps()) {
            if (p.isCollected()) continue;

            if (bPaddle.intersects(p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
                p.activate();       // Kích hoạt hiệu ứng
                p.setCollected(true); // Đánh dấu là đã nhặt

                if (p instanceof ExtraLifePowerUp) {
                    lives++;
                }

                if (p instanceof MultiBallPowerUp) {
                    executeMultiBall();
                }
            }
        }
    }

    private void executeMultiBall() {
        List<Ball> newBalls = new ArrayList<>();
        
        // Duyệt qua tất cả bóng hiện có để nhân bản
        for (Ball originalBall : ballManager.getBalls()) { 
            
            // Tạo 2 bóng mới dựa trên bóng gốc
            Ball newBall1 = new Ball((int)originalBall.getX(), (int)originalBall.getY(), originalBall.getDiameter() / 2);
            newBall1.setSpeed(originalBall.getSpeed()); 
            newBall1.setDirection(-originalBall.getDirX(), -originalBall.getDirY()); // Hướng 1
            newBall1.launch();
            
            Ball newBall2 = new Ball((int)originalBall.getX(), (int)originalBall.getY(), originalBall.getDiameter() / 2);
            newBall2.setSpeed(originalBall.getSpeed());
            newBall2.setDirection(originalBall.getDirX(), -originalBall.getDirY()); // Hướng 2
            newBall2.launch();

            newBalls.add(newBall1);
            newBalls.add(newBall2);
        }

        // Thêm tất cả bóng mới vào manager
        for (Ball b : newBalls) {
            ballManager.addBall(b);
        }
    }

    private void removeDestroyedBricks() {
        Iterator<AbstractBrick> it = bricks.iterator();
        while (it.hasNext()) {
            AbstractBrick b = it.next();
            if (b.isDestroyed()) it.remove();
        }
        panel.setRefs(ball, paddle, bricks);
    }

    private void reflectBall(Ball b, int flipX, int flipY) {
        double dirX = b.getDirX();
        double dirY = b.getDirY();
        if (flipX != 0) dirX = -dirX;
        if (flipY != 0) dirY = -dirY;
        b.setDirection(dirX, dirY);
    }

    private void checkLoseLife() {
        if (ballManager.getBalls().isEmpty()) { 
            loseLife();
        }
    }

    private void loseLife() {
        lives--;

        if (lives > 0) {
            ball.resetToPaddle(paddle);
            ballManager.addBall(ball);
            System.out.println("Ban con " + lives + " mang.");
        } else {
            gameOver = true;
            loop.stop(); // Dừng vòng lặp
            stopMusic();
//            panel.showGameOver();
            // Nếu bạn có giao diện GameOver thì gọi panel.showGameOver();
            if (onGameOver != null) { //goi ve main bao diem
                onGameOver.accept(this.score);
            }
        }
    }

    private void updateAndRenderEffects() {
        GraphicsContext g = panel.getGraphicsContext();
        Iterator<ExplosionEffect> it = effects.iterator();
        while (it.hasNext()) {
            ExplosionEffect e = it.next();
            e.update();
            e.render(g);
            if (e.isFinished()) it.remove();
        }
    }

    // simple transient effect for destroyed bricks
    private static class ExplosionEffect {
        private final double x;
        private final double y;
        private int timer;
        private final int duration;
        private final Image img;
        private static final double SIZE = 48;

        ExplosionEffect(double x, double y, int duration, Image img) {
            this.x = x;
            this.y = y;
            this.duration = duration;
            this.img = img;
            this.timer = 0;
        }

        void update() {
            timer++;
        }

        void render(GraphicsContext g) {
            double alpha = 1.0 - ((double) timer / Math.max(1, duration));
            g.save();
            g.setGlobalAlpha(Math.max(0, alpha));
            if (img != null) {
                g.drawImage(img, x, y, SIZE, SIZE);
            } else {
                g.setFill(Color.ORANGE);
                g.fillOval(x, y, SIZE, SIZE);
                g.setStroke(Color.YELLOW);
                g.strokeOval(x, y, SIZE, SIZE);
            }
            g.restore();
        }

        boolean isFinished() {
            return timer >= duration;
        }
    }

    private void playSound(String audioPath) {
        try {
            Media sound = new Media(getClass().getResource(audioPath).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(0.8);
            mediaPlayer.play();

            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.dispose());

        } catch (Exception e) {
            System.err.println("Không thể tải hiệu ứng âm thanh: " + audioPath);
        }
    }

    private void loadBackgroundMusic(String audioPath) {
        try {
            java.net.URL resource = getClass().getResource(audioPath);

            if (resource == null) {
                // Ném ngoại lệ rõ ràng nếu không tìm thấy
                throw new java.io.FileNotFoundException("Tài nguyên không tìm thấy trong classpath: " + audioPath);
            }

            Media sound = new Media(getClass().getResource(audioPath).toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(sound);

            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            backgroundMusicPlayer.setVolume(0.2);
            backgroundMedia = new Media(getClass().getResource(audioPath).toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(backgroundMedia);

            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.setVolume(0.2);
        } catch (Exception e) {
        System.err.println("Không thể tải nhạc nền: " + audioPath + ". Lỗi: " + e.getMessage());
    }
    }

    public void startMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    public void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }
}






