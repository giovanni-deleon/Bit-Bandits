package controller;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
<<<<<<< HEAD
    private GameState state;
    private JoyconInput input;
    private SerialController serial;
    private GamePanel panel;
    private Timer loop;

    private static final int DELAY_MS = 16;

    public GameController(GameState state, JoyconInput input, SerialController serial, GamePanel panel) {
        this.state = state;
        this.input = input;
        this.serial = serial;
        this.panel = panel;

        this.loop = new Timer(DELAY_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
    }
    public void startGame() {
        if (!loop.isRunning()) {
            loop.start();
        }
    }

    public void pauseGame() {
        if (loop.isRunning()) {
            loop.stop();
        }
    }

    public void stopGame() {
        if (loop.isRunning()) {
            loop.stop();
        }

        if (state != null) {
            state.reset();
        }
    }

    private void update() {
        handleInput();

        if (state != null) {
            state.update();
            checkLevelComplete();
        }

        if (panel != null) {
            panel.repaint();
        }
    }

    private void handleInput() {
        if (input == null) {
            return;
        }

        input.poll();
    }

    private void checkLevelComplete() {
        if (state != null && state.isLevelComplete()) {
            // Signal external hardware/peripherals via serial if necessary
            if (serial != null) {
                serial.sendSignal("LEVEL_COMPLETE");
            }
            state.advanceLevel();
        }
    }

    // Getters and Setters
    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public JoyconInput getInput() {
        return input;
    }

    public void setInput(JoyconInput input) {
        this.input = input;
    }

    public SerialController getSerial() {
        return serial;
    }

    public void setSerial(SerialController serial) {
        this.serial = serial;
    }

    public GamePanel getPanel() {
        return panel;
    }

    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }

    public Timer getLoop() {
        return loop;
    }
=======

        >>>>>>> fdecd9f101032c2a8076d88f15cc6a73f07e32c9
}