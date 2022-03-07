package com.app.controller;

import com.app.core.GameLoader;
import com.app.engine.GameLogger;
import com.app.engine.GameObject;
import com.app.file.SaveGame;
import com.app.fx.GraphicObject;
import com.app.model.Level;
import com.app.model.Movement;
import com.app.scores.Scoreboard;
import com.app.utilities.Audio;
import com.app.utilities.Notification;
import com.app.utilities.Stopwatch;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The game model and controller
 *
 */
public class GameController {

    private File saveFile;

    private Stage primaryStage;

    @FXML private GridPane gameGrid;
    @FXML private GridPane displayGrid;
    @FXML private GridPane boardGrid;
    @FXML private Label levelDisplay;
    @FXML private Label timeDisplay;
    @FXML private Label movesDisplay;
    @FXML private MenuBar MENU;

    public static final String GAME_NAME = "BestSokobanEverV6";
    public static GameLogger logger;
    private static boolean debug = false;
    private Level currentLevel;
    private String mapSetName;
    private String username;
    private List<Level> levels;
    private boolean gameComplete;
    private int moveCount;
    private int levelCount;
    private boolean resetLevelOn;
    private boolean moveComplete = false;

    private Movement movement;
    private Stopwatch timer;

    private boolean saveGameOn;
    private boolean undoMoveOn;

    private final GameLoader loader = new GameLoader();

    private void setLevelDisplay(int levelNumber) {
        this.levelDisplay.setText("Level: " + levelNumber);
    }

    public void setTimeDisplay(String timeDisplay) {
        this.timeDisplay.setText("Time: " + timeDisplay);
    }

    public void setMovesDisplay(int moveCount) {
        this.movesDisplay.setText("Moves: " + moveCount);
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getUsername() {
        return username;
    }

    public Stopwatch getTimer() {
        return timer;
    }

    public boolean isMoveComplete() {
        return moveComplete;
    }

    public void setMoveComplete(boolean moveComplete) {
        this.moveComplete = moveComplete;
    }

    /**
     * sets save game status
     *
     * @param saveGameOn boolean to act as a flag
     */
    public void setSaveGameOn(boolean saveGameOn) {
        this.saveGameOn = saveGameOn;
    }

    /**
     * @return the number of moves the player made
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * sets the move count on the dashboard
     *
     * @param moveCount moves as an integer
     */
    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    /**
     * resets move counter to 0
     */
    public void resetMoveCounter() {
        moveCount = 0;
    }

    /**
     * @return the current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Function to set the current level
     *
     * @param currentLevel the instance of Level
     */
    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    /**
     * @return true or false depending on whether game is complete or not
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    /**
     * function to return the next level of the map set
     *
     * @return null
     */
    public Level getNextLevel() {
        if (currentLevel == null) {
            return levels.get(0);
        }

        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex < levels.size()) {
            //remove + 1 to fix level skip bug
            return levels.get(currentLevelIndex);
        }

        gameComplete = true;
        return null;
    }

    /**
     * @return the name of the map set read from the .skb file
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * Keep a record of level count in GameController, for use with reset level,
     * as data in the Level class will be wiped, when reset level is called
     */
    private void setLevelCount() {
        this.levelCount = currentLevel.getIndex();
    }

