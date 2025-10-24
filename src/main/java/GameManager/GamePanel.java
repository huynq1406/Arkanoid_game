package GameManager;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Panel chính của game.
 * - Điều khiển chuột: move -> paddle theo chuột.
 * - Click: nếu đang chơi -> bắn; nếu đang dừng (win/lose) -> restart.
 * - Timer 60 FPS.
 */
public class GamePanel extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final GameManager game;
    private long lastTickNanos;
    private final Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocusInWindow();

        game = new GameManager(WIDTH, HEIGHT);

        // Move/drag chuột -> paddle bám theo
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                game.movePaddleToMouseX(e.getX());
                repaint();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                game.movePaddleToMouseX(e.getX());
                repaint();
            }
        });

        // Click: playing -> launch, stopped -> restart
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!game.isRunning()) game.restart();
                else game.launchBall();
                repaint();
            }
        });

        lastTickNanos = System.nanoTime();
        timer = new Timer(1000 / 60, e -> tick());
    }

    /** Bắt đầu vòng lặp game (Timer). */
    public void start() {
        timer.start();
    }

    private void tick() {
        long now = System.nanoTime();
        double dt = (now - lastTickNanos) / 1_000_000_000.0;
        if (dt > 0.05) dt = 0.05; // clamp khi bị lag
        lastTickNanos = now;

        game.update(dt);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public void stop() {
        timer.stop();
    }
}
