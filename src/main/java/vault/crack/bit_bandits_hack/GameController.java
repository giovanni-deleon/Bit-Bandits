package vault.crack.bit_bandits_hack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {

    @FXML
    private ImageView safeDial;

    private Rotate rotateTransform;
    private double startAngle = 0.0;

    @FXML
    public void initialize() {
        if (safeDial != null) {
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

        Point2D centerInParent = safeDial.localToParent(localCenterX, localCenterY);

        Node sourceNode = (Node) event.getSource();
        Point2D mouseInParent = sourceNode.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());

        double deltaX = mouseInParent.getX() - centerInParent.getX();
        double deltaY = mouseInParent.getY() - centerInParent.getY();

        return Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    public double getDialRotation() {
        double rotation = rotateTransform.getAngle() % 360;
        return rotation < 0 ? rotation + 360 : rotation;
    }

    /**
     * Transitions from Level 1 to Lock Pick mini-game.
     */
    @FXML
    public void switchToLockPick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/lock-pick.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load /view/lock-pick.fxml");
        }
    }
}