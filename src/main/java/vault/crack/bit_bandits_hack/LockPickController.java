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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class LockPickController {

    @FXML private Pane lockChamber;
    @FXML private Group pickTool;

    @FXML private Rectangle shaftTrack;
    @FXML private Rectangle notch1;
    @FXML private Rectangle notch2;
    @FXML private Rectangle notch3;

    @FXML private Circle pin1;
    @FXML private Circle pin2;
    @FXML private Circle pin3;

    @FXML private Label statusLabel;
    @FXML private Button nextLevelButton;

    private boolean set1 = false;
    private boolean set2 = false;
    private boolean set3 = false;

    // Default starting position for reset
    private double startX;
    private double startY;

    @FXML
    public void initialize() {
        startX = pickTool.getLayoutX();
        startY = pickTool.getLayoutY();

        pickTool.setOnMouseDragged(this::handleMouseDragged);
        pickTool.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMouseDragged(MouseEvent event) {
        // Translate pick relative to mouse position
        double newX = event.getSceneX() - pickTool.getBoundsInParent().getWidth() / 2.0;
        double newY = event.getSceneY();

        pickTool.setLayoutX(newX);
        pickTool.setLayoutY(newY);

        checkCollisionsAndNotches();
    }

    private void handleMouseReleased(MouseEvent event) {
        // Drop pick back to main channel if sequence not complete
        if (!(set1 && set2 && set3)) {
            pickTool.setLayoutY(startY);
        }
    }

    private void checkCollisionsAndNotches() {
        // Track the tip of the lock pick (-220px offset from group origin)
        double tipX = pickTool.getLayoutX() - 220.0;
        double tipY = pickTool.getLayoutY();

        Point2D tipPoint = new Point2D(tipX, tipY);

        // 1. Check if inside valid keyway bounds
        boolean inShaft = shaftTrack.getBoundsInParent().contains(tipPoint);
        boolean inNotch1 = notch1.getBoundsInParent().contains(tipPoint);
        boolean inNotch2 = notch2.getBoundsInParent().contains(tipPoint);
        boolean inNotch3 = notch3.getBoundsInParent().contains(tipPoint);

        if (!inShaft && !inNotch1 && !inNotch2 && !inNotch3) {
            // Visual mess up -> Restart
            statusLabel.setText("MESS UP! Hit lock wall - Position reset!");
            statusLabel.setTextFill(Color.web("#ff4444"));
            resetPick();
            return;
        }

        // 2. Check top notch completions (Pin connections)
        if (!set1 && inNotch1 && tipY <= notch1.getLayoutY() + 40) {
            set1 = true;
            pin1.setFill(Color.web("#00ff66"));
            statusLabel.setText("Pin 1 Set!");
            statusLabel.setTextFill(Color.web("#00ff66"));
        }

        if (!set2 && inNotch2 && tipY <= notch2.getLayoutY() + 40) {
            set2 = true;
            pin2.setFill(Color.web("#00ff66"));
            statusLabel.setText("Pin 2 Set!");
            statusLabel.setTextFill(Color.web("#00ff66"));
        }

        if (!set3 && inNotch3 && tipY <= notch3.getLayoutY() + 40) {
            set3 = true;
            pin3.setFill(Color.web("#00ff66"));
            statusLabel.setText("Pin 3 Set!");
            statusLabel.setTextFill(Color.web("#00ff66"));
        }

        // 3. Unlock condition
        if (set1 && set2 && set3) {
            statusLabel.setText("LOCK PICKED! Proceeding enabled.");
            statusLabel.setTextFill(Color.web("#00ff66"));
            nextLevelButton.setDisable(false);
        }
    }

    private void resetPick() {
        pickTool.setLayoutX(startX);
        pickTool.setLayoutY(startY);
    }

    @FXML
    public void switchToLaserHeist(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lock-pick.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load /view/laser-heist.fxml");
        }
    }
}