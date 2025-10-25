package GameManager;

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
        // Background image
        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/images.jpg"));
        } catch (Exception ex) {
            System.err.println("Cannot load background image: " + ex.getMessage());
        }
        ImageView bgView = new ImageView();
        if (bgImage != null) {
            bgView.setImage(bgImage);
            bgView.setPreserveRatio(false);
            // make background fill the pane
            bgView.fitWidthProperty().bind(widthProperty());
            bgView.fitHeightProperty().bind(heightProperty());
        }

        // Buttons container
        VBox vbox = new VBox(12);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Button playButton = createButton("Play");
        Button highScoreButton = createButton("High Score");
        Button quitButton = createButton("Quit");

        playButton.setOnAction(playAction);
        highScoreButton.setOnAction(highScoreAction);
        quitButton.setOnAction(quitAction);

        vbox.getChildren().addAll(playButton, highScoreButton, quitButton);

        // Add background first, then controls on top
        if (bgImage != null) {
            getChildren().addAll(bgView, vbox);
        } else {
            getChildren().add(vbox);
            setStyle("-fx-background-color: #14141E;"); // fallback background color
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
