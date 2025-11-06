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
import javafx.scene.text.Font;
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

    public GameManager(int width, int height, GamePanel panel, Ball ball, Paddle paddle, String playerName, Consumer<Integer> onGameOver, GameHUD gameHUD) {
        this.width = width;
        this.height = height;
        this.panel  = panel;
        this.ball   = ball;
        ballManager.addBall(this.ball);
        this.paddle = paddle;
        this.playerName = playerName;
        this.onGameOver = onGameOver;
        this.gameHUD = gameHUD;
        ballManager.addBall(this.ball);

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

    public void buildLevel() {
        GraphicsContext gc = panel.getGraphicsContext();
        currentLevel = new TextMapLevel("levels/Map.txt", levelIndex, gc);
        try {
            currentLevel.buildFromMap(GamePanel.WIDTH);
            bricks = new ArrayList<>(currentLevel.getBricks());
            panel.setRefs(ball, paddle, bricks);
            powerUpManager = new PowerUpManager();
            ball.resetToPaddle(paddle);  // Reset ball khi bắt đầu level mới
            System.out.println("Loaded level " + levelIndex);
        } catch (IllegalArgumentException e) {
            // Xử lý nếu không tìm thấy level (hết level)
            gameOver = true;
            loop.stop();
//            panel.showGameOver();  // Hoặc hiển thị "You Win!" nếu muốn
            System.out.println("No more levels: " + e.getMessage());
        }
    }

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
        if (!ball.isLaunched()) {
            ball.launch();
        }
    }


    private void tick(double dt) {
        if (gameOver) return;

        ballManager.updateAll(dt, paddle);
        paddle.update(dt);
        powerUpManager.updateAll();

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
        score += 1000;  // Bonus điểm khi hoàn thành level (tùy chỉnh)
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

    private void adjustBallPosition(Bounds bBall, Bounds bBrick, CollisionSide side) {
        double epsilon = 1.0;  // Khoảng cách nhỏ để push out
        switch (side) {
            case LEFT:   ball.setX((int)(ball.getX() - ((bBall.getMaxX() - bBrick.getMinX()) - epsilon))); break;
            case RIGHT:  ball.setX((int)(ball.getX() + (bBrick.getMaxX() - bBall.getMinX()) + epsilon)); break;
            case TOP:    ball.setY((int)(ball.getY() - (bBall.getMaxY() - bBrick.getMinY()) - epsilon)); break;
            case BOTTOM: ball.setY((int)(ball.getY() + (bBrick.getMaxY() - bBall.getMinY()) + epsilon)); break;
            default: break;
        }
    }

    private CollisionSide checkCollision(Bounds a, Bounds b) {
        if (!a.intersects(b)) return CollisionSide.NONE;

        double aLeft = a.getMinX(), aRight = a.getMaxX(), aTop = a.getMinY(), aBottom = a.getMaxY();
        double bLeft = b.getMinX(), bRight = b.getMaxX(), bTop = b.getMinY(), bBottom = b.getMaxY();

        double overlapLeft   = aRight - bLeft;   // a chèn vào b từ trái
        double overlapRight  = bRight - aLeft;   // a chèn vào b từ phải
        double overlapTop    = aBottom - bTop;   // a chèn vào b từ trên
        double overlapBottom = bBottom - aTop;   // a chèn vào b từ dưới

        double minHoriz = Math.min(overlapLeft, overlapRight);
        double minVert  = Math.min(overlapTop, overlapBottom);

        double threshold = 1.0;  // Nếu gần bằng, coi như góc
        if (Math.abs(minHoriz - minVert) < threshold) {
            // Góc: Trả về side dựa trên velocity (hướng bóng đang đi)
            if (ball.getDx() != 0 && ball.getDy() != 0) {
                // Có thể xử lý cả horiz và vert ở loop ngoài
                return (Math.abs(ball.getDx()) > Math.abs(ball.getDy())) ?
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
        Bounds br = boundsFrom(ball);
        if (br.getMinX() <= 0 && ball.getDx() < 0)                 reflectBall(1, 0);  // left wall
        if (br.getMaxX() >= width && ball.getDx() > 0)             reflectBall(-1, 0); // right wall
        if (br.getMinY() <= 0 && ball.getDy() < 0)                 reflectBall(0, 1);  // ceiling
    }

    private void checkCollisionWithPaddle() {
        Bounds bBall = boundsFrom(ball);
        Bounds bPaddle = boundsFrom(paddle);
        if (bBall.intersects(bPaddle) && ball.getDy() > 0) {
            reflectBall(0, -1); // bounce up
            double dirX = ball.getDirX(), dirY = ball.getDirY();
            double minAbsY = 0.35;
            if (Math.abs(dirY) < minAbsY) {
                dirY = -minAbsY;
                ball.setDirection(dirX, dirY);
            }
        }
    }

    private void checkCollisionWithBricks() {
        Bounds bBall = boundsFrom(ball);
        boolean reflectedHoriz = false;
        boolean reflectedVert = false;

        for (AbstractBrick brick : bricks) {
            if (brick.isDestroyed()) {
                continue;
            }
            CollisionSide side = checkCollision(bBall, boundsFrom(brick));
            if (side == CollisionSide.NONE) continue;

            // Collect sides để reflect sau (tránh flip nhiều lần)
            if (side == CollisionSide.LEFT || side == CollisionSide.RIGHT) reflectedHoriz = true;
            if (side == CollisionSide.TOP || side == CollisionSide.BOTTOM) reflectedVert = true;
            // Adjust vị trí bóng ra khỏi brick để tránh kẹt
            adjustBallPosition(bBall, boundsFrom(brick), side);

            boolean destroyedNow = brick.takeHit(bricks);

            if (destroyedNow) {
                // spawn explosion effect at brick's position
                playSound("/audio/hitbrick.wav");
                effects.add(new ExplosionEffect(brick.getX(), brick.getY(), 30, explosionImage));

                // Thêm xác suất rơi power-up khi gạch bị phá
                double rand = Math.random();
                if (rand < 0.3) { // 30% chance to drop a power-up
                    PowerUp powerUp;
                    double powerupRand = Math.random();

                    if (powerupRand < 0.4) {
                        powerUp = new BigPaddlePW(brick.getX(), brick.getY(), paddle);
                    } else if (powerupRand < 0.7) {
                        powerUp = new IncreaseBallSpeed(brick.getX(), brick.getY(), ball);
                    } else if (powerupRand < 0.9) {
                        powerUp = new ExtraLifePowerUp(brick.getX(), brick.getY());
                    } else {
                        powerUp = new MultiBallPowerUp(brick.getX(), brick.getY(), Arrays.asList(ball), ballManager);
                    }
                    powerUpManager.addPowerUp(powerUp);
                }

                score += (brick instanceof StrongBricks) ? 150 : 50;
                score += (brick instanceof NormalBricks) ? 100 : 0;

                if (gameHUD != null) {
                    gameHUD.updateScore(score);
                }

                double newSpeed = Math.min(420, ball.getSpeed() * 1.02);
                ball.setSpeed(newSpeed);
            } else if (brick instanceof ExplosiveBrick) {
                playSound("/audio/explode.ogg");
                ((ExplosiveBrick)brick).takeHit(bricks);
            }
        }
        // Apply reflect chỉ 1 lần sau loop
        if (reflectedHoriz) reflectBall(ball.getDx() > 0 ? -1 : 1, 0);  // Flip x dựa trên dir hiện tại
        if (reflectedVert) reflectBall(0, ball.getDy() > 0 ? -1 : 1);// Flip y
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
                    gameHUD.updateLives(lives);
                }
            }
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

    private void reflectBall(int flipX, int flipY) {
        double dirX = ball.getDirX();
        double dirY = ball.getDirY();
        if (flipX != 0) dirX = -dirX;
        if (flipY != 0) dirY = -dirY;
        ball.setDirection(dirX, dirY);
    }

    private void checkLoseLife() {
        boolean lost = ballManager.getBalls().stream()
                .anyMatch(b -> b.getY() > height);

        if (lost) {
            loseLife();
            resetBallsToPaddle();
        }
    }

    private void resetBallsToPaddle() {
        for (Ball b : ballManager.getBalls()) {
            b.resetToPaddle(paddle);
        }
    }

    private void loseLife() {
        lives--;

        if (gameHUD != null) {
            gameHUD.updateLives(lives);
        }

        if (lives > 0) {
            ball.resetToPaddle(paddle);
            System.out.println("Bạn còn " + lives + " mạng.");
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

            backgroundMusicPlayer.setVolume(0.5);
            backgroundMedia = new Media(getClass().getResource(audioPath).toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(backgroundMedia);

            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.setVolume(0.5);
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