    /**
     * the function to initialize the game once the fxml is loaded
     *
     * @param input the text from the .skb file, which contains the maps, as an input stream
     */
    public void initializeGame(InputStream input) {
        gridColorInit();

        try {
            setSaveGameOn(false);

            GraphicObject.setDefaultPathKeeper();

            gameComplete = false;
            ScoreController.levelComplete = false;

            if(!undoMoveOn) {
                timer = new Stopwatch(this);
                timer.start();
                //System.out.println("timer start");

            } else {
                //System.out.println("set false");
                undoMoveOn = false;
            }

            resetMoveCounter();

            logger = new GameLogger();
            levels = parseLevels(input);

            //todo reset level in start me up
            currentLevel = null;
            if(!resetLevelOn) {
                currentLevel = getNextLevel();
                ScoreController.levelComplete = false;
            } else {
                if(saveGameOn) {
                    currentLevel = levels.get(0);
                } else {
                    currentLevel = levels.get(levelCount - 1);
                }
                ScoreController.levelComplete = false;
                resetLevelOn = false;
            }

            movement = new Movement(this, this.currentLevel);
            movement.setEventFilter();
            movement.spriteTimer();

        } catch (IOException e) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: "
                    + Arrays.toString(e.getStackTrace()));
        }

        reloadGrid();
    }

    /**
     * Function to set the color of the gameGrid to
     * the same color as the selected floor color
     * so when a graphic object moves on the grid
     * game experience would be improved
     */
    private void gridColorInit() {
        if(SettingsController.selectTypeFloor == 0)
            boardGrid.setStyle("-fx-background-color: White");

        if(SettingsController.selectTypeFloor == 1) {
            String webFormat = String.format("#%02x%02x%02x",
                    (int) (255 * SettingsController.selectFloorColor.getRed()),
                    (int) (255 * SettingsController.selectFloorColor.getGreen()),
                    (int) (255 * SettingsController.selectFloorColor.getBlue()));
            boardGrid.setStyle("-fx-background-color: " + webFormat + ";");
        }

        if(SettingsController.selectTypeFloor == 2) {
            switch (GraphicObject.pathFloor) {
                case "floorGreen.png":
                    boardGrid.setStyle("-fx-background-color: #659F3E");
                    break;
                case "floorBrown.png":
                    boardGrid.setStyle("-fx-background-color: #B7916A");
                    break;
                case "floorGray.png":
                    boardGrid.setStyle("-fx-background-color: #989AA7");
                    break;
                case "floorSand.png":
                    boardGrid.setStyle("-fx-background-color: #ECE3CE");
                    break;
            }
        }
    }

    /**
     * @param input the text from the .skb file, which contains the maps, as an input stream
     * @return the list of levels read from the .skb file
     */
    private List<Level> parseLevels(InputStream input) {
        List<Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            mapSetName = "";
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                // Break the loop if EOF is reached
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level(mapSetName, levelName,
                                ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("SaveGame")) {
                    //SaveGame in the file, indicated that it is a save game state
                    setSaveGameOn(true);
                    continue;
                }

                if (line.contains("MoveCount")) {
                    moveCount = Integer.parseInt(
                            line.replace("MoveCount: ", ""));
                    setMoveComplete(true);
                    continue;
                }

                if (line.contains("LevelIndex")) {
                    levelIndex = Integer.parseInt(
                            line.replace("LevelIndex: ", "")) - 1;
                    continue;
                }

                if (line.contains("MapSetName")) {
                    mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) {
                        Level parsedLevel = new Level(mapSetName,
                                levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }

                    levelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                // If the line contains at least 2 WALLS, add it to the list
                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }

        } catch (IOException e) {
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }

        return levels;
    }

    /**
     * function to load the default save file, which is when the user presses start game
     *
     * @throws IOException because function uses a file input stream
     */
    void loadDefaultSaveFile() throws IOException {
        this.loader.loadDefaultSaveFile();
        this.saveFile = this.loader.getSaveFile();
        initializeGame(new FileInputStream(saveFile));
    }

    /**
     * loadGame function, to be called from the gameScene window,
     * removes event filter from old gameScene window and adds it
     * back if a new file is not loaded
     *
     * @throws IOException because function calls loadGameFile
     */
    public void loadGame() throws IOException {
        loader.loadGame(this.movement, this.loadGameFile());
    }

    /**
     * @return true or false, depending on whether a file was loaded or not
     * @throws IOException when the file cannot be found or loaded correctly
     */
    public boolean loadGameFile() throws IOException {
        boolean loadGameFile =
                this.loader.loadGameFile(debug, this.primaryStage, logger);
        this.saveFile = this.loader.getSaveFile();
        if (loadGameFile) {
            initializeGame(new FileInputStream(saveFile));
        }
        return loadGameFile;
    }

    public void reloadGrid() {
        setLevelCount();
        setLevelDisplay(currentLevel.getIndex());

        if (isMoveComplete()) {
            setMovesDisplay(getMoveCount()); //todo Added this
        }
        movement.listContents();

        /*System.out.println(
                mapSetName + " | " + currentLevel.getName() + " | "
                        + "Level: " + currentLevel.getIndex() + " | Moves: "
                        + getMoveCount() + " | " + getUsername()
        );*/

        if (isGameComplete()) {
            try {
                draw();
                movement.removeEventFilter();
                movement.spriteTimerStop();
                showScoreBoard();
                timer.reset();
                return;
            } catch (IOException e) {
                System.out.println("Error showing scoreboard.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (ScoreController.isLevelComplete()) {
            try {
                draw();
                movement.spriteTimerStop();
                showScoreBoard();
                timer.reset();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        draw();
    }

    /**
     * Function to redraw every object on the board grid
     */
    public void draw() {
        Level currentLevel = getCurrentLevel();
        Level.LevelIterator levelGridIterator =
                (Level.LevelIterator) currentLevel.iterator();
        boardGrid.getChildren().clear();

        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(),
                    levelGridIterator.getCurrentPosition());
        }
        boardGrid.autosize();
        primaryStage.sizeToScene();
    }

    /**
     * @param gameObject the game object
     * @param location   the x and y coordinates of the game object
     */
    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        boardGrid.add(graphicObject, location.y, location.x);
    }

    //todo---------------------------------------------------------------------------------

    /**
     * function to set the input file from the url
     *
     * @param fileIn url of file
     * @return the buffered reader with the file input
     * @throws Exception to catch errors from file input stream
     */
    private BufferedReader getReaderFromUrl(String fileIn) throws Exception {
        File input = new File(System.getProperty("user.dir")
                + "/src/main/resources/scores/sorted/" + fileIn);

        InputStream in = new FileInputStream(input);
        InputStreamReader isr = new InputStreamReader(in);
        return new BufferedReader(isr);
    }

    /**
     * function to create table from the csv, using table view
     *
     * @param scoreFile the csv input as a string
     * @return the table view object
     * @throws Exception tpo catch error from the get reader from url
     */
    public TableView<ObservableList<String>> createTable(
            String scoreFile) throws Exception {
        TableView<ObservableList<String>> table = new TableView<>();
        populateTable(getReaderFromUrl(scoreFile), table);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFocusTraversable(false);
        return table;
    }

    /**
     * function to populate the table
     *
     * @param in the buffered reader that was created from the file input
     * @param table the table object created previously form the csv input
     * @throws Exception to catch possible errors from the input stream
     */
    private void populateTable(BufferedReader in,
                               final TableView<ObservableList<String>> table)
            throws Exception {
        table.getItems().clear();
        table.getColumns().clear();

        //Header line //adding a space for row index
        final String headerLine = "#," + in.readLine();
        final String[] headerValues = headerLine.split(",");

        for (int column = 0; column < headerValues.length; column++) {
            //Add column headers to table
            table.getColumns().add(createColumn(column, headerValues[column]));
        }

        //Data
        String dataLine;
        for(int i = 0; i < 10; i++) {
            if((dataLine = in.readLine()) != null) {
                // Add data to table including row index
                final String[] dataValues = ((i + 1) + "," + dataLine).split(",");
                ObservableList<String> data = FXCollections.observableArrayList();
                Collections.addAll(data, dataValues);
                table.getItems().add(data);
            }
            else {
                break;
            }
        }
    }

    /**
     * function to create the columns within the table object
     *
     * @param columnIndex the column index as an int
     * @param columnTitle the column title as a string
     * @return return the table column object generated
     */
    private TableColumn<ObservableList<String>, String> createColumn(
            final int columnIndex, String columnTitle) {
        TableColumn<ObservableList<String>, String> column = new TableColumn<>();
        String title;
        if (columnTitle == null || columnTitle.trim().length() == 0) {
            title = "Column " + (columnIndex + 1);
        } else {
            title = columnTitle;
        }
        column.setText(title);

        column.setCellValueFactory(cellData -> {
            ObservableList<String> values = cellData.getValue();

            if (columnIndex > values.size()) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(values.get(columnIndex));
            }
        });

        column.setSortable(false);
        column.setResizable(false);
        column.setEditable(false);
        column.setReorderable(false);

        if(columnIndex == 0) {
            column.setPrefWidth(40);
        }
        if(columnIndex == 1 || columnIndex == 2 || columnIndex == 4) {
            column.setPrefWidth(120);
        }
        return column;
    }

    /**
     * function to show the scoreboard from the fxml file
     *
     * @throws Exception to catch errors from the fxml loader
     */
    public void showScoreBoard() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("score.fxml"));

        GridPane scoreGrid = loader.load();

        ScoreController scoreController = loader.getController();
        scoreController.setStage(primaryStage);
        scoreController.setGameController(this);

        Scoreboard scoreboard = new Scoreboard(this, scoreController);
        scoreboard.callWriteRawCSV();

        TimeUnit.MILLISECONDS.sleep(50);

        String fileToTable = scoreboard.getSortedFile() + ".csv";
        TableView<ObservableList<String>> table = createTable(fileToTable);

        VBox vbox = new VBox();
        vbox.getChildren().add(table);
        vbox.setPrefHeight(306);

        vbox.setPadding(new Insets(20));
        scoreGrid.add(vbox,0,2);

        Scene scoreScene = new Scene(scoreGrid);
        primaryStage.setScene(scoreScene);
        primaryStage.show();
    }

    /**
     * function to call the show about dialog box
     */
    public void showAbout() {
        Notification dialog = new Notification(primaryStage);
        dialog.showAbout();
    }

    /**
     * function to call the show help dialog box
     */
    public void showHelp() {
        Notification dialog = new Notification(primaryStage);
        dialog.showHelp();
    }

    /**
     * function to call the close game dialog box
     */
    public void closeGame() {
        Notification dialog = new Notification(primaryStage);
        if(dialog.showClose(false)) {
            primaryStage.close();
        }
    }

    /**
     * Function to call the saveGame method, in SaveGame
     * passes primaryStage, current level and GameController as parameters
     */
    public void saveGame() {
        SaveGame saveGame = new SaveGame(this.primaryStage, this.currentLevel, this);
        saveGame.save();
    }

    /**
     * get the keycodes from movement and store in game controller
     * before the level is reset and they are wiped from movement
     * pass keyCodes back to movement after level is reset
     * into the feed input
     *
     * @throws IOException throws exception due to calling resetLevel
     */
    public void undo() throws IOException {
        List<KeyCode> keyCodes = movement.getKeyCodes();
        if (!keyCodes.isEmpty()) {
            undoMoveOn = true;
            resetLevel();
            movement.feedInput(keyCodes);
        }
        reloadGrid();
    }

    /**
     * sets the reset boolean to true, removes the event filter from the scene
     * calls initializeGame with a new save file as a stream as the parameter
     *
     * @throws IOException can be caused by file input stream
     */
    public void resetLevel() throws IOException {
        resetLevelOn = true;
        movement.removeEventFilter();
        initializeGame(new FileInputStream(saveFile));
    }

    /**
     * toggle the music on and off, by calling function in Audio class
     */
    public void toggleMusic() {
        Audio audio = Audio.getInstance();
        audio.toggleMusic();
    }

    /**
     * toggle debug mode when in game
     */
    public void toggleDebug() {
        debug = !debug;
        reloadGrid();
    }

    /**
     * @return true or false depending on if debug is toggled on or off
     */
    public static boolean isDebugActive() {
        return debug;
    }

    /**
     * Function sets level complete to false, and removes the event filters
     * and calls main.start which changes the scene to the start menu
     *
     * @throws IOException because the start method is called, from an instance of Main
     */
    public void goBackToStart() throws IOException {
        Notification dialog = new Notification(primaryStage);
        if(dialog.showClose(true)) {
            ScoreController.levelComplete = false;
            movement.removeEventFilter();
            Main main = new Main();
            main.start(primaryStage);
        }
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
