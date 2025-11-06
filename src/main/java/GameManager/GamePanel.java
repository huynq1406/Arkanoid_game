package GameManager;

import Entities.Ball;
import Entities.Paddle;
import Entities.bricks.AbstractBrick;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GamePanel extends StackPane {
    public static final int WIDTH  = 800;   // giữ hằng như Swing cũ
    public static final int HEIGHT = 600;

    private final Canvas canvas; // Bỏ khởi tạo ngay tại đây để code gọn hơn trong constructor
    private GameHUD gameHUD;

    private Ball ball;
    private Paddle paddle;
    private GameManager gameManager;
    private List<AbstractBrick> bricks = new ArrayList<>();

    public GamePanel() {
        setPrefSize(WIDTH, HEIGHT);
        this.canvas = new Canvas(WIDTH, HEIGHT);
        this.setAlignment(Pos.TOP_LEFT);
        getChildren().add(canvas);
        canvas.setFocusTraversable(true); // Cho phép canvas nhận focus nếu cần
        canvas.setOnMouseMoved(e -> {
            if (gameManager != null) {
                gameManager.onMouseMove(e.getX());
            }
        });
        canvas.setOnMousePressed(e -> {
            if (gameManager != null) {
                gameManager.onMousePress();
            }
        });
    }

    public void initHUD(Runnable onPauseAction) {
        this.gameHUD = new GameHUD(onPauseAction);
        getChildren().clear();
        getChildren().addAll(canvas, gameHUD);
        StackPane.setAlignment(gameHUD, Pos.TOP_CENTER);
    }

    public GameHUD getHud() {
        return gameHUD;
    }

    public void setRefs(Ball ball, Paddle paddle, List<AbstractBrick> bricks) {
        this.ball   = ball;
        this.paddle = paddle;
        this.bricks = (bricks == null) ? new ArrayList<>() : bricks;
    }

    public void setGameManager(GameManager gm) {
        this.gameManager = gm;
    }

//    public void showGameOver() {
//        GraphicsContext g = canvas.getGraphicsContext2D();
//
//        // Vẽ nền mờ
//        g.setFill(new Color(0, 0, 0, 0.7));
//        g.fillRect(0, 0, WIDTH, HEIGHT);
//
//        // Hiển thị chữ GAME OVER
//        g.setFill(Color.RED);
//        g.setFont(Font.font("Arial", 60));
//        g.fillText("GAME OVER", WIDTH / 2.0 - 170, HEIGHT / 2.0 - 20);
//
//        // Thêm gợi ý
//        g.setFill(Color.WHITE);
//        g.setFont(Font.font("Arial", 24));
//        g.fillText("Nhấn ESC để thoát", WIDTH / 2.0 - 110, HEIGHT / 2.0 + 40);
//    }

    public GraphicsContext getGraphicsContext() {
        return canvas.getGraphicsContext2D();
    }


    public void render() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        if (bricks != null) {
            for (AbstractBrick b : bricks) {
                b.render(g);
            }
        }
        if (paddle != null) paddle.render(g);
        if (ball != null) {
            try {
                ball.render(g);
            } catch (Throwable t) {
                g.setFill(Color.WHITE);
                g.fillOval(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
            }
        }
    }
}
