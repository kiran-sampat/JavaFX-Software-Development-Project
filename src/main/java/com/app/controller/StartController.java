package com.app.controller;

import com.app.utilities.Notification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The start model and controller
 *
 */
public class StartController {

    private Stage primaryStage;

    public static Scene gameScene;

    @FXML private GridPane startGrid;
    @FXML private GridPane buttonGrid;
    @FXML private MenuBar MENU;
    @FXML private Label title;
    @FXML private Button play;
    @FXML private Button load;
    @FXML private Button settings;
    @FXML private Button exit;

    private String username;

    Notification nameDialog;

    /**
     * function to get the name of the player
     *
     * @return player name as string
     */
    public String getName() {
        return username;
    }

    /**
     * function to set the name of the player
     *
     * @param name player name as string
     */
    public void setName(String name) {
        this.username = name;
    }

    /**
     * function change scene to game scene through start game
     *
     * @throws Exception catch any fxml loader exceptions
     */
    public void callStartGame() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("game.fxml"));
        Parent root = loader.load();
        gameScene = new Scene(root);

        GameController gameController = loader.getController();
        nameDialog = new Notification(primaryStage, this);

        if(nameDialog.showInputTextDialog("start")) {
            gameController.setName(getName()); //TODO
            gameController.setStage(primaryStage);
            gameController.loadDefaultSaveFile();

            primaryStage.setTitle(GameController.GAME_NAME);
            primaryStage.setScene(gameScene);
            primaryStage.show();
        }
    }

    /**
     * function to change scene to the game scene through load game
     *
     * @throws Exception catch fxml loader exceptions
     */
    public void callLoadGame() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("game.fxml"));
        Parent root = loader.load();
        gameScene = new Scene(root);

        GameController gameController = loader.getController();
        nameDialog = new Notification(primaryStage, this);

        if(nameDialog.showInputTextDialog("load")) {
            gameController.setName(getName()); //TODO
            gameController.setStage(primaryStage);

            if(gameController.loadGameFile()) {
                primaryStage.setScene(gameScene);
                primaryStage.setTitle(GameController.GAME_NAME);
                primaryStage.show();
            }
        }
    }

    /**
     * function to change the scene to the settings scene
     *
     * @throws Exception catch fxml loader exceptions
     */
    public void callSettings() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("settings.fxml"));
        Parent root = loader.load();
        Scene settingsScene = new Scene(root);

        SettingsController settingsController = loader.getController();
        settingsController.setStage(primaryStage);

        primaryStage.setScene(settingsScene);
        primaryStage.setTitle(GameController.GAME_NAME);
        primaryStage.show();
    }

    /**
     * function to call the close game dialog
     */
    public void closeGame() {
        Notification dialog = new Notification(primaryStage);
        if(dialog.showClose(false)) {
            primaryStage.close();
        }
    }

    /**
     * function to set the stage to the primary stage
     *
     * @param primaryStage the primary stage
     */
    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
