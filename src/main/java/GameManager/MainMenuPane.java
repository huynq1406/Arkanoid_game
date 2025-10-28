package GameManager;

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
import javafx.scene.text.Font;

public class MainMenuPane extends StackPane {
    public MainMenuPane(EventHandler<ActionEvent> playAction,
                        EventHandler<ActionEvent> highScoreAction,
                        EventHandler<ActionEvent> quitAction)  {
//                        EventHandler<ActionEvent> settingAction) {
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
            System.out.println("Cannot load menu");
        }
        VBox vbox = new VBox(12);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        //Tao cac button hien thi man hinh
        Button play = createButton("Play");
        Button highScore = createButton("HighScore");
        //Button setting = createButton("Setting");
        Button quit = createButton("Quit");

        play.setOnAction(playAction);
        highScore.setOnAction(highScoreAction);
        //setting.setOnAction(settingAction);
        quit.setOnAction(quitAction);

        vbox.getChildren().addAll(play, highScore, quit);

        if (mediaView != null) {
            getChildren().addAll(mediaView, vbox);
        } else {
            getChildren().add(vbox);
            setStyle("-fx-menu-error");
        }
    }

        // Background image
//        Image bgImage = null;
//        try {
//            bgImage = new Image(getClass().getResourceAsStream("/images.jpg"));
//        } catch (Exception ex) {
//            System.err.println("Cannot load background image: " + ex.getMessage());
//        }
//        ImageView bgView = new ImageView();
//        if (bgImage != null) {
//            bgView.setImage(bgImage);
//            bgView.setPreserveRatio(false);
//            // make background fill the pane
//            bgView.fitWidthProperty().bind(widthProperty());
//            bgView.fitHeightProperty().bind(heightProperty());
//        }
//
//        // Buttons container
//        VBox vbox = new VBox(12);
//        vbox.setAlignment(Pos.CENTER);
//        vbox.setPadding(new Insets(20));
//
//        Button playButton = createButton("Play");
//        Button highScoreButton = createButton("High Score");
//        Button quitButton = createButton("Quit");
//
//        playButton.setOnAction(playAction);
//        highScoreButton.setOnAction(highScoreAction);
//        quitButton.setOnAction(quitAction);
//
//        vbox.getChildren().addAll(playButton, highScoreButton, quitButton);
//
//        // Add background first, then controls on top
//        if (bgImage != null) {
//            getChildren().addAll(bgView, vbox);
//        } else {
//            getChildren().add(vbox);
//            setStyle("-fx-background-color: #14141E;"); // fallback background color
//        }
//    }

    private Button createButton(String text) {
        Button b = new Button(text);
        b.setPrefSize(250, 50);
        b.setFont(Font.font("Verdana", 22));
        b.setStyle(
                "-fx-background-color: #505064; -fx-text-fill: white; -fx-background-radius: 6; -fx-focus-color: transparent;"
        );
        return b;
    }
}
