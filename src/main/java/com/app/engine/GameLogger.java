package com.app.engine;

import com.app.controller.GameController;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A game logger, logs errors and outputs to file
 *
 */
public class GameLogger extends Logger {

    private static final Logger logger = Logger.getLogger("GameLogger");
    private final DateFormat dateFormat =
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Calendar calendar = Calendar.getInstance();

    /**
     * constructor for game loggers
     *
     * @throws IOException catches file handler exceptions
     */
    public GameLogger() throws IOException {
        super("GameLogger", null);

        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(
                directory + "/" + GameController.GAME_NAME + ".log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        fh.close();
    }

    /**
     * function to create the logger message
     *
     * @param message message as a string
     * @return the date and message
     */
    private String createFormattedMessage(String message) {
        return dateFormat.format(calendar.getTime()) + " -- " + message;
    }

    /**
     * logger helper method
     *
     * @param message message as string
     */
    public void info(String message) {
        logger.info(createFormattedMessage(message));
    }

    /**
     * logger helper method
     *
     * @param message message as string
     */
    public void warning(String message) {
        logger.warning(createFormattedMessage(message));
    }

    /**
     * logger helper method
     *
     * @param message message as string
     */
    public void severe(String message) {
        logger.severe(createFormattedMessage(message));
    }
}