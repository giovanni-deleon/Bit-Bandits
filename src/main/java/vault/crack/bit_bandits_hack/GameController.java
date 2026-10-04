package vault.crack.bit_bandits_hack;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public class GameController {

    @FXML
    private ImageView safeDial;

    private Rotate rotateTransform;
    private double startAngle = 0.0;

    @FXML
    public void initialize() {
        if (safeDial != null) {
            // Attach a Rotate transform with placeholder pivot (0,0);
            // pivot coordinates will update dynamically on mouse press
            rotateTransform = new Rotate(0, 0, 0);
            safeDial.getTransforms().add(rotateTransform);

            safeDial.setOnMousePressed(this::handleMousePressed);
            safeDial.setOnMouseDragged(this::handleMouseDragged);
        }
    }

    private void handleMousePressed(MouseEvent event) {
        double pivotX = safeDial.getBoundsInLocal().getWidth() / 2.0;
        double pivotY = safeDial.getBoundsInLocal().getHeight() / 2.0;

        rotateTransform.setPivotX(pivotX);
        rotateTransform.setPivotY(pivotY);

        startAngle = calculateAngle(event) - rotateTransform.getAngle();
    }

    private void handleMouseDragged(MouseEvent event) {
        double currentAngle = calculateAngle(event);
        rotateTransform.setAngle(currentAngle - startAngle);
    }

    private double calculateAngle(MouseEvent event) {
        double localCenterX = safeDial.getBoundsInLocal().getWidth() / 2.0;
        double localCenterY = safeDial.getBoundsInLocal().getHeight() / 2.0;

        Point2D centerInScene = safeDial.localToScene(localCenterX, localCenterY);


        double deltaX = event.getSceneX() - centerInScene.getX();
        double deltaY = event.getSceneY() - centerInScene.getY();

        return Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    /**
     * Calculates angle using Scene coordinates so mouse positions
     * remain static relative to window bounds during rotation.
     */
    private double calculateAngleInSceneSpace() {
        double localCenterX = safeDial.getBoundsInLocal().getWidth() / 2.0;
        double localCenterY = safeDial.getBoundsInLocal().getHeight() / 2.0;

        // Convert center pivot to Scene/Window coordinates
        Point2D centerInScene = safeDial.localToScene(localCenterX, localCenterY);

        // Track cursor relative to static scene center
        double deltaX = safeDial.getScene().getX() - centerInScene.getX();
        // Note: use local cursor location transformed to scene space
        return 0; // Handled cleanly below via event-driven position
    }

    /**
     * Helper to retrieve current lock rotation angle normalized between (0 - 360 degrees)
     */
    public double getDialRotation() {
        double rotation = rotateTransform.getAngle() % 360;
        return rotation < 0 ? rotation + 360 : rotation;
    }
}