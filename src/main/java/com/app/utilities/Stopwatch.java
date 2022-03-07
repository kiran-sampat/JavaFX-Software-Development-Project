package com.app.utilities;

import com.app.controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Timer for use within the game scene
 *
 */
public class Stopwatch {

    private static Stopwatch single_instance;
    private final GameController gameController;
    private int mins = 0;
    private int secs = 0;
    private int millis = 0;
    private Timeline timeline;
    private Label text;

    /**
     * constructor for the stopwatch class
     *
     * @param gameController instance of game controller
     */
    public Stopwatch(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * function to update the timer
     *
     * @param text the label to set
     */
    public void update(Label text) {
        millis++;
        if (millis == 1000) {
            secs++;
            millis = 0;
        }
        if (secs == 60) {
            mins++;
            secs = 0;
        }

        text.setText(
                (((mins / 10) == 0) ? "0" : "") + mins + ":"
                        + (((secs / 10) == 0) ? "0" : "") + secs + ":"
                        + (((millis / 100) == 0) ? "0" : "") + millis / 10
        );

        setTimeDisplay();
    }

    /**
     * timer initialization function
     */
    public void start() {
        text = new Label("00:00:00");
        timeline = new Timeline(new KeyFrame(
                Duration.millis(1), event -> update(text)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        play();
    }

    /**
     * start the timeline
     */
    public void play() {
        timeline.play();
    }

    /**
     * pause timeline, used by reset
     */
    public void pause() {
        timeline.pause();
    }

    /**
     * function to reset the time display
     */
    public void reset() {
        mins = 0;
        secs = 0;
        millis = 0;
        pause();
        text.setText("00:00:00");
        setTimeDisplay();
    }

    /**
     * function to set the time display
     */
    private void setTimeDisplay() {
        String s = text.getText();
        gameController.setTimeDisplay(s);
    }

    /**
     * function to get the text from the time label
     *
     * @return time as a string
     */
    public String getText() {
        return text.getText();
    }
}
