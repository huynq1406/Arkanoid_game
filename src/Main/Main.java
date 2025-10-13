package Main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import GameManager.GamePanel;

/** Điểm vào chương trình. Chạy để chơi ngay. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Arkanoid - Mouse Control (v3)");
            GamePanel panel = new GamePanel();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.start();
        });
    }
}
