package vault.crack.bit_bandits_hack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Loads the main safe dial view
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/view/first-level.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setTitle("Bit Bandits Hack");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}