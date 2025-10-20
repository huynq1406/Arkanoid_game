package GameManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
    private static JFrame frame;
    private static GamePanel gamePanel;
    private static MainMenuPanel mainMenuPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Arkanoid Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            // Tạo các panel ngay từ đầu
            gamePanel = new GamePanel();

            // Tạo MainMenuPanel và truyền vào các hành động (action)
            mainMenuPanel = new MainMenuPanel(
                e -> showGame(),          // Hành động cho nút Play
                e -> showHighScores(),    // Hành động cho nút High Score
                e -> System.exit(0)  // Hành động cho nút Quit
            );
            
            // Bắt đầu bằng việc hiển thị menu
            showMenu();

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void showMenu() {
        // Dừng game nếu nó đang chạy
        if (gamePanel != null) {
            gamePanel.stop();
        }
        // Hiển thị menu
        switchPanel(mainMenuPanel);
    }

    public static void showGame() {
        // Hiển thị game panel và bắt đầu game
        switchPanel(gamePanel);
        gamePanel.start(); // Gọi hàm start để khởi động Timer
    }

    public static void showHighScores() {
        // Tạm thời chỉ in ra console, bạn có thể tạo một HighScorePanel tương tự
        System.out.println("Showing High Scores (chưa làm)...");
    }

    // Helper method để chuyển đổi giữa các panel
    private static void switchPanel(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
        panel.requestFocusInWindow();
    }
}