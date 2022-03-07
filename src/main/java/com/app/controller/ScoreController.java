package com.app.controller;

import com.app.fx.GraphicObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The score model and controller
 *
 */
public class ScoreController {

    private Stage primaryStage;
    private GameController gameController;

    @FXML private GridPane scoreGrid;
    @FXML private GridPane titleGrid;
    @FXML private GridPane buttonGrid;
    @FXML private MenuBar MENU;
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label level;
    @FXML private Label moves;
    @FXML private Label time;
    @FXML private Button mainMenu;
    @FXML private Button nextLevel;

    public static boolean levelComplete = false;

    /**
     * function to call when the next level button is pressed
     */
    public void nextLevel() {
        levelComplete = false;
        gameController.setCurrentLevel(gameController.getNextLevel());
        gameController.resetMoveCounter();
        GraphicObject.setDefaultPathKeeper();
        gameController.getTimer().play();
        gameController.reloadGrid();

        primaryStage.setScene(StartController.gameScene);
    }

    public static boolean isLevelComplete() { //TODO
        return levelComplete;
    }

    /**
     * setter method for the game controller
     *
     * @param gameController instance of game controller
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * function to call close game
     */
    public void callCloseGame() {
        gameController.closeGame();
    }

    /**
     * function to call function to change scene to start menu
     *
     * @throws IOException throws exception
     */
    public void callGoBackToStart() throws IOException {
        gameController.goBackToStart();
    }

    /**
     * function to set the stage
     *
     * @param primaryStage the primary stage
     */
    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
