package vault.crack.bit_bandits_hack;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

public class LaserHeistController {

    @FXML
    private Rectangle player;

    @FXML
    private Rectangle laser1;

    @FXML
    private Rectangle laser2;

    @FXML
    private Rectangle laser3;

    @FXML
    private Rectangle goal;

    @FXML
    private Label livesLabel;

    @FXML
    private Label messageLabel;

    private final double SPEED = 10;

    private final double START_X = 40;
    private final double START_Y = 220;

    // Player starts with 3 lives
    private int lives = 3;

    // Game states
    private boolean gameOver = false;
    private boolean gameStarted = false;

    // LASER 2 MOVEMENT
    private double laser2Speed = 1.0;

    private final double LASER2_START_X = 400;
    private final double LASER2_MIN_X = 300;
    private final double LASER2_MAX_X = 500;


    // Starts automatic movement for Laser 2
    @FXML
    public void initialize() {

        AnimationTimer laser2Animation = new AnimationTimer() {

            @Override
            public void handle(long now) {

                // Laser only moves while game is running
                if (!gameStarted || gameOver) {
                    return;
                }

                moveLaser2();
            }
        };

        laser2Animation.start();
    }


    private void moveLaser2() {

        // Move Laser 2 left and right
        laser2.setX(laser2.getX() + laser2Speed);

        // Right limit
        if (laser2.getX() >= LASER2_MAX_X) {

            laser2.setX(LASER2_MAX_X);
            laser2Speed = -Math.abs(laser2Speed);
        }

        // Left limit
        else if (laser2.getX() <= LASER2_MIN_X) {

            laser2.setX(LASER2_MIN_X);
            laser2Speed = Math.abs(laser2Speed);
        }
    }


    public void handleKeyPress(KeyEvent event) {

        // R can restart the game at any time
        if (event.getCode() == KeyCode.R) {
            restartGame();
            return;
        }

        // ENTER starts the game
        if (!gameStarted && event.getCode() == KeyCode.ENTER) {

            gameStarted = true;
            gameOver = false;

            messageLabel.setText("");
            return;
        }

        // Do not move until the game starts
        if (!gameStarted) {
            return;
        }

        // Do not move after winning or losing
        if (gameOver) {
            return;
        }

        // PLAYER MOVEMENT
        switch (event.getCode()) {

            case W:
                player.setY(player.getY() - SPEED);
                break;

            case S:
                player.setY(player.getY() + SPEED);
                break;

            case A:
                player.setX(player.getX() - SPEED);
                break;

            case D:
                player.setX(player.getX() + SPEED);
                break;

            default:
                return;
        }

        // Keep player inside the screen
        keepPlayerInside();

        // Check lasers and loot
        checkCollision();
    }


    private void keepPlayerInside() {

        double minX = 0;
        double minY = 0;

        double maxX = 800 - player.getWidth();
        double maxY = 500 - player.getHeight();

        if (player.getX() < minX) {
            player.setX(minX);
        }

        if (player.getX() > maxX) {
            player.setX(maxX);
        }

        if (player.getY() < minY) {
            player.setY(minY);
        }

        if (player.getY() > maxY) {
            player.setY(maxY);
        }
    }


    private void checkCollision() {

        /*
         * The visual player is 30x30.
         *
         * We make the real hitbox slightly smaller.
         * This prevents losing a life when the player
         * only barely touches the glow/edge of a laser.
         */
        Bounds playerBounds = player.getBoundsInParent();

        double margin = 20;

        Bounds playerHitbox = new BoundingBox(
                playerBounds.getMinX() + margin,
                playerBounds.getMinY() + margin,
                playerBounds.getWidth() - (margin * 2),
                playerBounds.getHeight() - (margin * 2)
        );

        // Check all three lasers
        boolean hitLaser =
                playerHitbox.intersects(laser1.getBoundsInParent()) ||
                        playerHitbox.intersects(laser2.getBoundsInParent()) ||
                        playerHitbox.intersects(laser3.getBoundsInParent());

        if (hitLaser) {

            lives--;

            livesLabel.setText("Lives: " + lives);

            System.out.println("LASER HIT!");
            System.out.println("Lives remaining: " + lives);

            // Return player to starting position
            resetPlayer();

            // No lives remaining
            if (lives <= 0) {

                gameOver = true;

                messageLabel.setText(
                        "ALARM TRIGGERED - GAME OVER!"
                );

                System.out.println("ALARM TRIGGERED!");
                System.out.println("GAME OVER!");
            }

            return;
        }


        // Check if player reached the loot
        if (playerHitbox.intersects(goal.getBoundsInParent())) {

            gameOver = true;

            messageLabel.setText("HEIST COMPLETE!");

            System.out.println("HEIST COMPLETE!");
        }
    }


    private void resetPlayer() {

        player.setX(START_X);
        player.setY(START_Y);
    }


    private void restartGame() {

        // Restore lives
        lives = 3;

        // Reset game state
        gameOver = false;
        gameStarted = false;

        // Return player to starting position
        resetPlayer();

        // Reset Laser 2 to original position
        laser2.setX(LASER2_START_X);

        // Start moving right again
        laser2Speed = Math.abs(laser2Speed);

        // Restore UI
        livesLabel.setText("Lives: 3");
        messageLabel.setText("PRESS ENTER TO START");

        System.out.println("GAME RESTARTED!");
    }
}