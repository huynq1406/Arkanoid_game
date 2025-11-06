package GameManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameHUD extends HBox {
    private Label scoreValueLabel;
    private Label livesValueLabel;
    private Label levelValueLabel;
    private Button pauseButton;
    private HBox powerUpDisplayBox;

    public GameHUD(Runnable onPauseAction) {
        // 1. Cấu hình chung cho thanh HUD
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setSpacing(15); // Khoảng cách giữa các phần tử
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Nền bán trong suốt
        this.setMaxHeight(60);

        // 2. Tạo các cặp Label (Ảnh nhãn + Giá trị số)
        HBox scoreBox = createStatBox("score_label.png", scoreValueLabel = createValueLabel("0"));
        HBox livesBox = createStatBox("lives_label.png", livesValueLabel = createValueLabel("3"));
        HBox levelBox = createStatBox("level_label.png", levelValueLabel = createValueLabel("1"));

        // 3. Tạo nút Pause bằng ảnh
        pauseButton = createPauseButton("pause_button.png", onPauseAction);

        // 4. Khu vực hiển thị Power-up (để trống, dùng sau)
        powerUpDisplayBox = new HBox(5);
        powerUpDisplayBox.setAlignment(Pos.CENTER);

        // 5. Tạo các khoảng trống co giãn để căn chỉnh
        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        // 6. Thêm tất cả vào HUD
        // Thứ tự: [Score] [Lives] --spacer-- [Level] [PowerUps] --spacer-- [Pause]
        this.getChildren().addAll(scoreBox, livesBox, spacerLeft, levelBox, powerUpDisplayBox, spacerRight, pauseButton);
    }

    // Helper tạo HBox chứa: [Ảnh nhãn] + [Text giá trị]
    private HBox createStatBox(String imageName, Label valueLabel) {
        HBox box = new HBox(5); // Khoảng cách giữa ảnh và số là 5px
        box.setAlignment(Pos.CENTER_LEFT);

        ImageView imageView = new ImageView();
        try {
            // Tải ảnh từ resources. Đảm bảo đường dẫn đúng với project của bạn
            Image img = new Image(getClass().getResourceAsStream("/" + imageName));
            imageView.setImage(img);
            imageView.setFitHeight(30); // Điều chỉnh chiều cao ảnh cho phù hợp
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            // Fallback nếu không tìm thấy ảnh: dùng text tạm
            box.getChildren().add(new Label(imageName.replace(".png", "").toUpperCase() + ": "));
            System.err.println("Could not load HUD image: " + imageName);
        }

        if (imageView.getImage() != null) {
            box.getChildren().add(imageView);
        }
        box.getChildren().add(valueLabel);
        return box;
    }

    // Helper tạo Label hiển thị giá trị số
    private Label createValueLabel(String initialValue) {
        Label label = new Label(initialValue);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        // Hiệu ứng bóng đổ nhẹ giúp số dễ đọc hơn trên nền game
        label.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 0);");
        return label;
    }

    // Helper tạo nút Pause có hình ảnh
    private Button createPauseButton(String imageName, Runnable action) {
        Button btn = new Button();
        try {
            ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream("/" + imageName)));
            imgView.setFitHeight(40); // Kích thước nút pause
            imgView.setPreserveRatio(true);
            btn.setGraphic(imgView);
            btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;"); // Nền trong suốt
        } catch (Exception e) {
            btn.setText("||"); // Fallback text
            btn.setStyle("-fx-font-size: 20px; -fx-background-color: #ff9900; -fx-text-fill: white;");
        }
        btn.setFocusTraversable(false); // Không để nút này nhận focus bàn phím
        btn.setOnAction(e -> action.run());
        return btn;
    }

    // --- Các phương thức public để cập nhật từ bên ngoài ---
    public void updateScore(int score) {
        scoreValueLabel.setText(String.valueOf(score));
    }

    public void updateLives(int lives) {
        livesValueLabel.setText(String.valueOf(lives));
    }

    public void updateLevel(int level) {
        levelValueLabel.setText(String.valueOf(level));
    }

    public HBox getPowerUpDisplayBox() {
        return powerUpDisplayBox;
    }
}