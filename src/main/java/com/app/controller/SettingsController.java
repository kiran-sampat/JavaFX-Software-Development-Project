package com.app.controller;

import com.app.fx.GraphicObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The settings model and controller
 *
 */
public class SettingsController {

    private Stage primaryStage;

    @FXML private GridPane settingsGrid;
    @FXML private GridPane titleGrid;
    @FXML private GridPane buttonGrid;
    @FXML private MenuBar MENU;

    @FXML private GridPane colorGrid;
    @FXML private GridPane spritesGrid;
    @FXML private Button color;
    @FXML private Button sprites;
    @FXML private Button music;
    @FXML private Button back;
    @FXML private Label title;

    public static int selectTypeWall = 0;
    public static int selectTypeCrate = 0;
    public static int selectTypeDiamond = 0;
    public static int selectTypeKeeper = 0;
    public static int selectTypeFloor = 0;
    public static int selectTypeCrateOnDiamond = 0;

    public static Color selectWallColor = Color.BLACK;
    public static Color selectCrateColor = Color.ORANGE;
    public static Color selectDiamondColor = Color.DEEPSKYBLUE;
    public static Color selectKeeperColor = Color.RED;
    public static Color selectFloorColor = Color.WHITE;
    public static Color selectCrateOnDiamondColor = Color.DARKCYAN;

    public static Text wallStatus = new Text(" - Nothing Selected");
    public static Text crateStatus = new Text(" - Nothing Selected");
    public static Text diamondStatus = new Text(" - Nothing Selected");
    public static Text floorStatus = new Text(" - Nothing Selected");

    /**
     * sets values for the color picker, and changes global counters
     *
     * @param c the color picker to edit
     * @param type the type of game object to change
     */
    public void setValues(ColorPicker c, int type) {
        switch (type) {
            case 1:
                selectWallColor = c.getValue();
                selectTypeWall = 1;
                break;
            case 2:
                selectCrateColor = c.getValue();
                selectTypeCrate = 1;
                break;
            case 3:
                selectDiamondColor = c.getValue();
                selectTypeDiamond = 1;
                break;
            case 4:
                selectKeeperColor = c.getValue();
                selectTypeKeeper = 1;
                break;
            case 5:
                selectFloorColor = c.getValue();
                selectTypeFloor = 1;
                break;
            case 6:
                selectCrateOnDiamondColor = c.getValue();
                selectTypeCrateOnDiamond = 1;
                break;
        }
    }

    /**
     * function to dynamically create pickers
     *
     * @return the vbox with color pickers populated
     */
    public VBox createPickers() {
        VBox vbox = new VBox();

        int[] types = {1, 2, 3, 4, 5, 6};
        Color[] colors = {selectWallColor, selectCrateColor, selectDiamondColor,
                selectKeeperColor, selectFloorColor, selectCrateOnDiamondColor};
        String[] texts = {
                "Wall", "Crate", "Diamond", "Keeper", "Floor", "Crate on Diamond"};

        for (int i = 0; i < types.length; i++) {
            int type = i;

            Text text = new Text(texts[i]);
            ColorPicker cp = new ColorPicker(colors[i]);

            cp.setOnAction(e -> setValues(cp, types[type]));

            vbox.getChildren().add(text);
            vbox.getChildren().add(cp);
        }

        vbox.setAlignment(Pos.TOP_CENTER);
        return vbox;
    }

