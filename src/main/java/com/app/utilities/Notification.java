package com.app.utilities;

import com.app.controller.StartController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Manages notifications for in game
 *
 */
public class Notification {

    private final Stage primaryStage;
    private StartController startController;

    /**
     * constructor for notification class
     *
     * @param primaryStage the primary stage
     */
    public Notification(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * constructor for notification class
     *
     * @param primaryStage the primary stage
     * @param startController instance of star controller
     */
    public Notification(Stage primaryStage, StartController startController) {
        this.primaryStage = primaryStage;
        this.startController = startController;
    }

    /**
     * function to return image view created with parameters
     *
     * @param path path to the image
     * @param size the size of the image thumbnail
     * @param ratio true or false, to keep ratio of image fixed
     * @return the image view object
     */
    public ImageView thumbnail(String path, int size, boolean ratio) {
        return new ImageView(new Image(path,
                size, 50, ratio,
                false, false));
    }

    /**
     * shows the close dialog box, when invoked
     *
     * @param back boolean indicates if function is invoked through back button or not
     * @return true or false, if ok button is pressed
     */
    public boolean showClose(boolean back) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setResizable(false);
        if(back) {
            alert.setTitle("Go Back");
            alert.setHeaderText("Go back to Main Menu?");
            alert.setContentText("Are you sure you want to go back? " +
                    "Progress for this level will be lost if you don't save!");
        } else {
            alert.setTitle("Exit Game");
            alert.setHeaderText("Exit Sokoban");
            alert.setContentText("Are you sure you want to quit? " +
                    "Remember to save your game if you haven't finished the level!");
        }
        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    /**
     * shows the about dialog box, when invoked
     */
    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About This Game");
        alert.setHeaderText("Sokoban");
        alert.initOwner(primaryStage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setResizable(false);
        alert.getDialogPane().setPrefSize(300, 140);

        GridPane aboutPane = new GridPane();
        aboutPane.add(new Label("Best Sokoban Ever Version 6."), 0, 0);
        aboutPane.add(new Label("Enjoy the Game!"), 0, 1);

        alert.getDialogPane().setContent(aboutPane);
        alert.showAndWait();
    }

    /**
     * shows the help dialog box, when invoked
     */
    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Help");
        alert.initOwner(primaryStage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setResizable(false);
        alert.setGraphic(thumbnail("crateYellow.png", 55, true));
        //alert.getDialogPane().setPadding(new Insets(20));
        Label text = new Label(
                "The game is played on a board, where each square on the " +
                        "board is either a floor, wall or void space. " +
                        "Floor squares can contain crates, and some can " +
                        "contain diamonds.\n\n" +
                    "The keeper, can only move horizontally or vertically " +
                        "onto empty floor or diamond squares and never " +
                        "directly through walls.\n" +
                    "The keeper can also push crates into empty floor, " +
                        "or diamond squares, by moving into them. However, " +
                        "crates cannot be pushed into other crates or walls, " +
                        "and cannot be pulled either.\n\n" +
                    "The level/game is complete when all diamond spaces " +
                        "have been filled with an equal number of crates.\n\n");

        text.setWrapText(true);
        text.setPrefWidth(350D);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        text.setPadding(new Insets(20));

        GridPane pane = new GridPane();

        ImageView keeper = thumbnail("stopDown.png", 40, false);
        pane.add(keeper, 0, 0);
        GridPane.setHalignment(keeper, HPos.CENTER);
        pane.add(new Label("Warehouse Keeper"), 1, 0);

        ImageView diamond = thumbnail("diamondPurple.png", 30, true);
        pane.add(diamond, 0, 1);
        GridPane.setHalignment(diamond, HPos.CENTER);
        pane.add(new Label("Diamond (storage location)"), 1, 1);

        ImageView crate = thumbnail("crateRed.png", 45, true);
        pane.add(crate, 0, 2);
        GridPane.setHalignment(crate, HPos.CENTER);
        pane.add(new Label("Crate"), 1, 2);

        pane.setHgap(30);
        pane.setVgap(10);
        //pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.TOP_CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(text, new Separator(), pane, new Separator());

        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
    }

    /**
     * function to show the text input field for player name
     *
     * @param type the type of invocation, either through start or load
     * @return boolean, whether name is entered correctly
     */
    public boolean showInputTextDialog(String type) {
        // Create the and setup the text input dialog window
        TextInputDialog tid = new TextInputDialog();
        tid.initOwner(primaryStage);
        tid.initModality(Modality.APPLICATION_MODAL);
        tid.getDialogPane().setPrefSize(380, 180);
        tid.setResizable(false);
        tid.setTitle("Enter Sokoban Username");
        tid.setHeaderText(
                "Please enter a Sokoban Username." +
                        "\nUsernames can only contain " +
                        "\nalphanumeric characters and dashes!"
        );
        tid.setContentText("Username: ");

        // Lookup the existing text field, and set the prompt text
        TextField username = tid.getEditor();
        username.setPromptText("Enter username");
        username.requestFocus();

        // Lookup the existing ok button
        // Enable/Disable ok button depending on whether input is valid
        Button okayButton = (Button) tid.getDialogPane().lookupButton(ButtonType.OK);
        okayButton.setDisable(true);

        String thumbPath;
        if (type.equals("load")) {
            thumbPath = "crateYellow.png";
            okayButton.setText("Load");
        } else {
            thumbPath = "crateRed.png";
            okayButton.setText("Play");
        }

        // Set the graphic based on if dialog is called from start or load
        tid.setGraphic(thumbnail(thumbPath, 55, true));

        // Do validation on the input, empty and containing special characters
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            okayButton.setDisable(newValue.trim().isEmpty()
                    || newValue.matches("^.*[^a-zA-Z0-9].*$"));
        });

        // Convert the result to a string when the ok button is pressed
        tid.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return username.getText();
            }
            return null;
        });

        Optional<String> result = tid.showAndWait();

        if (result.isPresent()) {
            String name = result.get();
            startController.setName(name);
            return true;
        }

        return false;
    }

    /**
     * show victory message, is now deprecated
     */
    @Deprecated
    public void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed the game in 0 number of moves!";
        MotionBlur mb = new MotionBlur(1, 1);

        newDialog(dialogTitle, dialogMessage, mb);
    }

    /**
     * @param dialogTitle         the title of the dialog
     * @param dialogMessage       the text of the message in the dialog
     * @param dialogMessageEffect the effects applied to the dialog text
     */
    @Deprecated
    public void newDialog(String dialogTitle,
                          String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(14));

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
