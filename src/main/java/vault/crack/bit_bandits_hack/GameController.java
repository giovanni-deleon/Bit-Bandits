package vault.crack.bit_bandits_hack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class GameController {

    @FXML
    private Group safeDial;

    private final Rotate rotateTransform = new Rotate(0, 0, 0);
    private double startAngle = 0.0;
    private Point2D fixedCenterInScene;

    // Vault combination logic
    private final double[] COMBINATION = {90.0, 220.0, 45.0}; // 3 combination points (degrees)
    private final double TOLERANCE = 10.0;                   // Acceptable offset angle (+/- 10 degrees)
    private int currentCombinationIndex = 0;
    private boolean combinationReached = false;

    @FXML
    public void initialize() {
        if (safeDial != null) {
            safeDial.getTransforms().add(rotateTransform);

            safeDial.setOnMousePressed(this::handleMousePressed);
            safeDial.setOnMouseDragged(this::handleMouseDragged);
            safeDial.setOnMouseReleased(this::handleMouseReleased);
        }
    }

    private void handleMousePressed(MouseEvent event) {
        Bounds bounds = safeDial.localToScene(safeDial.getLayoutBounds());
        fixedCenterInScene = new Point2D(
                bounds.getMinX() + bounds.getWidth() / 2.0,
                bounds.getMinY() + bounds.getHeight() / 2.0
        );

        startAngle = calculateAngle(event) - rotateTransform.getAngle();
        combinationReached = false;
    }

    private void handleMouseDragged(MouseEvent event) {
        if (fixedCenterInScene == null) return;

        double currentAngle = calculateAngle(event);
        rotateTransform.setAngle(currentAngle - startAngle);

        checkCombinationPoint(getDialRotation());
    }

    private void handleMouseReleased(MouseEvent event) {
        if (combinationReached) {
            currentCombinationIndex++;
            System.out.println("Combination step reached: " + currentCombinationIndex + " / " + COMBINATION.length);

            if (currentCombinationIndex >= COMBINATION.length) {
                System.out.println("Vault Unlocked!");
                triggerSceneSwitch(event);
            }
            combinationReached = false;
        }
    }

    private void checkCombinationPoint(double currentRotation) {
        if (currentCombinationIndex < COMBINATION.length) {
            double target = COMBINATION[currentCombinationIndex];
            double diff = Math.abs(currentRotation - target);

            if (diff <= TOLERANCE || (360 - diff) <= TOLERANCE) {
                combinationReached = true;
            }
        }
    }

    private double calculateAngle(MouseEvent event) {
        double deltaX = event.getSceneX() - fixedCenterInScene.getX();
        double deltaY = event.getSceneY() - fixedCenterInScene.getY();
        return Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    public double getDialRotation() {
        double rotation = rotateTransform.getAngle() % 360;
        return rotation < 0 ? rotation + 360 : rotation;
    }

    private URL getLockPickResource() {
        URL resource = getClass().getResource("/view/lock-pick.fxml");
        if (resource == null) {
            resource = getClass().getResource("/lock-pick.fxml");
        }
        return resource;
    }

    private void triggerSceneSwitch(MouseEvent event) {
        try {
            URL fxmlLocation = getLockPickResource();
            if (fxmlLocation == null) {
                throw new IOException("Could not resolve lock-pick.fxml path.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load lock-pick.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void switchToLockPick(ActionEvent event) {
        try {
            URL fxmlLocation = getLockPickResource();
            if (fxmlLocation == null) {
                throw new IOException("Could not resolve lock-pick.fxml path.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load lock-pick.fxml: " + e.getMessage());
        }
    }
}
