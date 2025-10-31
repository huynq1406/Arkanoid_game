package GameManager;

import Entities.Ball;
import Entities.Paddle;
import Entities.bricks.AbstractBrick;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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

    /** Return canvas GraphicsContext so GameManager can draw HUD/overlays */
    public GraphicsContext getGraphicsContext() {
        return canvas.getGraphicsContext2D();
    }

    /** JavaFX: gọi mỗi frame để vẽ (thay paintComponent) */
    public void render() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // nền
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // bricks
        if (bricks != null) {
            for (AbstractBrick b : bricks) drawBrick(g, b);
        }

        // paddle & ball (nếu class của bạn có draw(g), hãy gọi trực tiếp)
        if (paddle != null) drawPaddle(g, paddle);
        if (ball   != null) {
            // keep existing API: ball.drawBall(g, ball) or adapt to ball.draw(g)
            try {
                ball.drawBall(g, ball);
            } catch (Throwable t) {
                // fallback basic draw using position/size if drawBall not present
                g.setFill(Color.WHITE);
                g.fillOval(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
            }
        }
    }

    private void drawBrick(GraphicsContext g, AbstractBrick b) {
        // TODO: nếu Brick có sprite/skin riêng, thay bằng b.draw(g)
        g.setFill(b.isDestroyed() ? Color.web("#3C3C3C") : Color.web("#4FC3F7"));
        g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        g.setStroke(Color.DARKGRAY);
        g.strokeRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
    }

    private void drawPaddle(GraphicsContext g, Paddle p) {
        // TODO: nếu Paddle đã có draw(g), gọi p.draw(g);
        g.setFill(Color.web("#FFEE58"));
        g.fillRoundRect(p.getX(), p.getY(), p.getWidth(), p.getHeight(), 10, 10);
    }
}
