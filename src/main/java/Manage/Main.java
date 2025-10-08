package Manage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int W = 800, H = 600;
            JFrame f = new JFrame("Arkanoid (basic)");
            GamePanel panel = new GamePanel(W, H);
            f.setContentPane(panel);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            panel.start();
        });
    }
}
