package GameManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Entities.Ball;
import Entities.Paddle;
import java.util.function.Consumer;

public class Main extends Application {
    private Stage primaryStage;
    private Scene scene;
    private MainMenuPane menuPane;
    private GameManager gameManager;
    private GamePanel gamePanel;
    private Ball ball = new Ball(400, 300, 10);
    private Paddle paddle = new Paddle(350, 550, 100, 20);
    private String playerName;

    @Override
    public void start(Stage primaryStage) { //bat dau hien thi meu
        this.primaryStage = primaryStage;
        menuPane = new MainMenuPane(
                (String nameFromOverlay) -> startGame(playerName),
                (ActionEvent e) -> showHighScore(),
                (ActionEvent e) -> showSettings(),
                (ActionEvent e) -> quitGame()
        );
        scene = new Scene(menuPane, GamePanel.WIDTH, GamePanel.HEIGHT);
//        primaryStage.setTitle("Arkanoid Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void startGame(String playerName) { //bat dau game
        if (gamePanel == null) {
            gamePanel = new GamePanel();
        }
        scene.setRoot(gamePanel);
        gamePanel.requestFocus();

        this.gameManager = new GameManager(
                GamePanel.WIDTH,
                GamePanel.HEIGHT,
                gamePanel,
                new Entities.Ball(400, 300, 10),
                new Entities.Paddle(350, 550, 100, 20),
                playerName
        );
        gamePanel.setGameManager(gameManager);
        gameManager.buildLevel();
        gameManager.start();
    }

    private void showSettings() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Settings");
        alert.setHeaderText(null);
        alert.setContentText("Settings will be added soon!");
        alert.showAndWait();
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


