// Author: Giovanni De Leon

package vault.crack.bit_bandits_hack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LockPickController {

    /**
     * Transitions from Lock Pick to Laser Heist scene and attaches key listeners.
     */
    @FXML
    public void switchToLaserHeist(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/laser-room.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Bind WASD / Key controls for the LaserHeistController
            LaserHeistController controller = loader.getController();
            if (controller != null) {
                scene.setOnKeyPressed(controller::handleKeyPress);
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load /view/laser-room.fxml");
        }
    }
}