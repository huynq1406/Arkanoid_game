package GameManager;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MainMenuPane extends StackPane {
    private final VBox mainButtonsVbox;
    private final ImageView titleView;
    private VBox nameOverlay;
    private VBox highScoreOverlay;
    private final Button highScoreButton;

    private MediaView createBackgroundVideo() {
        try {
            String videoPath = getClass().getResource("/menu.mp4").toExternalForm();
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(true);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.fitWidthProperty().bind(widthProperty());
            mediaView.fitHeightProperty().bind(heightProperty());
            mediaView.setPreserveRatio(false);
            return mediaView;
        } catch (Exception ex) {
            System.out.println("Không thể tải video menu: " + ex.getMessage());
            return null;
        }
    }

    private ImageView createTitileView() {
        ImageView view = new ImageView();
        try {
            view.setImage(new Image(getClass().getResourceAsStream("/textTitle.png")));
        } catch (Exception e) {
            System.out.println("Không thể load ảnh");
        }
        return view;
    }

    private Button createMenuButton(String baseName, boolean withGlow) {
        Button button = new Button();
        try {
            String imagePath = "/" + baseName + ".png";
            ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
            imgView.setFitWidth(190);
            imgView.setPreserveRatio(true);
            button.setGraphic(imgView);
            button.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        } catch (Exception e) {
            button.setText(baseName.toUpperCase());
            button.setFont(Font.font("Verdana", 20));
        }
        applyButtonEffects(button, withGlow);
        return button;
    }

    private void createNameOverlay(Consumer<String> playAction) {
        nameOverlay = createBaseOverlay(300, 250);

        BorderPane header = new BorderPane();
        ImageView nameTitle = new ImageView();
        try {
            nameTitle.setImage(new Image(getClass().getResourceAsStream("/Entername.png")));
            nameTitle.setFitWidth(250);
            nameTitle.setPreserveRatio(true);
            header.setCenter(nameTitle);
        } catch (Exception e) {
            Label lbl = new Label("ENTER YOUR NAME");
            lbl.setTextFill(Color.CYAN);
            lbl.setFont(Font.font("Verdana", 24));
            header.setCenter(lbl);
        }
        header.setRight(createCloseButton(nameOverlay)); //đang set ô nhập tên been phải closeButton
        //Nhap ten
        TextField nameField = new TextField();
        nameField.setMaxWidth(250);
        nameField.setFont(Font.font("Verdana", 16));
        nameField.setAlignment(Pos.CENTER);
        styleTextField(nameField, false);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

        Button okButton = createMenuButton("OK", false);
        Runnable submitAction = () -> {
            String name = nameField.getText().trim();
            if (validateName(name, errorLabel, nameField)) {
                playAction.accept(name);
            }
        };
        okButton.setOnAction(e -> submitAction.run());
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) submitAction.run();
            else {
                errorLabel.setVisible(false);
                styleTextField(nameField, false);
            }
        });
        nameOverlay.getChildren().addAll(header, nameField, errorLabel, okButton);
    }

    private ListView<String> highScoreListView;

    private void createHighScoreOverlay(Supplier<List<String>> highScoreAction) {
        highScoreOverlay = createBaseOverlay(400, 500);
        BorderPane header = new BorderPane();

        ImageView imgHighScore = new ImageView();
        try {
            Image img = new Image(getClass().getResourceAsStream("/Highscore.png"));
            imgHighScore.setImage(img);
            imgHighScore.setFitWidth(250);
            imgHighScore.setPreserveRatio(true);

            DropShadow blueGlow = new DropShadow();
            blueGlow.setColor(Color.CYAN);
            blueGlow.setRadius(25);
            blueGlow.setSpread(0.4);
            imgHighScore.setEffect(blueGlow);

            header.setCenter(imgHighScore);
        } catch (Exception e) {
            Label fallbackTitle = new Label("HIGH SCORES");
            fallbackTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
            fallbackTitle.setTextFill(Color.GOLD);
            fallbackTitle.setEffect(new DropShadow(10, Color.CYAN));
            header.setCenter(fallbackTitle);
        }
        header.setRight(createCloseButton(highScoreOverlay)); //đang đặt phía bên trái closeButton
        highScoreListView = new ListView<>();
        highScoreListView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent; -fx-font-size: 18px; -fx-font-family: 'Monospaced'; -fx-text-fill: #FFFFFF;");

        highScoreOverlay.getChildren().addAll(header, highScoreListView);
    }

    private VBox createBaseOverlay(double width, double height) {
        VBox overlay = new VBox(15);
        overlay.setAlignment(Pos.TOP_CENTER);
        overlay.setPadding(new Insets(20));
        overlay.setMaxSize(width, height);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85); -fx-background-radius: 20; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-width: 2; -fx-border-radius: 20;");
        overlay.setVisible(false);
        StackPane.setAlignment(overlay, Pos.CENTER);
        return overlay;
    }

    private Button createCloseButton(VBox targetOverlay) {
        Button xButton = new Button();
        try {
            ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream("/X.png")));
            imgView.setFitWidth(40);
            imgView.setPreserveRatio(true);
            xButton.setGraphic(imgView);
            xButton.setStyle("-fx-background-color: transparent;");
        } catch (Exception e) {
            xButton.setText("X");
            xButton.setTextFill(Color.RED);
            xButton.setFont(Font.font(20));
            xButton.setStyle("-fx-background-color: transparent;");
        }
        xButton.setOnMouseEntered(e -> {
            xButton.setScaleX(1.2);
            xButton.setScaleY(1.2);
            xButton.setCursor(Cursor.HAND);
        });
        xButton.setOnMouseExited(e -> {
            xButton.setScaleX(1.0);
            xButton.setScaleY(1.0);
            xButton.setCursor(Cursor.DEFAULT);
        });
        xButton.setOnAction(e -> hideOverlay(targetOverlay));
        return xButton;
    }

    private void showOverlay(VBox overlay) {
        mainButtonsVbox.setVisible(false);
        titleView.setVisible(false);
        overlay.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(300), overlay);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        if (overlay == nameOverlay) {
            nameOverlay.getChildren().stream()
                    .filter(node -> node instanceof TextField)
                    .findFirst()
                    .ifPresent(node -> ((TextField) node).requestFocus());
        }
    }

    private void hideOverlay(VBox overlay) {
        overlay.setVisible(false);
        mainButtonsVbox.setVisible(true);
        titleView.setVisible(true);
    }

    private void updateHighScoreList(Supplier<List<String>> supplier) {
        highScoreListView.getItems().clear();
        List<String> scores = supplier.get();
        if (scores.isEmpty()) {
            highScoreListView.getItems().add("No high scores yet!");
        } else {
            highScoreListView.getItems().addAll(scores);
        }
    }

    private void applyButtonEffects(Button b, boolean withGlow) {
        DropShadow glow = withGlow ? new DropShadow(20, Color.WHITE) : null;
        b.setOnMouseEntered(e -> {
            if (withGlow) b.setEffect(glow);
            b.setScaleX(1.1);
            b.setScaleY(1.1);
            b.setCursor(Cursor.HAND);
        });
        b.setOnMouseExited(e -> {
            b.setEffect(null);
            b.setScaleX(1.0);
            b.setScaleY(1.0);
            b.setCursor(Cursor.DEFAULT);
        });
        b.setOnMousePressed(e -> {
            b.setScaleX(1.05);
            b.setScaleY(1.05);
        });
        b.setOnMouseReleased(e -> {
            b.setScaleX(1.1);
            b.setScaleY(1.1);
        });
    }

    private boolean validateName(String name, Label errorLbl, TextField field) {
        String errorMsg = "";
        if (name.isEmpty()) {
            errorMsg = "Name cannot be empty!";
        } else {
            if (!Character.isUpperCase(name.charAt(0))) {
                errorMsg = "Must start with uppercase!";
            } else {
                if (!name.matches(".*\\d.*")) {
                    errorMsg = "Must contain a number!";
                }
            }
        }
        if (!errorMsg.isEmpty()) {
            errorLbl.setText(errorMsg);
            errorLbl.setVisible(true);
            styleTextField(field, true);
            return false;
        } else {
            return true;
        }
    }

    private void styleTextField(TextField tf, boolean isError) {
        String baseStyle = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2;";
        if (isError) tf.setStyle(baseStyle + "-fx-border-color: RED; -fx-background-color: #FFF0F0;");
        else tf.setStyle(baseStyle + "-fx-border-color: #E0E0E0; -fx-background-color: WHITE;");
    }

    public void showHighScoreOverlay() {
        highScoreButton.fire();
    }

    public MainMenuPane(Consumer<String> playAction,
                        Supplier<List<String>> highScoreAction,
                        EventHandler<ActionEvent> settingAction,
                        EventHandler<ActionEvent> quitAction) {

        /**
         * Background video.
         */
        MediaView mediaView = createBackgroundVideo();
        if (mediaView != null) {
            getChildren().add(mediaView);
        } else {
            setStyle("-fx-background-color: #505064;");
        }

        /**
         * TitleGame.
         */
        titleView = createTitileView();
        StackPane.setAlignment(titleView, Pos.TOP_CENTER);
        StackPane.setMargin(titleView, new Insets(50, 0, 0, 0));
        getChildren().add(titleView);

        /**
         * MainButton.
         */
        mainButtonsVbox = new VBox(12);
        mainButtonsVbox.setAlignment(Pos.BOTTOM_CENTER);
        Button playButton = createMenuButton("Play", true);
        this.highScoreButton = createMenuButton("HighScore", true);
        Button settingButton = createMenuButton("Setting", true);
        Button quitButton = createMenuButton("Quit", true);

        mainButtonsVbox.getChildren().addAll(playButton, highScoreButton, settingButton, quitButton);
        StackPane.setAlignment(mainButtonsVbox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(mainButtonsVbox, new Insets(0, 0, 20, 0));
        getChildren().add(mainButtonsVbox);
            //phu lop name va highscore hien thi len tren
        createNameOverlay(playAction);
        createHighScoreOverlay(highScoreAction);
            //them 2 overlay vap vbox
        getChildren().addAll(nameOverlay, highScoreOverlay);

        playButton.setOnAction(e -> showOverlay(nameOverlay));
        highScoreButton.setOnAction(e -> {
            updateHighScoreList(highScoreAction);
            showOverlay(highScoreOverlay);
        });
        settingButton.setOnAction(settingAction);
        quitButton.setOnAction(quitAction);
    }
}


