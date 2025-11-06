package GameManager;

import Entities.Ball;
import Entities.Paddle;
import Entities.bricks.AbstractBrick;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GamePanel extends Pane {
    public static final int WIDTH  = 800;   // giữ hằng như Swing cũ
    public static final int HEIGHT = 600;

    private final Canvas canvas = new Canvas(WIDTH, HEIGHT);

    private Ball ball;
    private Paddle paddle;
    private GameManager gameManager;
    private List<AbstractBrick> bricks = new ArrayList<>();

    public GamePanel() {
        setPrefSize(WIDTH, HEIGHT);
        getChildren().add(canvas);
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

    public void setRefs(Ball ball, Paddle paddle, List<AbstractBrick> bricks) {
        this.ball   = ball;
        this.paddle = paddle;
        this.bricks = (bricks == null) ? new ArrayList<>() : bricks;
    }

    public void setGameManager(GameManager gm) {
        this.gameManager = gm;
    }

    public void showGameOver() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // Vẽ nền mờ
        g.setFill(new Color(0, 0, 0, 0.7));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Hiển thị chữ GAME OVER
        g.setFill(Color.RED);
        g.setFont(Font.font("Arial", 60));
        g.fillText("GAME OVER", WIDTH / 2.0 - 170, HEIGHT / 2.0 - 20);

        // Thêm gợi ý
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Arial", 24));
        g.fillText("Nhấn ESC để thoát", WIDTH / 2.0 - 110, HEIGHT / 2.0 + 40);
    }

    /** Return canvas GraphicsContext so GameManager can draw HUD/overlays */
    public GraphicsContext getGraphicsContext() {
        return canvas.getGraphicsContext2D();
    }


    public void render() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // nền
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // bricks
        if (bricks != null) {
            for (AbstractBrick b : bricks) {
                b.render(g);
            }
        }

        // paddle & ball (nếu class của bạn có draw(g), hãy gọi trực tiếp)
        if (paddle != null) paddle.render(g);
        if (ball   != null) {
            // keep existing API: ball.drawBall(g, ball) or adapt to ball.draw(g)
            try {
                ball.render(g);
            } catch (Throwable t) {
                // fallback basic draw using position/size if drawBall not present
                g.setFill(Color.WHITE);
                g.fillOval(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
            }
        }
    }
}
