package com.app.file;

import com.app.controller.GameController;
import com.app.model.Level;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;

/**
 * Save state of game to skb file
 *
 */
public class SaveGame {

    private final Level currentLevel;
    private final Stage primaryStage;
    private final GameController gameController;
    private StringBuilder finalLevel;

    /**
     * constructor for save game
     *
     * @param primaryStage primary stage
     * @param currentLevel the instance of level
     * @param gameController the instance of game controller
     */
    public SaveGame(Stage primaryStage, Level currentLevel,
                    GameController gameController) {
        this.primaryStage = primaryStage;
        this.currentLevel = currentLevel;
        this.gameController = gameController;
    }

    /**
     * @return the path to the default directory containing the map files
     */
    private File getFileDirectory() {
        File folder = new File(
                System.getProperty("user.dir") + "/src/main/resources/saves");

        if (!Files.exists(folder.toPath())) {
            folder = new File(System.getProperty("user.dir"));
        }
        return folder;
    }

    /**
     * function to setup and call the right function to start saving the file
     */
    public void save() {
        parseLevel(currentLevel);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Sokoban File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter
                ("Sokoban save file", "*.skb"));
        fileChooser.setInitialDirectory(getFileDirectory());
        fileChooser.setInitialFileName("Save_"
                + currentLevel.getMapSetName().replaceAll(
                        "[^a-zA-Z0-9_!-]", "")
                + "_" + currentLevel.getIndex() + "_" + gameController.getUsername()
        );

        File saveFile = fileChooser.showSaveDialog(primaryStage);

        if (saveFile != null)
            writeLevelToFile(saveFile);
    }

    /**
     * function to write the level as a file
     *
     * @param file the file to write to
     */
    public void writeLevelToFile(File file) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println("SaveGame");
            pw.println("MoveCount: " + gameController.getMoveCount());
            pw.println("LevelIndex: " + currentLevel.getIndex());
            pw.println("MapSetName: " + currentLevel.getMapSetName());
            pw.println("LevelName: " + currentLevel.getName());
            pw.print(finalLevel);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            System.out.println("Cannot write to file: " + e);
        }
    }

    /**
     * goes over the level to create the right grid.
     *
     * @param currentLevel the instance of level
     */
    public void parseLevel(Level currentLevel) {
        finalLevel = new StringBuilder();
        StringBuilder objectsGrid = new StringBuilder(currentLevel.toString());
        StringBuilder diamondsGrid =
                new StringBuilder(currentLevel.getDiamondsGrid().toString());

        for(int i = 0; i < objectsGrid.length(); i++) {
            if(diamondsGrid.charAt(i) == 'D' && objectsGrid.charAt(i) == 'C') {
                finalLevel.append('O');
            } else if(diamondsGrid.charAt(i) == '=') {
                finalLevel.append(objectsGrid.charAt(i));
            } else {
                finalLevel.append(diamondsGrid.charAt(i));
            }
        }
    }
}
