package GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(ActionListener playAction, ActionListener highScoreAction, ActionListener quitAction) {
        // DÒNG CODE MỚI: Thiết lập kích thước cho panel
        setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
        
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Tạo các nút
        JButton playButton = createMenuButton("Play");
        JButton highScoreButton = createMenuButton("High Score");
        JButton quitButton = createMenuButton("Quit");

        // Gán hành động
        playButton.addActionListener(playAction);
        highScoreButton.addActionListener(highScoreAction);
        quitButton.addActionListener(quitAction);

        // Thêm nút vào panel
        add(playButton, gbc);
        add(highScoreButton, gbc);
        add(quitButton, gbc);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Verdana", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(80, 80, 100));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 50));
        return button;
    }
}