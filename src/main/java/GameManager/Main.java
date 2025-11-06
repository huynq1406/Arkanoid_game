package GameManager;

//import java.io.*;
//import java.util.*;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.stage.Stage;
//import Entities.Ball;
//import Entities.Paddle;
//
//class ScoreEntry implements Comparable<ScoreEntry> {
//    String name;
//    int score;
//
//    public ScoreEntry(String name, int score) {
//        this.name = name;
//        this.score = score;
//    }
//    @Override
//    public int compareTo(ScoreEntry other) {
//        return other.score - this.score;
//    }
//    @Override
//    public String toString() {
//        return name + ":" + score;
//    }
//}
//
//public class Main extends Application {
//    private Stage primaryStage;
//    private Scene scene;
//    private MainMenuPane menuPane;
//    private GameManager gameManager;
//    private GamePanel gamePanel;
//    private Ball ball = new Ball(400, 300, 10);
//    private Paddle paddle = new Paddle(350, 550, 100, 20);
//    private String playerName;
//    private static final String HIGHSCORE_FILE = "highscore.txt";
//    private List<ScoreEntry> HighScores = new ArrayList<>();
//
//    @Override
//    public void start(Stage primaryStage) { //bat dau hien thi meu
//        this.primaryStage = primaryStage;
//        menuPane = new MainMenuPane(
//                (String nameFromOverlay) -> startGame(nameFromOverlay),
//                (ActionEvent e) -> showHighScore(),
//                (ActionEvent e) -> showSettings(),
//                (ActionEvent e) -> quitGame()
//        );
//        scene = new Scene(menuPane, GamePanel.WIDTH, GamePanel.HEIGHT);
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
//        primaryStage.show();
//    }
//
//    private void startGame(String playerName) { //bat dau game
//        if (gamePanel == null) {
//            gamePanel = new GamePanel();
//        }
//        scene.setRoot(gamePanel);
//        gamePanel.requestFocus();
//
//        this.gameManager = new GameManager(
//                GamePanel.WIDTH,
//                GamePanel.HEIGHT,
//                gamePanel,
//                new Entities.Ball(400, 300, 10),
//                new Entities.Paddle(350, 550, 100, 20),
//                playerName,
//                (score) -> handleGameOver(playerName, score)
//        );
//        gamePanel.setGameManager(gameManager);
//        gameManager.buildLevel();
//        gameManager.start();
//    }
//
//    private void handleGameOver(String playerName, int score) {
//        HighScores.add(new ScoreEntry(playerName, score));
//        Collections.sort(HighScores);
//        if (HighScores.size() > 5) {
//            HighScores = HighScores.subList(0, 5);
//        }
//        saveHighScores();
//        Platform.runLater(()-> {
//            scene.setRoot(menuPane);
//            showHighScore();
//        });
//    }
//
//    private void loadHighScores() {
//        HighScores.clear();
//        File file = new File(HIGHSCORE_FILE);
//        if (!file.exists()) return; // Nếu file chưa có thì thôi
//
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(":");
//                if (parts.length == 2) {
//                    HighScores.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
//                }
//            }
//            Collections.sort(HighScores); // Sắp xếp luôn cho chắc
//        } catch (IOException e) {
//            System.err.println("Lỗi đọc file highscore: " + e.getMessage());
//        }
//    }
//
//
//    private void showSettings() {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.initOwner(primaryStage);
//        alert.setTitle("Settings");
//        alert.setHeaderText(null);
//        alert.setContentText("Settings will be added soon!");
//        alert.showAndWait();
//    }
//
//    private void saveHighScores() {
//        try (PrintWriter pw = new PrintWriter(new FileWriter(HIGHSCORE_FILE))) {
//            for (ScoreEntry entry : HighScores) {
//                pw.println(entry.toString());
//            }
//        } catch (IOException e) {
//            System.err.println("Lỗi ghi file highscore: " + e.getMessage());
//        }
//    }
//
//    private List<String> showHighScore() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < HighScores.size(); i++) {
//            sb.append((i + 1) + ". " + HighScores.get(i).name + " - " + HighScores.get(i).score + "\n");
//        }
//        if (HighScores.isEmpty()) {
//            sb.append("No high scores yet!");
//        }
//
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.initOwner(primaryStage);
//        alert.setTitle("High Score");
//        alert.setHeaderText("Top 5 Players");
//        alert.setContentText(sb.toString());
//        alert.showAndWait();
//        return null;
//    }
//
//    private void quitGame() {
//        Platform.exit();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

import Entities.Ball;
import Entities.Paddle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;
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
    private MediaPlayer backgroundMusicPlayer;
    private boolean isMusicOn = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadHighScores();

        menuPane = new MainMenuPane(
                this::startGame,
                this::playAgain,
                this::getFormattedHighScores,
                () -> this.isMusicOn,
                this::toggleMusic,
                (ActionEvent e) -> quitGame()
        );

        this.backgroundMusicPlayer = menuPane.getBackgroundMusicPlayer();
        toggleMusic(this.isMusicOn);

        scene = new Scene(menuPane, GamePanel.WIDTH, GamePanel.HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void toggleMusic(boolean on) {
        this.isMusicOn = on;
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setMute(!on);
        }
    }


    private void startGame(String playerName) {
        this.playerName = playerName;
        launchGame();
    }

    private void playAgain() {
        if (this.playerName == null || this.playerName.isEmpty()) {
            this.playerName = "Player1";
        }
        launchGame();
    }

    private void launchGame() {
        if (gamePanel == null) gamePanel = new GamePanel();
        scene.setRoot(gamePanel);
        gamePanel.requestFocus();

        this.gameManager = new GameManager(
                GamePanel.WIDTH, GamePanel.HEIGHT, gamePanel,
                new Ball(400, 300, 10), new Paddle(350, 550, 100, 20),
                this.playerName,
                this::handleGameOver
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
            menuPane.showGameOver(this.playerName, score);
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
        System.out.println("Nút Cài đặt được xử lý trong MainMenuPane.");
    }

    private void quitGame() {
        Platform.exit();
        System.exit(0);
    }
}


