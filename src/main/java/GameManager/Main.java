package GameManager;

import javax.swing.*;
import java.awt.*;

public class Main {
    private JFrame frame;
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;

    public Main() {
        // Tạo cửa sổ chính
        frame = new JFrame("Golf Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Khởi tạo menu chính
        mainMenuPanel = new MainMenuPanel(
                e -> startGame(),           // Nút Play
                e -> showHighScore(),       // Nút High Score
                e -> quitGame()             // Nút Quit
        );

        // Hiển thị menu đầu tiên
        frame.getContentPane().add(mainMenuPanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Căn giữa
        frame.setVisible(true);
    }

    private void startGame() {
        // Khi bấm "Play" -> chuyển sang GamePanel
        if (gamePanel == null)
            gamePanel = new GamePanel();

        frame.getContentPane().removeAll();   // Xóa panel cũ (menu)
        frame.getContentPane().add(gamePanel);
        frame.revalidate();
        frame.repaint();

        gamePanel.requestFocusInWindow();
        gamePanel.start(); // nếu GamePanel có phương thức start()
    }

    private void showHighScore() {
        JOptionPane.showMessageDialog(frame, "High Scores will be added soon!",
                "High Score", JOptionPane.INFORMATION_MESSAGE);
    }

    private void quitGame() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Do you really want to quit?",
                "Quit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Khởi động giao diện trong EDT
        SwingUtilities.invokeLater(Main::new);
    }
}