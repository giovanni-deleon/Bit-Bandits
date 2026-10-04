package vault.crack.bit_bandits_hack;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class LaserHeistController {

    @FXML private Rectangle player;
    @FXML private Rectangle laser1;
    @FXML private Rectangle laser2;
    @FXML private Rectangle laser3;
    @FXML private Rectangle goal;
    @FXML private Label livesLabel;
    @FXML private Label messageLabel;

    private final double SPEED = 10;

    private final double START_X = 40;
    private final double START_Y = 220;

    private int lives = 3;
    private boolean gameOver = false;
    private boolean gameStarted = false;

    // Laser 2 slides left and right
    private double laser2Speed = 1.5;
    private final double LASER2_START_X = 400;
    private final double LASER2_MIN_X = 300;
    private final double LASER2_MAX_X = 500;

    private AnimationTimer laserAnimation;

    // End-screen overlay (win / loss)
    private Pane overlay;
    private final List<Animation> overlayAnimations = new ArrayList<>();

    @FXML
    public void initialize() {

        player.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::handleKeyPress);
            } else if (laserAnimation != null) {
                laserAnimation.stop();
            }
        });

        laserAnimation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameStarted || gameOver) {
                    return;
                }
                moveLaser2();
                checkCollision();
            }
        };
        laserAnimation.start();
    }

    private void moveLaser2() {
        laser2.setX(laser2.getX() + laser2Speed);

        if (laser2.getX() >= LASER2_MAX_X) {
            laser2.setX(LASER2_MAX_X);
            laser2Speed = -Math.abs(laser2Speed);
        } else if (laser2.getX() <= LASER2_MIN_X) {
            laser2.setX(LASER2_MIN_X);
            laser2Speed = Math.abs(laser2Speed);
        }
    }

    public void handleKeyPress(KeyEvent event) {

        if (event.getCode() == KeyCode.R) {
            restartGame();
            return;
        }

        if (!gameStarted && event.getCode() == KeyCode.ENTER) {
            gameStarted = true;
            gameOver = false;
            messageLabel.setText("");
            return;
        }

        if (!gameStarted || gameOver) {
            return;
        }

        switch (event.getCode()) {
            case W: case UP:    player.setY(player.getY() - SPEED); break;
            case S: case DOWN:  player.setY(player.getY() + SPEED); break;
            case A: case LEFT:  player.setX(player.getX() - SPEED); break;
            case D: case RIGHT: player.setX(player.getX() + SPEED); break;
            default: return;
        }

        keepPlayerInside();
        checkCollision();
    }

    private void keepPlayerInside() {
        double maxX = 800 - player.getWidth();
        double maxY = 500 - player.getHeight();

        player.setX(Math.max(0, Math.min(maxX, player.getX())));
        player.setY(Math.max(0, Math.min(maxY, player.getY())));
    }

    private void checkCollision() {
        if (gameOver) return;

        Bounds p = player.getLayoutBounds();

        double margin = 4;
        Bounds playerHitbox = new BoundingBox(
                p.getMinX() + margin,
                p.getMinY() + margin,
                p.getWidth() - margin * 2,
                p.getHeight() - margin * 2
        );

        boolean hitLaser =
                playerHitbox.intersects(laser1.getLayoutBounds()) ||
                        playerHitbox.intersects(laser2.getLayoutBounds()) ||
                        playerHitbox.intersects(laser3.getLayoutBounds());

        if (hitLaser) {
            lives--;
            livesLabel.setText("Lives: " + lives);
            resetPlayer();

            if (lives <= 0) {
                gameOver = true;
                messageLabel.setText("");
                showLossScreen();
            } else {
                messageLabel.setText("LASER HIT! " + lives
                        + (lives == 1 ? " life left" : " lives left"));
            }
            return;
        }

        if (playerHitbox.intersects(goal.getLayoutBounds())) {
            gameOver = true;
            messageLabel.setText("");
            showWinScreen();
        }
    }

    private void resetPlayer() {
        player.setX(START_X);
        player.setY(START_Y);
    }

    private void restartGame() {
        hideOverlay();

        lives = 3;
        gameOver = false;
        gameStarted = false;

        resetPlayer();

        laser2.setX(LASER2_START_X);
        laser2Speed = Math.abs(laser2Speed);

        livesLabel.setText("Lives: 3");
        messageLabel.setText("PRESS ENTER TO START");
    }

    // ================= END SCREENS =================

    private void showWinScreen() {
        hideOverlay();
        overlay = new Pane();
        overlay.setPrefSize(800, 500);

        Rectangle dim = new Rectangle(800, 500, Color.web("#111827", 0.92));
        overlay.getChildren().add(dim);

        // Three glowing nuggets
        Group n1 = makeNugget(260, 190, 1.0, -18);
        Group n2 = makeNugget(400, 165, 1.3, 4);
        Group n3 = makeNugget(545, 195, 1.0, 20);
        overlay.getChildren().addAll(n1, n2, n3);
        floatAndPulse(n1, 0);
        floatAndPulse(n2, 300);
        floatAndPulse(n3, 600);

        Label title = makeText("HEIST COMPLETE!", 40, Color.web("#FFD43B"));
        title.setLayoutY(285);
        centerHorizontally(title);

        Label sub = makeText("You escaped with the glowing chicken nuggets!", 18, Color.WHITE);
        sub.setLayoutY(340);
        centerHorizontally(sub);

        Button again = makeButton("Play Again", "#FFD43B", "#111827");
        again.setLayoutX(320);
        again.setLayoutY(395);

        overlay.getChildren().addAll(title, sub, again);
        showOverlay();
    }

    private void showLossScreen() {
        hideOverlay();
        overlay = new Pane();
        overlay.setPrefSize(800, 500);

        Rectangle dim = new Rectangle(800, 500, Color.web("#1a0508", 0.93));
        overlay.getChildren().add(dim);

        // Simple red "X" badge
        Circle ring = new Circle(400, 175, 62);
        ring.setFill(Color.TRANSPARENT);
        ring.setStroke(Color.web("#FF3B1F"));
        ring.setStrokeWidth(10);

        Rectangle bar1 = new Rectangle(340, 169, 120, 12);
        bar1.setArcWidth(8);
        bar1.setArcHeight(8);
        bar1.setFill(Color.web("#FF3B1F"));
        bar1.setRotate(45);

        Rectangle bar2 = new Rectangle(340, 169, 120, 12);
        bar2.setArcWidth(8);
        bar2.setArcHeight(8);
        bar2.setFill(Color.web("#FF3B1F"));
        bar2.setRotate(-45);

        Group icon = new Group(ring, bar1, bar2);
        icon.setEffect(new DropShadow(30, Color.web("#FF3B1F")));
        overlay.getChildren().add(icon);

        // Pulsing alarm glow
        FadeTransition pulse = new FadeTransition(Duration.seconds(0.6), icon);
        pulse.setFromValue(1.0);
        pulse.setToValue(0.45);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();
        overlayAnimations.add(pulse);

        Label title = makeText("BUSTED!", 44, Color.web("#FF3B1F"));
        title.setLayoutY(265);
        centerHorizontally(title);

        Label sub = makeText("The alarm was triggered. No nuggets for you.", 18, Color.WHITE);
        sub.setLayoutY(330);
        centerHorizontally(sub);

        Button retry = makeButton("Try Again", "#FF3B1F", "#FFFFFF");
        retry.setLayoutX(325);
        retry.setLayoutY(390);

        overlay.getChildren().addAll(title, sub, retry);
        showOverlay();
    }

    private void showOverlay() {
        Pane root = (Pane) player.getParent();
        root.getChildren().add(overlay);

        overlay.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(400), overlay);
        fade.setToValue(1);
        fade.play();
    }

    private void hideOverlay() {
        for (Animation a : overlayAnimations) {
            a.stop();
        }
        overlayAnimations.clear();

        if (overlay != null && overlay.getParent() instanceof Pane) {
            ((Pane) overlay.getParent()).getChildren().remove(overlay);
        }
        overlay = null;
    }

    // ---------- Drawing helpers ----------

    // A chicken nugget: merged blobs, golden gradient, crumbs, and a glow
    private Group makeNugget(double cx, double cy, double scale, double rotation) {
        Shape body = Shape.union(
                Shape.union(new Ellipse(0, 0, 46, 32), new Circle(-28, -6, 26)),
                Shape.union(new Circle(26, 10, 24), new Circle(-5, 18, 22)));

        body.setFill(new RadialGradient(0, 0, 0.4, 0.35, 0.9, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#FFE08A")),
                new Stop(1, Color.web("#D9822B"))));
        body.setStroke(Color.web("#A85A12"));
        body.setStrokeWidth(3);

        Group nugget = new Group(body);

        // Crispy crumb dots
        double[][] crumbs = {{-30, -10}, {-8, -14}, {18, -4}, {32, 14}, {-14, 22}, {6, 8}, {-38, 8}};
        for (double[] c : crumbs) {
            Circle dot = new Circle(c[0], c[1], 3);
            dot.setFill(Color.web("#B8691C", 0.7));
            nugget.getChildren().add(dot);
        }

        nugget.setLayoutX(cx);
        nugget.setLayoutY(cy);
        nugget.setScaleX(scale);
        nugget.setScaleY(scale);
        nugget.setRotate(rotation);

        DropShadow glow = new DropShadow(30, Color.web("#FFD43B"));
        glow.setSpread(0.35);
        nugget.setEffect(glow);

        return nugget;
    }

    // Glow that pulses plus a gentle bobbing motion
    private void floatAndPulse(Group nugget, int delayMs) {
        DropShadow glow = (DropShadow) nugget.getEffect();

        Timeline glowPulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.radiusProperty(), 20)),
                new KeyFrame(Duration.seconds(0.9),
                        new KeyValue(glow.radiusProperty(), 55, Interpolator.EASE_BOTH)));
        glowPulse.setAutoReverse(true);
        glowPulse.setCycleCount(Animation.INDEFINITE);
        glowPulse.setDelay(Duration.millis(delayMs));
        glowPulse.play();

        TranslateTransition bob = new TranslateTransition(Duration.seconds(1.2), nugget);
        bob.setByY(-14);
        bob.setInterpolator(Interpolator.EASE_BOTH);
        bob.setAutoReverse(true);
        bob.setCycleCount(Animation.INDEFINITE);
        bob.setDelay(Duration.millis(delayMs));
        bob.play();

        overlayAnimations.add(glowPulse);
        overlayAnimations.add(bob);
    }

    private Label makeText(String text, double size, Color color) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, size));
        label.setTextFill(color);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    // Centers a label across the 800px-wide screen once its width is known
    private void centerHorizontally(Label label) {
        label.widthProperty().addListener((obs, o, w) ->
                label.setLayoutX((800 - w.doubleValue()) / 2));
    }

    private Button makeButton(String text, String bg, String fg) {
        Button b = new Button(text);
        b.setPrefSize(150, 44);
        b.setFocusTraversable(false); // keeps ENTER/keys going to the game
        b.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;"
                + "-fx-background-color: " + bg + "; -fx-text-fill: " + fg + ";"
                + "-fx-background-radius: 10; -fx-cursor: hand;");
        b.setOnAction(e -> restartGame());
        return b;
    }
}