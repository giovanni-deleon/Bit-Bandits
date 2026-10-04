package vault.crack.bit_bandits_hack;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class GameController {

    @FXML
    private ImageView safeDial;

    private double startAngle = 0.0;
    private double initialRotation = 0.0;

    @FXML
    public void initialize() {
        if (safeDial != null) {
            // Store initial angle when mouse is pressed
            safeDial.setOnMousePressed(this::handleMousePressed);
            // Calculate new angle and rotate as mouse is dragged
            safeDial.setOnMouseDragged(this::handleMouseDragged);
        }
    }

    private void handleMousePressed(MouseEvent event) {
        // Find center point of the dial in screen coordinates
        double centerX = safeDial.getBoundsInParent().getWidth() / 2.0;
        double centerY = safeDial.getBoundsInParent().getHeight() / 2.0;

        // Calculate starting angle relative to dial center
        startAngle = Math.toDegrees(Math.atan2(event.getY() - centerY, event.getX() - centerX));
        initialRotation = safeDial.getRotate();
    }

    private void handleMouseDragged(MouseEvent event) {
        double centerX = safeDial.getBoundsInParent().getWidth() / 2.0;
        double centerY = safeDial.getBoundsInParent().getHeight() / 2.0;

        // Calculate current angle relative to dial center
        double currentAngle = Math.toDegrees(Math.atan2(event.getY() - centerY, event.getX() - centerX));

        // Update dial rotation seamlessly
        double deltaAngle = currentAngle - startAngle;
        safeDial.setRotate(initialRotation + deltaAngle);
    }

    /**
     * Helper to retrieve current lock rotation angle (0 - 360 degrees)
     */
    public double getDialRotation() {
        double rotation = safeDial.getRotate() % 360;
        return rotation < 0 ? rotation + 360 : rotation;
    }
}