    /**
     * function to change scene to the color settings fxml scene
     *
     * @throws IOException throws exception
     */
    public void callColor() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("color.fxml"));
        colorGrid = loader.load();

        SettingsController settingsController = loader.getController();
        settingsController.setStage(primaryStage);

        VBox vbox = createPickers();
        colorGrid.add(vbox,0,2);

        Scene colorScene = new Scene(colorGrid);
        primaryStage.setScene(colorScene);
        primaryStage.show();
    }

    /**
     * function to create a button form the given parameters
     *
     * @param path path of the image to use as thumbnail as a string
     * @return the button created
     */
    public Button returnButtonThumb(String path) {
        Button button = new Button();
        ImageView img = new ImageView( new Image(path));
        button.setGraphic(img);
        button.getStyleClass().add("spriteButtons");
        return button;
    }

    /**
     * function to set the path of the global counter based on which button was pressed
     *
     * @param path path to file as string
     * @param type the type of game object to change as an int
     */
    public void setPath(String path, int type) {
        switch (type) {
            case 1:
                GraphicObject.pathWall = path;
                selectTypeWall = 2;
                break;
            case 2:
                GraphicObject.pathCrate = path;
                path = path.replace("crate", "cod");
                GraphicObject.pathCrateOnDiamond = path;
                selectTypeCrate = 2;
                selectTypeCrateOnDiamond = 2;
                break;
            case 3:
                GraphicObject.pathDiamond = path;
                selectTypeDiamond = 2;
                break;
            case 4:
                GraphicObject.pathFloor = path;
                selectTypeFloor = 2;
                break;
        }
    }

    /**
     * function to dynamically create buttons
     *
     * @param names the array of strings of the names of sprite types
     * @param status the status of which button was selected last
     * @param type the type of game object to change
     * @return the hbox with buttons already populated
     */
    public HBox createButtons(String[] names, Text status, int type) {
        HBox hbox = new HBox();
        Button button;
        for (String name : names) {
            String path = name + ".png";
            button = returnButtonThumb(path);
            hbox.getChildren().add(button);

            button.setOnAction(e -> {
                status.setText(" - " + name + " Selected");
                setPath(path, type);
                GraphicObject.initSprite();
            });
        }
        hbox.setAlignment(Pos.TOP_CENTER);
        hbox.setSpacing(4);
        return hbox;
    }

    /**
     * function to change the scene to the sprite selection scene from the fxml document
     *
     * @throws IOException throws exception
     */
    public void callSprites() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("sprites.fxml"));
        spritesGrid = loader.load();

        SettingsController settingsController = loader.getController();
        settingsController.setStage(primaryStage);

        Text selectWallTitle = new Text("Select Wall");
        Text selectCrateTitle = new Text("Select Crate");
        Text selectDiamondTitle = new Text("Select Diamond");
        Text selectFloorTitle = new Text("Select Floor");

        String[] namesWall = {
                "wallBlack", "wallBrown", "wallGray", "wallSand"
        };
        String[] namesCrateA = {
                "crateBlack", "crateBrown", "crateGray", "crateSand"
        };
        String[] namesCrateB = {
                "crateBlue", "cratePurple", "crateRed", "crateYellow"
        };
        String[] namesDiamondA = {
                "diamondBlack", "diamondBrown", "diamondGray", "diamondSand"
        };
        String[] namesDiamondB = {
                "diamondBlue", "diamondPurple", "diamondRed", "diamondYellow"
        };
        String[] namesFloor = {
                "floorGreen", "floorBrown", "floorGray", "floorSand"
        };

        HBox wallDisplay = new HBox(selectWallTitle, wallStatus);
        HBox crateDisplay = new HBox(selectCrateTitle, crateStatus);
        HBox diamondDisplay = new HBox(selectDiamondTitle, diamondStatus);
        HBox floorDisplay = new HBox(selectFloorTitle, floorStatus);

        HBox[] styleDisplays = {wallDisplay, crateDisplay,
                diamondDisplay, floorDisplay};

        for(HBox hBoxButton : styleDisplays) {
            hBoxButton.setAlignment(Pos.TOP_CENTER);
            hBoxButton.setStyle("-fx-padding: 0 7 0 7;");
        }

        VBox vbox = new VBox(
                wallDisplay, createButtons(namesWall, wallStatus, 1),
                crateDisplay, createButtons(namesCrateA, crateStatus, 2),
                createButtons(namesCrateB, crateStatus, 2),
                diamondDisplay, createButtons(namesDiamondA, diamondStatus, 3),
                createButtons(namesDiamondB, diamondStatus, 3),
                floorDisplay, createButtons(namesFloor, floorStatus, 4)
        );

        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getStylesheets().add("style.css");

        spritesGrid.add(vbox,0,2);

        Scene spritesScene = new Scene(spritesGrid);
        primaryStage.setScene(spritesScene);
        primaryStage.show();
    }

    /**
     * function to change the scene to the settings scene from the fxml document
     *
     * @throws Exception throws exception
     */
    public void callCallSettings() throws Exception {
        StartController start = new StartController();

        start.setStage(primaryStage);
        start.callSettings();
    }

    /**
     * calls toggle music
     */
    public void callToggleMusic() {
        GameController music = new GameController();
        music.toggleMusic();
    }

    /**
     * calls go back to start scene from the fxml document
     *
     * @throws IOException throws exception
     */
    public void callGoBackToStart() throws IOException {
        Main main = new Main();
        main.start(primaryStage);
    }

    /**
     * sets the stage to the primary stage
     *
     * @param primaryStage the primary stage
     */
    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
