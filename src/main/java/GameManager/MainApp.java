package GameManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public void start(Stage primaryStage) {
        // Create menu with handlers
        MainMenuPane menu = new MainMenuPane(
                e -> System.out.println("Play clicked"),
                e -> System.out.println("High Score clicked"),
                e -> {
                    System.out.println("Quit clicked");
                    primaryStage.close();
                }
        );

        Scene scene = new Scene(menu, GamePanel.WIDTH, GamePanel.HEIGHT); // reuse your existing constants
        primaryStage.setTitle("Game Main Menu (JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
