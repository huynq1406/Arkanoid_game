package GameManager;

import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.util.function.Consumer;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;

public class MainMenuPane extends StackPane {

    /**
     * Hàm khởi tạo (Constructor) của MainMenuPane.
     * @param playAction Hàm sẽ được gọi khi người dùng nhấn "OK", truyền tên người chơi (String).
     * @param highScoreAction Hàm cho nút HighScore.
     * @param quitAction Hàm cho nút Quit.
     * @param settingAction Hàm cho nút Setting.
     */
    public MainMenuPane(Consumer<String> playAction,
                        EventHandler<ActionEvent> highScoreAction,
                        EventHandler<ActionEvent> settingAction,
                        EventHandler<ActionEvent> quitAction) {

        /**
         * Tải video nền.
         */
        MediaView mediaView = null;
        try {
            String videoPath = getClass().getResource("/menu.mp4").toExternalForm();
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(true);

            mediaView = new MediaView(mediaPlayer);

            mediaView.fitWidthProperty().bind(widthProperty());
            mediaView.fitHeightProperty().bind(heightProperty());
            mediaView.setPreserveRatio(false);
        } catch (Exception ex) {
            System.out.println("Cannot load menu video");
        }

        /**
         * Tạo vbox các button.
         */
        VBox mainButtonsVBox = new VBox(12);
        mainButtonsVBox.setAlignment(Pos.CENTER);
        Button play = createButton("Play", true);
        Button highScore = createButton("Highscore", true);
        Button setting = createButton("Setting", true);
        Button quit = createButton("Quit", true);

        /**
         * Gắn hoạt động các button.
         */
        highScore.setOnAction(highScoreAction);
        setting.setOnAction(settingAction);
        quit.setOnAction(quitAction);
        mainButtonsVBox.getChildren().addAll(play, highScore, setting, quit);

        /**
         * Tao title.
         */
        ImageView titleView = new ImageView();
        try {
            Image logoImg = new Image(getClass().getResourceAsStream("/textTitle.png"));
            titleView.setImage(logoImg);
        }
        catch (Exception e) {
            System.out.println("Không thể tải ảnh /textTitle.png");
        }

        /**
         * Tạo overlay nhập usename.
         */
        VBox nameInputOverlay = new VBox(15);
        nameInputOverlay.setAlignment(Pos.CENTER);
        nameInputOverlay.setPadding(new Insets(15));
        nameInputOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85); -fx-background-radius: 15; -fx-border-width: 2; -fx-border-radius: 15;");
        nameInputOverlay.setMaxSize(200, 200);

        BorderPane headerPane = new BorderPane();
        /**
         * Overlay usename.
         */
        ImageView nameView = new ImageView();
        try {
            Image nameImg = new Image(getClass().getResourceAsStream("/Entername.png"));
            nameView.setImage(nameImg); // Chỉ 'set' ảnh
            nameView.setFitWidth(280);
            nameView.setPreserveRatio(true);
            headerPane.setCenter(nameView);
            BorderPane.setMargin(nameView, new Insets(0,0,0,20));
        }
        catch (Exception e) {
            System.out.println("Không thể tải ảnh /EnterName.png");
            Label nameLabel = new Label("Enter Your Name:");
            nameLabel.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 24px; -fx-font-weight: bold;");
            headerPane.setCenter(nameLabel);
        }

        Button closeButton = new Button();
        try {
            Image xImg = new Image(getClass().getResourceAsStream("/X.png"));
            ImageView xImgView = new ImageView(xImg);
            xImgView.setFitWidth(50);  // Kích thước nút X (nhỏ thôi)
            xImgView.setFitHeight(30);
            xImgView.setPreserveRatio(true);
            closeButton.setGraphic(xImgView);
            closeButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;"); // Nút trong suốt
        } catch (Exception e) {
            closeButton.setText("X"); // Dự phòng nếu lỗi ảnh
            closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 20px; -fx-font-weight: bold;");
        }

        DropShadow xGlow = new DropShadow(10, Color.RED); // Sáng màu đỏ nhẹ
        closeButton.setOnMouseEntered(e -> {
            closeButton.setScaleX(1.2); // Phóng to lên 120%
            closeButton.setScaleY(1.2);
            closeButton.setEffect(xGlow);
            closeButton.setCursor(Cursor.HAND); // Đổi con trỏ thành hình bàn tay
        });
        closeButton.setOnMouseExited(e -> {
            closeButton.setScaleX(1.0);
            closeButton.setScaleY(1.0);
            closeButton.setEffect(null);
            closeButton.setCursor(Cursor.DEFAULT);
        });
        // Hành động nút X: Ẩn overlay
        closeButton.setOnAction(e -> {
            nameInputOverlay.setVisible(false);
            mainButtonsVBox.setVisible(true);
            titleView.setVisible(true);
        });

        headerPane.setRight(closeButton);


        /**
         * Nhap ten.
         */
        TextField nameField = new TextField(); // Đã xóa "Player 1"
        nameField.setMaxWidth(280);
        nameField.setFont(Font.font("Verdana", 18));
        nameField.setAlignment(Pos.CENTER);