//        MediaView mediaView = null;
//        try {
//            String videoPath = getClass().getResource("/menu.mp4").toExternalForm();
//            Media media = new Media(videoPath);
//            MediaPlayer mediaPlayer = new MediaPlayer(media);
//            mediaPlayer.setAutoPlay(true);
//            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//            mediaPlayer.setMute(true);
//
//            mediaView = new MediaView(mediaPlayer);
//
//            mediaView.fitWidthProperty().bind(widthProperty());
//            mediaView.fitHeightProperty().bind(heightProperty());
//            mediaView.setPreserveRatio(false);
//        } catch (Exception ex) {
//            System.out.println("Cannot load menu video");
//        }
//
//        /**
//         * Tạo vbox các button.
//         */
//        VBox mainButtonsVBox = new VBox(12);
//        mainButtonsVBox.setAlignment(Pos.CENTER);
//        Button play = createButton("Play", true);
//        Button highScore = createButton("Highscore", true);
//        Button setting = createButton("Setting", true);
//        Button quit = createButton("Quit", true);
//
//        /**
//         * Gắn hoạt động các button.
//         */
//        highScore.setOnAction(highScoreAction);
//        setting.setOnAction(settingAction);
//        quit.setOnAction(quitAction);
//        mainButtonsVBox.getChildren().addAll(play, highScore, setting, quit);
//
//        /**
//         * Tao title.
//         */
//        ImageView titleView = new ImageView();
//        try {
//            Image logoImg = new Image(getClass().getResourceAsStream("/textTitle.png"));
//            titleView.setImage(logoImg);
//        }
//        catch (Exception e) {
//            System.out.println("Không thể tải ảnh /textTitle.png");
//        }
//
//        /**
//         * Tạo overlay nhập usename.
//         */
//        VBox nameInputOverlay = new VBox(15);
//        nameInputOverlay.setAlignment(Pos.CENTER);
//        nameInputOverlay.setPadding(new Insets(15));
//        nameInputOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85); -fx-background-radius: 15; -fx-border-width: 2; -fx-border-radius: 15;");
//        nameInputOverlay.setMaxSize(200, 200);
//
//        BorderPane headerPane = new BorderPane();
//        /**
//         * Overlay usename.
//         */
//        ImageView nameView = new ImageView();
//        try {
//            Image nameImg = new Image(getClass().getResourceAsStream("/Entername.png"));
//            nameView.setImage(nameImg); // Chỉ 'set' ảnh
//            nameView.setFitWidth(280);
//            nameView.setPreserveRatio(true);
//            headerPane.setCenter(nameView);
//            BorderPane.setMargin(nameView, new Insets(0,0,0,20));
//        }
//        catch (Exception e) {
//            System.out.println("Không thể tải ảnh /EnterName.png");
//            Label nameLabel = new Label("Enter Your Name:");
//            nameLabel.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 24px; -fx-font-weight: bold;");
//            headerPane.setCenter(nameLabel);
//        }
//
//        Button closeButton = new Button();
//        try {
//            Image xImg = new Image(getClass().getResourceAsStream("/X.png"));
//            ImageView xImgView = new ImageView(xImg);
//            xImgView.setFitWidth(50);  // Kích thước nút X (nhỏ thôi)
//            xImgView.setFitHeight(30);
//            xImgView.setPreserveRatio(true);
//            closeButton.setGraphic(xImgView);
//            closeButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;"); // Nút trong suốt
//        } catch (Exception e) {
//            closeButton.setText("X"); // Dự phòng nếu lỗi ảnh
//            closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 20px; -fx-font-weight: bold;");
//        }
//
//        DropShadow xGlow = new DropShadow(10, Color.RED); // Sáng màu đỏ nhẹ
//        closeButton.setOnMouseEntered(e -> {
//            closeButton.setScaleX(1.2); // Phóng to lên 120%
//            closeButton.setScaleY(1.2);
//            closeButton.setEffect(xGlow);
//            closeButton.setCursor(Cursor.HAND); // Đổi con trỏ thành hình bàn tay
//        });
//        closeButton.setOnMouseExited(e -> {
//            closeButton.setScaleX(1.0);
//            closeButton.setScaleY(1.0);
//            closeButton.setEffect(null);
//            closeButton.setCursor(Cursor.DEFAULT);
//        });
//        // Hành động nút X: Ẩn overlay
//        closeButton.setOnAction(e -> {
//            nameInputOverlay.setVisible(false);
//            mainButtonsVBox.setVisible(true);
//            titleView.setVisible(true);
//        });
//
//        headerPane.setRight(closeButton);
//
//
//        /**
//         * Nhap ten.
//         */
//        TextField nameField = new TextField(); // Đã xóa "Player 1"
//        nameField.setMaxWidth(280);
//        nameField.setFont(Font.font("Verdana", 18));
//        nameField.setAlignment(Pos.CENTER);
//
//        final String NORMAL_STYLE = "-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #E0E0E0; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;";
//        final String ERROR_STYLE = "-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: RED; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;";
//        nameField.setStyle(NORMAL_STYLE);
//
//        Label errorLabel = new Label(""); // Ban đầu rỗng
//        errorLabel.setTextFill(Color.RED); // Màu đỏ
//        errorLabel.setFont(Font.font("Verdana", 12)); // Font nhỏ hơn chút
//        errorLabel.setVisible(false);
//
//        Button okButton = createButton("OK", false);
//
//        Runnable checkNameAndStart = () -> {
//            String name = nameField.getText().trim();
//            errorLabel.setVisible(false);
//            nameField.setStyle(NORMAL_STYLE);
//
//            if (name.isEmpty()) {
//                errorLabel.setText("Name cannot be empty!");
//                errorLabel.setVisible(true);
//                nameField.setStyle(ERROR_STYLE);
//                return;
//            }
//
//            // 2. Kiểm tra ký tự đầu viết hoa
//            if (!Character.isUpperCase(name.charAt(0))) {
//                errorLabel.setText("First letter must be uppercase!");
//                errorLabel.setVisible(true);
//                nameField.setStyle(ERROR_STYLE);
//                return;
//            }
//
//            // 3. Kiểm tra có chứa số
//            boolean hasDigit = false;
//            for (char c : name.toCharArray()) {
//                if (Character.isDigit(c)) {
//                    hasDigit = true;
//                    break;
//                }
//            }
//            if (!hasDigit) {
//                errorLabel.setText("Name must contain a number!");
//                errorLabel.setVisible(true);
//                nameField.setStyle(ERROR_STYLE);
//                return;
//            }
//
//            // Nếu HỢP LỆ
//            playAction.accept(name); // Bắt đầu game
//        };
//
//
//        okButton.setOnAction(e -> checkNameAndStart.run());
//        nameField.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                checkNameAndStart.run();
//            } else {
//                // Khi người dùng gõ lại, xóa thông báo lỗi và trả lại viền xanh
//                errorLabel.setVisible(false);
//                nameField.setStyle(NORMAL_STYLE);
//            }
//        });
//
//        // Thêm tất cả vào Overlay
//        nameInputOverlay.getChildren().addAll(headerPane, nameField, errorLabel, okButton);
//        nameInputOverlay.setVisible(false);
//
//        /**
//         * Action cua playAction.
//         */
//        play.setOnAction(event -> {
//            mainButtonsVBox.setVisible(false);
//            titleView.setVisible(false);
//            nameInputOverlay.setVisible(true);
//            nameField.setText("");
//            errorLabel.setVisible(false);
//            nameField.setStyle(NORMAL_STYLE); // Reset style khi mở overlay
//            nameField.requestFocus();
//        });
//
//        /**
//         * Sap xep thu tu layer.
//         */
//        if (mediaView != null) {
//            getChildren().addAll(mediaView, titleView, mainButtonsVBox, nameInputOverlay);
//        } else {
//            getChildren().addAll(titleView, mainButtonsVBox, nameInputOverlay);
//            setStyle("-fx-background-color: #505064; ...");
//        }
//
//        StackPane.setAlignment(titleView, Pos.TOP_CENTER); //can le title
//        StackPane.setMargin(titleView, new Insets(50, 0, 0, 0));
//
//        StackPane.setAlignment(mainButtonsVBox, Pos.BOTTOM_CENTER); //can le mainButton
//        StackPane.setMargin(mainButtonsVBox, new Insets(350,0,20,0));
//
//        StackPane.setAlignment(nameInputOverlay, Pos.CENTER); //can le overlay usename
//    }
//
//    /**
//     * Hàm chung để tạo các nút bằng ảnh
//     * @param baseName Tên file ảnh (ví dụ: "Play" -> sẽ tải "/Play.png")
//     * @param withGlow true = thêm hiệu ứng phát sáng; false = chỉ phóng to
//     */
//    private Button createButton(String baseName, boolean withGlow) {
//        Button b = new Button();
//        try {
//            String imagePath = "/" + baseName + ".png";
//            Image img = new Image(getClass().getResourceAsStream(imagePath));
//            ImageView imgView = new ImageView(img);
//            imgView.setFitWidth(190);
//            imgView.setPreserveRatio(true);
//            b.setGraphic(imgView);
//            b.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
//        }
//        catch (Exception e) {
//            System.out.println("Không thể tải ảnh: " + baseName);
//            b.setText(baseName.toUpperCase());
//        }
//        /**
//         * Tao effect.
//         */
//        DropShadow glow = null;
//        if (withGlow) {
//            glow = new DropShadow();
//            glow.setColor(Color.WHITE);
//            glow.setRadius(25);
//            glow.setSpread(0.25);
//        }
//
//        final DropShadow finalGlow = glow;
//        //dieu khien chuot
//        b.setOnMouseEntered(event -> {
//            if (withGlow) {
//                b.setEffect(finalGlow);
//            }
//            b.setScaleX(1.15);
//            b.setScaleY(1.15);
//        });
//        b.setOnMouseExited(event -> {
//            if (withGlow) {
//                b.setEffect(null);
//            }
//            b.setScaleX(1.0);
//            b.setScaleY(1.0);
//        });
//        b.setOnMousePressed(event -> {
//            b.setScaleX(1.05);
//            b.setScaleY(1.05);
//        });
//        b.setOnMouseReleased(event -> {
//            if (b.isHover()) {
//                b.setScaleX(1.15);
//                b.setScaleY(1.15);
//            } else {
//                b.setScaleX(1.0);
//                b.setScaleY(1.0);
//            }
//        });
//        return b;
//    }
//}


