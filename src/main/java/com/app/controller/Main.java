package com.app.controller;

import com.app.utilities.Audio;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main class to launch application
 *
 */
public class Main extends Application {

    /**
     * @param args command line argument string list
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    /**
     * @param primaryStage the primary stage is the window for the game
     * @throws IOException if fxml document cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Audio audio = Audio.getInstance();
        audio.createPlayer();

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("start.fxml"));
        Parent root = loader.load();
        Scene startScene = new Scene(root);

        StartController startController = loader.getController();
        startController.setStage(primaryStage);

        primaryStage.setOnCloseRequest(event -> {
            startController.closeGame();
            event.consume();
        });
        primaryStage.getIcons().add(new Image("crateBlue.png"));
        primaryStage.setTitle(GameController.GAME_NAME);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
}