        final String NORMAL_STYLE = "-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #E0E0E0; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;";
        final String ERROR_STYLE = "-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: RED; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;";
        nameField.setStyle(NORMAL_STYLE);

        Label errorLabel = new Label(""); // Ban đầu rỗng
        errorLabel.setTextFill(Color.RED); // Màu đỏ
        errorLabel.setFont(Font.font("Verdana", 12)); // Font nhỏ hơn chút
        errorLabel.setVisible(false);

        Button okButton = createButton("OK", false);

        Runnable checkNameAndStart = () -> {
            String name = nameField.getText().trim();
            errorLabel.setVisible(false);
            nameField.setStyle(NORMAL_STYLE);

            if (name.isEmpty()) {
                errorLabel.setText("Name cannot be empty!");
                errorLabel.setVisible(true);
                nameField.setStyle(ERROR_STYLE);
                return;
            }

            // 2. Kiểm tra ký tự đầu viết hoa
            if (!Character.isUpperCase(name.charAt(0))) {
                errorLabel.setText("First letter must be uppercase!");
                errorLabel.setVisible(true);
                nameField.setStyle(ERROR_STYLE);
                return;
            }

            // 3. Kiểm tra có chứa số
            boolean hasDigit = false;
            for (char c : name.toCharArray()) {
                if (Character.isDigit(c)) {
                    hasDigit = true;
                    break;
                }
            }
            if (!hasDigit) {
                errorLabel.setText("Name must contain a number!");
                errorLabel.setVisible(true);
                nameField.setStyle(ERROR_STYLE);
                return;
            }

            // Nếu HỢP LỆ
            playAction.accept(name); // Bắt đầu game
        };


        okButton.setOnAction(e -> checkNameAndStart.run());
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                checkNameAndStart.run();
            } else {
                // Khi người dùng gõ lại, xóa thông báo lỗi và trả lại viền xanh
                errorLabel.setVisible(false);
                nameField.setStyle(NORMAL_STYLE);
            }
        });

        // Thêm tất cả vào Overlay
        nameInputOverlay.getChildren().addAll(headerPane, nameField, errorLabel, okButton);
        nameInputOverlay.setVisible(false);

        /**
         * Action cua playAction.
         */
        play.setOnAction(event -> {
            mainButtonsVBox.setVisible(false);
            titleView.setVisible(false);
            nameInputOverlay.setVisible(true);
            nameField.setText("");
            errorLabel.setVisible(false);
            nameField.setStyle(NORMAL_STYLE); // Reset style khi mở overlay
            nameField.requestFocus();
        });

        /**
         * Sap xep thu tu layer.
         */
        if (mediaView != null) {
            getChildren().addAll(mediaView, titleView, mainButtonsVBox, nameInputOverlay);
        } else {
            getChildren().addAll(titleView, mainButtonsVBox, nameInputOverlay);
            setStyle("-fx-background-color: #505064; ...");
        }

        StackPane.setAlignment(titleView, Pos.TOP_CENTER); //can le title
        StackPane.setMargin(titleView, new Insets(50, 0, 0, 0));

        StackPane.setAlignment(mainButtonsVBox, Pos.BOTTOM_CENTER); //can le mainButton
        StackPane.setMargin(mainButtonsVBox, new Insets(350,0,20,0));

        StackPane.setAlignment(nameInputOverlay, Pos.CENTER); //can le overlay usename
    }

    /**
     * Hàm chung để tạo các nút bằng ảnh
     * @param baseName Tên file ảnh (ví dụ: "Play" -> sẽ tải "/Play.png")
     * @param withGlow true = thêm hiệu ứng phát sáng; false = chỉ phóng to
     */
    private Button createButton(String baseName, boolean withGlow) {
        Button b = new Button();
        try {
            String imagePath = "/" + baseName + ".png";
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(190);
            imgView.setPreserveRatio(true);
            b.setGraphic(imgView);
            b.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        }
        catch (Exception e) {
            System.out.println("Không thể tải ảnh: " + baseName);
            b.setText(baseName.toUpperCase());
        }
        /**
         * Tao effect.
         */
        DropShadow glow = null;
        if (withGlow) {
            glow = new DropShadow();
            glow.setColor(Color.WHITE);
            glow.setRadius(25);
            glow.setSpread(0.25);
        }

        final DropShadow finalGlow = glow;
        //dieu khien chuot
        b.setOnMouseEntered(event -> {
            if (withGlow) {
                b.setEffect(finalGlow);
            }
            b.setScaleX(1.15);
            b.setScaleY(1.15);
        });
        b.setOnMouseExited(event -> {
            if (withGlow) {
                b.setEffect(null);
            }
            b.setScaleX(1.0);
            b.setScaleY(1.0);
        });
        b.setOnMousePressed(event -> {
            b.setScaleX(1.05);
            b.setScaleY(1.05);
        });
        b.setOnMouseReleased(event -> {
            if (b.isHover()) {
                b.setScaleX(1.15);
                b.setScaleY(1.15);
            } else {
                b.setScaleX(1.0);
                b.setScaleY(1.0);
            }
        });
        return b;
    }
}