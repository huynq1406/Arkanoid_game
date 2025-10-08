package Manage;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable {

    private final GameManager game;
    private Thread loopThread;
    private boolean running = false;

    public GamePanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocusInWindow();

        game = new GameManager(width, height);
        initInput();
    }

    private void initInput() {
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT  -> game.leftPressed  = true;
                    case KeyEvent.VK_RIGHT -> game.rightPressed = true;
                    case KeyEvent.VK_SPACE -> game.spacePressed = true;
                }
            }
            @Override public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT  -> game.leftPressed  = false;
                    case KeyEvent.VK_RIGHT -> game.rightPressed = false;
                    case KeyEvent.VK_SPACE -> game.spacePressed = false;
                }
            }
        });
    }

    public void start() {
        if (running) return;
        running = true;
        loopThread = new Thread(this, "GameLoop");
        loopThread.start();
    }

    @Override
    public void run() {
        final int fps = 60;
        final long frameTime = 1000L / fps;

        while (running) {
            long start = System.currentTimeMillis();

            game.update();
            repaint();

            long elapsed = System.currentTimeMillis() - start;
            long sleep = frameTime - elapsed;
            if (sleep < 2) sleep = 2;  // tránh sleep âm
            try { Thread.sleep(sleep); } catch (InterruptedException ignored) {}
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }
}
