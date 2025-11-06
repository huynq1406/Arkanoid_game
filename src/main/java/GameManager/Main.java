package GameManager;

import Entities.Ball;
import Entities.Paddle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ScoreEntry implements Comparable<ScoreEntry> {
    String name;
    int score;

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return other.score - this.score;
    }

    @Override
    public String toString() {
        return name + ":" + score;
    }
}

public class Main extends Application {
    private static final String HIGHSCORE_FILE = "highscore.txt";
    private Stage primaryStage;
    private Scene scene;
    private MainMenuPane menuPane;
    private GameManager gameManager;
    private GamePanel gamePanel;
    private String playerName;
    private List<ScoreEntry> highScores = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadHighScores();

        menuPane = new MainMenuPane(
                this::startGame,
                this::getFormattedHighScores,
                (ActionEvent e) -> showSettings(),
                (ActionEvent e) -> quitGame()
        );

        scene = new Scene(menuPane, GamePanel.WIDTH, GamePanel.HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Arkanoid Game");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void startGame(String playerName) {
        this.playerName = playerName;
        if (gamePanel == null) {
            gamePanel = new GamePanel();
        }
        scene.setRoot(gamePanel);
        gamePanel.requestFocus();

        this.gameManager = new GameManager(
                GamePanel.WIDTH,
                GamePanel.HEIGHT,
                gamePanel,
                new Ball(400, 300, 10),
                new Paddle(350, 550, 100, 20),
                playerName,
                (score) -> handleGameOver(score)
        );
        gamePanel.setGameManager(gameManager);
        gameManager.buildLevel();
        gameManager.start();
    }

    private void handleGameOver(int score) {
        highScores.add(new ScoreEntry(this.playerName, score));
        Collections.sort(highScores);
        if (highScores.size() > 5) {
            highScores = highScores.subList(0, 5);
        }
        saveHighScores();

        Platform.runLater(() -> {
            scene.setRoot(menuPane);
            menuPane.showHighScoreOverlay();
        });
    }

    private List<String> getFormattedHighScores() {
        List<String> formattedList = new ArrayList<>();
        for (int i = 0; i < highScores.size(); i++) {
            ScoreEntry entry = highScores.get(i);
            formattedList.add(String.format("%d. %-10s - %5d", (i + 1), entry.name, entry.score));
        }
        return formattedList;
    }

    private void loadHighScores() {
        highScores.clear();
        File file = new File(HIGHSCORE_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    highScores.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
                }
            }
            Collections.sort(highScores);
        } catch (IOException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }
    }

    private void saveHighScores() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(HIGHSCORE_FILE))) {
            for (ScoreEntry entry : highScores) {
                pw.println(entry.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    private void showSettings() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Settings");
        alert.setHeaderText("Game Settings");
        alert.setContentText("Features coming soon:\n- Sound Volume\n- Difficulty Level\n- Custom Controls");
        alert.showAndWait();
    }

    private void quitGame() {
        Platform.exit();
        System.exit(0);
    }
}
