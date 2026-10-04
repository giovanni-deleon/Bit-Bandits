package vault.crack.bit_bandits_hack;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LockPickController {

    @FXML private Pane lockChamber;
    @FXML private Group pickTool;
    @FXML private ImageView pickImage;
    @FXML private Circle pickTipPoint;

    // Header UI indicators
    @FXML private Circle pin1;
    @FXML private Circle pin2;
    @FXML private Circle pin3;

    // Target dots on the keyhole track
    @FXML private Circle visualPin1;
    @FXML private Circle visualPin2;
    @FXML private Circle visualPin3;

    @FXML private Label statusLabel;
    @FXML private Button nextLevelButton;

    private boolean set1 = false;
    private boolean set2 = false;
    private boolean set3 = false;
    private boolean switching = false;

    private double dragOffsetX;
    private double dragOffsetY;

    // Quick guesses, checked before the automatic scan
    private static final String[] LASER_FXML_PATHS = {
            "/view/laser-heist.fxml",
            "/view/laser-heist-view.fxml",
            "/view/laser_heist.fxml",
            "/view/LaserHeist.fxml",
            "/laser-heist.fxml",
            "/vault/crack/bit_bandits_hack/laser-heist.fxml",
            "/vault/crack/bit_bandits_hack/view/laser-heist.fxml"
    };

    @FXML
    public void initialize() {
        visualPin1.setVisible(true);
        visualPin2.setVisible(false);
        visualPin3.setVisible(false);

        pickTool.setOnMousePressed(this::handleMousePressed);
        pickTool.setOnMouseDragged(this::handleMouseDragged);
    }

    private void handleMousePressed(MouseEvent event) {
        dragOffsetX = event.getSceneX() - pickTool.getLayoutX();
        dragOffsetY = event.getSceneY() - pickTool.getLayoutY();
    }

    private void handleMouseDragged(MouseEvent event) {
        pickTool.setLayoutX(event.getSceneX() - dragOffsetX);
        pickTool.setLayoutY(event.getSceneY() - dragOffsetY);

        checkTipIntersection();
    }

    private void checkTipIntersection() {
        Point2D sceneTip = pickTipPoint.localToScene(
                pickTipPoint.getCenterX(), pickTipPoint.getCenterY());
        Point2D tip = lockChamber.sceneToLocal(sceneTip);

        // 1. First green dot
        if (!set1 && visualPin1.isVisible() && isTipTouchingCircle(tip, visualPin1)) {
            set1 = true;

            pin1.setFill(Color.web("#00ff66"));
            visualPin1.setVisible(false);
            visualPin2.setVisible(true);

            statusLabel.setText("First Pin Set! Move to the middle green dot.");
            statusLabel.setTextFill(Color.web("#00ff66"));
            return;
        }

        // 2. Second green dot
        if (set1 && !set2 && visualPin2.isVisible() && isTipTouchingCircle(tip, visualPin2)) {
            set2 = true;

            pin2.setFill(Color.web("#00ff66"));
            visualPin2.setVisible(false);
            visualPin3.setVisible(true);

            statusLabel.setText("Second Pin Set! Move left to the final green dot.");
            statusLabel.setTextFill(Color.web("#00ff66"));
            return;
        }

        // 3. Final green dot -> auto-switch to laser heist
        if (set1 && set2 && !set3 && visualPin3.isVisible() && isTipTouchingCircle(tip, visualPin3)) {
            set3 = true;

            pin3.setFill(Color.web("#00ff66"));
            visualPin3.setVisible(false);

            statusLabel.setText("LOCK PICKED! Entering the Laser Heist...");
            statusLabel.setTextFill(Color.web("#00ff66"));
            nextLevelButton.setDisable(false);

            PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
            delay.setOnFinished(e -> loadLaserHeist());
            delay.play();
        }
    }

    private boolean isTipTouchingCircle(Point2D tipPoint, Circle targetCircle) {
        double dx = tipPoint.getX() - targetCircle.getCenterX();
        double dy = tipPoint.getY() - targetCircle.getCenterY();
        double touchThreshold = targetCircle.getRadius() + 20.0;

        return (dx * dx + dy * dy) <= (touchThreshold * touchThreshold);
    }

    // ---------- Finding the laser FXML without changing your folders ----------

    // Lists every .fxml in the compiled resources, as paths like "view/lock-pick.fxml"
    private List<String> scanAllFxml() {
        List<String> found = new ArrayList<>();
        try {
            URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
            Path root = Paths.get(location.toURI());

            if (Files.isDirectory(root)) {
                try (Stream<Path> walk = Files.walk(root)) {
                    found = walk
                            .filter(p -> p.toString().toLowerCase().endsWith(".fxml"))
                            .map(p -> root.relativize(p).toString().replace('\\', '/'))
                            .collect(Collectors.toList());
                }
            } else {
                try (JarFile jar = new JarFile(root.toFile())) {
                    found = jar.stream()
                            .map(JarEntry::getName)
                            .filter(n -> n.toLowerCase().endsWith(".fxml"))
                            .collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    private URL findLaserHeistFxml() {
        // 1. Known paths
        for (String path : LASER_FXML_PATHS) {
            URL url = getClass().getResource(path);
            if (url != null) {
                System.out.println("Found laser FXML at: " + path);
                return url;
            }
        }

        // 2. Scan for any FXML with "laser" in the name
        List<String> all = scanAllFxml();
        for (String rel : all) {
            if (rel.toLowerCase().contains("laser")) {
                URL url = getClass().getResource("/" + rel);
                if (url != null) {
                    System.out.println("Found laser FXML by scan at: /" + rel);
                    return url;
                }
            }
        }

        System.err.println("No FXML with 'laser' in its name was found.");
        System.err.println("FXML files that DO exist in your resources: " + all);
        return null;
    }

    private void loadLaserHeist() {
        if (switching) return;
        switching = true;

        try {
            URL fxmlLocation = findLaserHeistFxml();
            if (fxmlLocation == null) {
                throw new IllegalStateException(
                        "Laser Heist FXML not found (check the Run console for the list of FXML files)");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Stage stage = (Stage) lockChamber.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            switching = false;
            e.printStackTrace();

            Throwable cause = e;
            while (cause.getCause() != null) cause = cause.getCause();
            statusLabel.setText("Could not open Laser Heist: " + cause.getMessage());
            statusLabel.setTextFill(Color.web("#ff4444"));
        }
    }

    // "Proceed to Laser Heist" button (backup)
    @FXML
    public void switchToLaserHeist(ActionEvent event) {
        loadLaserHeist();
    }
}