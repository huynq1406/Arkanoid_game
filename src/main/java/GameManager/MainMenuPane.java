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
                        EventHandler<ActionEvent> quitAction) {
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
