package com.app.core;

import com.app.engine.GameLogger;
import com.app.model.Movement;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Game loader to help with loading the game skb files
 *
 */
public class GameLoader {
    private File saveFile;

    /**
     * returns the save file used to be loaded in
     *
     * @return save file
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * function to set the default save file when game is started through start game
     */
    public void loadDefaultSaveFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            URL resource = classLoader.getResource("SampleGame.skb");
            if (resource == null) {
                throw new FileNotFoundException("File cannot be found.");
            }
            this.saveFile = new File(resource.toURI());
        } catch (FileNotFoundException | URISyntaxException e) {
            System.out.println("File cannot be found." + e);
        }
    }

    /**
     * calls the load game function
     *
     * @param movement instance of movement
     * @param gameLoaded boolean to check if game is loaded
     */
    public void loadGame(Movement movement, boolean gameLoaded) {
        movement.removeEventFilter();
        if (!gameLoaded) {
            movement.setEventFilter();
        }
    }

    /**
     *
     *
     * @param debugActive boolean to check debug status
     * @param primaryStage the primary stage
     * @param logger instance of logger
     * @return true or false, depending on whether a file was loaded or not
     */
    public boolean loadGameFile(
            boolean debugActive, Stage primaryStage, GameLogger logger) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter
                ("Sokoban save file", "*.skb"));
        fileChooser.setInitialDirectory(getFileDirectory());

        saveFile = fileChooser.showOpenDialog(primaryStage);

        if (saveFile != null) {
            if (debugActive) {
                logger.info("Loading save file: " + saveFile.getName());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * sets the default directory of file viewer windows
     *
     * @return the correct file object
     */
    private File getFileDirectory() {
        File folder = new File(
                System.getProperty("user.dir") + "/src/main/resources/maps");

        if (!Files.exists(folder.toPath())) {
            folder = new File(System.getProperty("user.dir"));
        }
        return folder;
    }
}