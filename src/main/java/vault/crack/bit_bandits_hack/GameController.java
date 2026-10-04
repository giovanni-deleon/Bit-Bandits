package vault.crack.bit_bandits_hack;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GameController {

    // Matches fx:id="safeDial" in your FXML
    @FXML
    private ImageView safeDial;

    @FXML
    public void initialize() {
        // Example: Set initial rotation or rotate the dial programmatically
        // safeDial.setRotate(45.0);
    }

    /**
     * Helper method to rotate the safe dial by a given angle (in degrees).
     * @param angle Angle to rotate the dial
     */
    public void rotateDial(double angle) {
        if (safeDial != null) {
            safeDial.setRotate(safeDial.getRotate() + angle);
        }
    }
}