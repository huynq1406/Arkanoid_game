package GameManager;

import javafx.scene.effect.DropShadow;
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
                        EventHandler<ActionEvent> quitAction,
                        EventHandler<ActionEvent> settingAction) {

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
        nameInputOverlay.setPadding(new Insets(25));
        nameInputOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); -fx-background-radius: 10;");
        nameInputOverlay.setMaxSize(200, 200);

        /**
         * Overlay usename.
         */
        ImageView nameView = new ImageView();
        try {
            Image nameImg = new Image(getClass().getResourceAsStream("/Entername.png"));
            nameView.setImage(nameImg); // Chỉ 'set' ảnh
            nameView.setFitWidth(280);
            nameView.setPreserveRatio(true);
        }
        catch (Exception e) {
            System.out.println("Không thể tải ảnh /EnterName.png");
            Label nameLabel = new Label("Enter Your Name:");
            nameLabel.setStyle("-fx-text-fill: white;");
            nameLabel.setFont(Font.font("Verdana", 20));
            nameInputOverlay.getChildren().add(nameLabel);
        }

        /**
         * Nhap ten.
         */
        TextField nameField = new TextField(); // Đã xóa "Player 1"
        nameField.setMaxWidth(280);
        nameField.setFont(Font.font("Verdana", 18));
        nameField.setAlignment(Pos.CENTER);

        /**
         * Create button okButton big.
         */
        Button okButton = createButton("OK", false);
        if (nameView.getImage() != null) {
            nameInputOverlay.getChildren().add(nameView);
        }
        nameInputOverlay.getChildren().addAll(nameField, okButton);
        nameInputOverlay.setVisible(false);

        /**
         * Action cua playAaction.
         */
        play.setOnAction(event -> {
            mainButtonsVBox.setVisible(false);
            titleView.setVisible(false);
            nameInputOverlay.setVisible(true);
            nameField.requestFocus();
        });

        /**
         * Khi an okButton or Enter.
         */
        Runnable startGameAction = () -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                playAction.accept(playerName);
            } else {
                nameField.setStyle("-fx-border-color: red; -fx-border-width: 2;"); // Báo lỗi
            }
        };
        okButton.setOnAction(e -> startGameAction.run());
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                startGameAction.run();
            } else {
                nameField.setStyle(null);
            }
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