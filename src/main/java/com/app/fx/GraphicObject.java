package com.app.fx;

import com.app.controller.GameController;
import com.app.controller.SettingsController;
import com.app.engine.GameObject;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Styles the game objects on the game window
 *
 */
public class GraphicObject extends Rectangle {

    public static String pathWall;
    public static String pathCrate;
    public static String pathDiamond;
    public static String pathKeeper;
    public static String pathFloor;
    public static String pathCrateOnDiamond;

    public static Image wall;
    public static Image crate;
    public static Image diamond;
    public static Image keeper;
    public static Image floor;
    public static Image crateOnDiamond;

    /**
     * function to initialize all the sprites
     */
    public static void initSprite() {
        wall = new Image(Objects.requireNonNullElse(
                pathWall, "wallBlack.png"));
        crate = new Image(Objects.requireNonNullElse(
                pathCrate, "crateYellow.png"));
        diamond = new Image(Objects.requireNonNullElse(
                pathDiamond, "diamondBlue.png"));
        keeper = new Image(Objects.requireNonNullElse(
                pathKeeper, "stopDown.png"));
        floor = new Image(Objects.requireNonNullElse(
                pathFloor, "floorGreen.png"));
        crateOnDiamond = new Image(Objects.requireNonNullElse(
                pathCrateOnDiamond, "codYellow.png"));
    }

    /**
     * function to return the correct path of the keeper
     *
     * @return the path of the keeper
     */
    public static String getPathKeeper() {
        return pathKeeper;
    }

    /**
     * function to set the correct keeper path
     *
     * @param pathKeeper the path of the keeper as a string
     */
    public static void setPathKeeper(String pathKeeper) {
        GraphicObject.pathKeeper = pathKeeper;
    }

    /**
     * function to set the default keeper path
     */
    public static void setDefaultPathKeeper() {
        GraphicObject.pathKeeper = "stopDown.png";
        initSprite();
    }

    /**
     * the graphic object constructor
     *
     * @param obj the game object
     */
    public GraphicObject(GameObject obj) {
        Paint color;
        boolean changeToSprite = false;

        switch (obj) {
            case VOID:
                color = Color.LIGHTBLUE;
                this.setFill(color);
                break;

            case WALL:
                if(SettingsController.selectTypeWall == 0) {
                    color = Color.BLACK;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeWall == 1) {
                    color = SettingsController.selectWallColor;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeWall == 2) {
                    this.setFill(new ImagePattern(wall));
                }
                break;

            case CRATE:
                if(SettingsController.selectTypeCrate == 0) {
                    color = Color.ORANGE;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeCrate == 1) {
                    color = SettingsController.selectCrateColor;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeCrate == 2) {
                    this.setFill(new ImagePattern(crate));
                    changeToSprite = true;
                }

                break;

            case DIAMOND:
                if(SettingsController.selectTypeDiamond == 0) {
                    color = Color.DEEPSKYBLUE;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeDiamond == 1) {
                    color = SettingsController.selectDiamondColor;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeDiamond == 2) {
                    this.setFill(new ImagePattern(diamond));
                }

                // TODO: fix memory leak.
                if (GameController.isDebugActive()) {
                    FadeTransition ft =
                            new FadeTransition(Duration.millis(1000), this);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.2);
                    ft.setCycleCount(Timeline.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                }
                break;

            case KEEPER:
                /*if(SettingsController.selectTypeKeeper == 0) {
                    color = Color.RED;
                    this.setFill(color);
                    break;
                }
                else if(SettingsController.selectTypeKeeper == 1) {
                    color = SettingsController.selectKeeperColor;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeKeeper == 2) {
                    this.setFill(new ImagePattern(keeper));
                }*/
                this.setFill(new ImagePattern(keeper));
                break;

            case FLOOR:
                if(SettingsController.selectTypeFloor == 0) {
                    color = Color.WHITE;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeFloor == 1) {
                    color = SettingsController.selectFloorColor;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeFloor == 2) {
                    this.setFill(new ImagePattern(floor));
                }
                break;

            case CRATE_ON_DIAMOND:
                if(SettingsController.selectTypeCrateOnDiamond == 0) {
                    color = Color.DARKCYAN;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeCrateOnDiamond == 1) {
                    color = SettingsController.selectCrateOnDiamondColor;
                    this.setFill(color);
                }
                else if(SettingsController.selectTypeCrateOnDiamond == 2) {
                    this.setFill(new ImagePattern(crateOnDiamond));
                    changeToSprite = true;
                }
                break;

            default:
                String message =
                        "Error in Level constructor. Object not recognized.";
                GameController.logger.severe(message);
                throw new AssertionError(message);
        }

        //this.setFill(color);
        this.setHeight(30);
        this.setWidth(30);

        if (!changeToSprite && obj != GameObject.FLOOR
                && obj != GameObject.WALL && obj != GameObject.VOID) {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }

        if (GameController.isDebugActive()) {
            this.setStroke(Color.RED);
            this.setStrokeWidth(0.25);
        }
    }
}
