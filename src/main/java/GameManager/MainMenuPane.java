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
import javafx.scene.layout.HBox;
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
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MainMenuPane extends StackPane {
    private final VBox mainButtonsVbox;
    private final ImageView titleView;
    private VBox nameOverlay;
    private VBox highScoreOverlay;
    private VBox gameOveroverlay;
    private VBox settingsOverlay;

    private Label gameOverScoreLabel;
    private final Button highScoreButton;
    private Runnable postHighScoreAction = null; //bien luu hanh dong khi dong highscore

    private MediaPlayer mediaPlayer;
    private Button musicBtnOn;
    private Button musicBtnOff;

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

    public MediaPlayer getBackgroundMusicPlayer() {
        return this.mediaPlayer;
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

    private void createGameOveroverlay(Runnable playAgainAction, Supplier<List<String>> highScoreAction) {
        gameOveroverlay = createBaseOverlay(250, 100);
        gameOveroverlay.setSpacing(20);

        DropShadow blueGlow = new DropShadow();
        blueGlow.setColor(Color.CORNFLOWERBLUE);
        blueGlow.setRadius(30);
        blueGlow.setSpread(0.5);

        ImageView gameOverImg = new ImageView();
        try {
            gameOverImg.setImage(new Image(getClass().getResourceAsStream("/Gameover.png")));
            if (gameOverImg!= null) gameOverImg.setFitWidth(250);
            VBox.setMargin(gameOverImg, new Insets(20, 0, 0, 0));
            gameOverImg.setPreserveRatio(true);
            gameOverImg.setEffect(blueGlow);
        } catch (Exception e) {
            Label lbl = new Label("GAMEOVER");
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("Verdana", 24));
        }

        gameOverScoreLabel = new Label();
        gameOverScoreLabel.setTextFill(Color.WHITE);
        gameOverScoreLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        gameOverScoreLabel.setStyle("-fx-text-alignment: center;");

        Button playAgainButton = createMenuButton("PlayAgain", true);
        ImageView playAgainImg = (ImageView) playAgainButton.getGraphic();
        if (playAgainImg != null) playAgainImg.setFitWidth(250);
        VBox.setMargin(playAgainButton, new Insets(0, 0, 0, 0));

        playAgainButton.setOnAction(e -> {
            postHighScoreAction = playAgainAction;
            updateHighScoreList(highScoreAction);
            showOverlay(highScoreOverlay);
        });

        Button menuButton = createMenuButton("Menu", true);
        ImageView menuImg = (ImageView) menuButton.getGraphic();
        if (menuImg != null) menuImg.setFitWidth(165);

        VBox.setMargin(menuButton, new Insets(0, 0, 0, 0));
        menuButton.setOnAction(e -> {
            postHighScoreAction = null;
            updateHighScoreList(highScoreAction);
            showOverlay(highScoreOverlay);
        });
        gameOveroverlay.getChildren().addAll(gameOverImg, gameOverScoreLabel, playAgainButton, menuButton);
    }

    public void showGameOver(String playerName, int score) {
        gameOverScoreLabel.setText(playerName.toUpperCase() + "\nSCORE: " + score);
        showOverlay(gameOveroverlay);
    }

    private void createNameOverlay(Consumer<String> playAction) {
        nameOverlay = createBaseOverlay(300,250);
        nameOverlay.setSpacing(8);
        StackPane header = new StackPane();
        header.setAlignment(Pos.CENTER);

        ImageView nameTitle = new ImageView();
        try {
            nameTitle.setImage(new Image(getClass().getResourceAsStream("/Entername.png")));
            nameTitle.setFitWidth(290);
            nameTitle.setPreserveRatio(true);
        } catch (Exception e) {
            Label lbl = new Label("ENTER YOUR NAME");
            lbl.setTextFill(Color.CYAN);
            lbl.setFont(Font.font("Verdana", 24));
        }
        Button closeButton = createCloseButton(nameOverlay);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        closeButton.setTranslateX(30);
        closeButton.setTranslateY(-30);
        header.getChildren().addAll(nameTitle, closeButton);

        //Nhap ten
        TextField nameField = new TextField();
        nameField.setMaxWidth(250);
        nameField.setFont(Font.font("Verdana", 16));
        nameField.setAlignment(Pos.CENTER);
        styleTextField(nameField, false);
        VBox.setMargin(nameField, new Insets(15, 0, 5, 0));

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);


        Button okButton = createMenuButton("OK", false);
        ImageView okImg = (ImageView) okButton.getGraphic();
        if (okImg != null) {
            okImg.setFitWidth(220);
        }
        VBox.setMargin(okButton, new Insets(10, 0, 0, 0));
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
            blueGlow.setColor(Color.CORNFLOWERBLUE);
            blueGlow.setRadius(25);
            blueGlow.setSpread(0.4);
            imgHighScore.setEffect(blueGlow);

            header.setCenter(imgHighScore);
        } catch (Exception e) {
            Label fallbackTitle = new Label("HIGH SCORES");
            fallbackTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
            fallbackTitle.setTextFill(Color.CORNFLOWERBLUE);
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
        xButton.setOnAction(e -> {

            if (targetOverlay == highScoreOverlay && postHighScoreAction != null) {
                targetOverlay.setVisible(false);
                postHighScoreAction.run();
                postHighScoreAction = null;
            } else {
                hideOverlay(targetOverlay);
            }
        });
        return xButton;
    }

    private void createSettingsOverlay(Supplier<Boolean> musicStateGetter, Consumer<Boolean> musicStateSetter) {
        settingsOverlay = createBaseOverlay(300, 250);
        settingsOverlay.setSpacing(20);

        StackPane header = new StackPane();
        header.setAlignment(Pos.CENTER);
        ImageView musictitle = new ImageView();
        try {
            musictitle.setImage(new Image(getClass().getResourceAsStream("/Music.png")));
            musictitle.setFitWidth(200);
            musictitle.setPreserveRatio(true);
            DropShadow blueGlow = new DropShadow();
            blueGlow.setColor(Color.CORNFLOWERBLUE); // Hoặc Color.CYAN tùy bạn chọn
            blueGlow.setRadius(20);
            blueGlow.setSpread(0.4);
            musictitle.setEffect(blueGlow);
            header.getChildren().add(musictitle);
        } catch (Exception e) {
            Label lbl = new Label("MUSIC");
            lbl.setTextFill(Color.CYAN);
            lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
            header.getChildren().add(lbl);
        }
        Button closeButton = createCloseButton(settingsOverlay);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        closeButton.setTranslateX(30);
        closeButton.setTranslateY(-30);
        header.getChildren().add(closeButton);

        HBox toggleBar = new HBox(10);
        toggleBar.setAlignment(Pos.CENTER);

        musicBtnOn = new Button("ON");
        musicBtnOn.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        applyButtonEffects(musicBtnOn, false);
        musicBtnOn.setOnAction(e -> {
            musicStateSetter.accept(true);
            updateMusicButtonStyles(true);
        });

        musicBtnOff = new Button("OFF");
        musicBtnOff.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        applyButtonEffects(musicBtnOff, false); // Áp dụng hiệu ứng phóng to
        musicBtnOff.setOnAction(e -> {
            musicStateSetter.accept(false);
            updateMusicButtonStyles(false);
        });
        toggleBar.getChildren().addAll(musicBtnOn, musicBtnOff);
        updateMusicButtonStyles(musicStateGetter.get());

        settingsOverlay.getChildren().addAll(header, toggleBar);
    }

    private void updateMusicButtonStyles(boolean isMusicOn) {
        if (isMusicOn) {
            musicBtnOn.setStyle("-fx-background-color: #20C20E; -fx-text-fill: white; -fx-background-radius: 10;");
            musicBtnOff.setStyle("-fx-background-color: #505050; -fx-text-fill: #A0A0A0; -fx-background-radius: 10; -fx-opacity: 0.7;");
        } else {
            musicBtnOn.setStyle("-fx-background-color: #505050; -fx-text-fill: #A0A0A0; -fx-background-radius: 10; -fx-opacity: 0.7;");
            musicBtnOff.setStyle("-fx-background-color: #D41010; -fx-text-fill: white; -fx-background-radius: 10;");
        }
    }

    private void showOverlay(VBox overlay) {
        mainButtonsVbox.setVisible(false);
        titleView.setVisible(false);

        if (nameOverlay != null) nameOverlay.setVisible(false);
        if (highScoreOverlay != null) highScoreOverlay.setVisible(false);
        if (gameOveroverlay != null) gameOveroverlay.setVisible(false);

        overlay.setVisible(true);
        overlay.toFront();

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
                        Runnable playAgainAction,
                        Supplier<List<String>> highScoreAction,
                        Supplier<Boolean> musicStateGetter,
                        Consumer<Boolean> musicStateSetter,
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
        createGameOveroverlay(playAgainAction, highScoreAction);
        createSettingsOverlay(musicStateGetter, musicStateSetter);
            //them 2 overlay vao vbox
        getChildren().addAll(nameOverlay, highScoreOverlay,gameOveroverlay, settingsOverlay);

        playButton.setOnAction(e -> showOverlay(nameOverlay));
        highScoreButton.setOnAction(e -> {
            postHighScoreAction = null;
            updateHighScoreList(highScoreAction);
            showOverlay(highScoreOverlay);
        });
        settingButton.setOnAction(e -> {
            updateMusicButtonStyles(musicStateGetter.get());
            showOverlay(settingsOverlay);
        });
        quitButton.setOnAction(quitAction);
    }
}




