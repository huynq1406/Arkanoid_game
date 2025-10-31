package Levels;

import java.util.ArrayList;
import java.util.List;

import Entities.BrickFactory;
import Entities.bricks.AbstractBrick;
import javafx.scene.canvas.GraphicsContext;


public class TextMapLevel extends BaseLevel {

    private final String resourcePath; // ví dụ "levels/Map.txt"
    private final int levelIndex;      // ví dụ 1

    // layout mặc định
    private int topPadding = 60;
    private int brickH = 24;

    // dữ liệu level
    private List<AbstractBrick> builtBricks = new ArrayList<>();
    private int initialLives = 3;
    private float ballSpeed = 5.0f;
    private int paddleWidth = 100;

    public TextMapLevel(String resourcePath, int levelIndex, GraphicsContext gc) {
        super(gc, 0);
        this.resourcePath = resourcePath; // THÊM /
        this.levelIndex   = levelIndex;
    }

    public void buildFromMap(int panelWidth) {
        List<String> rows = LevelLoader.load(resourcePath, levelIndex);

        // 2) Tính kích thước gạch (auto-fit theo panel)
        int cols = rows.stream().mapToInt(String::length).max().orElse(0);
        int brickW = (cols > 0) ? Math.max(8, panelWidth / cols) : 40;

        // 3) Tạo bricks theo ký hiệu
        builtBricks = new ArrayList<>();
        int y = topPadding;
        for (String row : rows) {
            int x = 0;
            for (int i = 0; i < row.length(); i++) {
                char ch = row.charAt(i);
                AbstractBrick b = BrickFactory.fromSymbol(ch, x, y, brickW, brickH);
                if (b != null) builtBricks.add(b);
                x += brickW;
            }
            y += brickH;
        }

        // 4) (tuỳ chọn) set tham số level; sau này có thể đọc từ header map
        this.initialLives = 3;
        this.ballSpeed    = 5.0f;
        this.paddleWidth  = 100;
    }

    public List<AbstractBrick> getBricks() {
        return builtBricks;
    }

    public int getInitialLives() {
        return initialLives;
    }

    public float getBallSpeed() {
        return ballSpeed;
    }

    public int getPaddleWidth() {
        return paddleWidth;
    }

    public TextMapLevel withTopPadding(int topPadding) { this.topPadding = topPadding; return this; }
    public TextMapLevel withBrickHeight(int brickH)     { this.brickH = brickH;         return this; }
}
