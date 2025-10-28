package GameManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;
    private Scene scene;
    private MainMenuPane menuPane;
    private GamePanel gamePanel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        menuPane = new MainMenuPane(
                (ActionEvent e) -> startGame(),
                (ActionEvent e) -> showHighScore(),
                (ActionEvent e) -> quitGame()
        );
        scene = new Scene(menuPane, GamePanel.WIDTH, GamePanel.HEIGHT);
        primaryStage.setTitle("Golf Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void startGame() {
        if (gamePanel == null) {
            gamePanel = new GamePanel();
        }
        scene.setRoot(gamePanel);
        gamePanel.requestFocus();
    }

    private void showHighScore() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("High Score");
        alert.setHeaderText(null);
        alert.setContentText("High Scores will be added soon!");
        alert.showAndWait();
    }

    private void quitGame() {
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


