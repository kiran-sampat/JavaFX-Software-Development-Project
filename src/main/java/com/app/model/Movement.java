package com.app.model;

import com.app.controller.GameController;
import com.app.controller.ScoreController;
import com.app.controller.StartController;
import com.app.engine.GameObject;
import com.app.fx.GraphicObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Related to movement of the player within the game scene
 *
 */
public class Movement {

    private final GameController gameController;
    public List<KeyCode> keyCodeStack = new ArrayList<>();
    private Level currentLevel;
    private EventHandler<KeyEvent> handler;
    private Timeline timeline;
    private int counter;
    private int down = 0;
    private int up = 0;

    /**
     * movement constructor
     *
     * @param gameController the instance of game controller
     * @param currentLevel the instance of level
     */
    public Movement(GameController gameController, Level currentLevel) {
        this.gameController = gameController;
        this.currentLevel = currentLevel;
    }

    /**
     * return the entire stack
     *
     * @return the stack
     */
    public List<KeyCode> getKeyCodes() {
        return keyCodeStack;
    }

    /**
     * push a new element onto the stack
     *
     * @param keyCode the key code to push
     */
    public void push(KeyCode keyCode) {
        keyCodeStack.add(keyCode);
    }

    /**
     * peeks the first element of stack
     *
     * @return the first element
     */
    public KeyCode peek() {
        if (keyCodeStack.isEmpty()) {
            return null;
        }

        return keyCodeStack.get(keyCodeStack.size() - 1);
    }

    /**
     * removes first element of stack
     *
     * @return the removed first element
     */
    public KeyCode pop() {
        if (keyCodeStack.isEmpty()) {
            return null;
        }

        KeyCode top = keyCodeStack.get(keyCodeStack.size() - 1);
        keyCodeStack.remove(keyCodeStack.size() - 1);
        return top;
    }

    /**
     * function to list or clear the clear the stack
     */
    public void listContents() {
        /*System.out.println("------------STACK CONTENT------------");
        for (KeyCode keyCode : keyCodeStack) {
            System.out.println(keyCode.toString());
        }
        System.out.println("Peek top element: " + peek());
        System.out.println("Size of stack: " + keyCodeStack.size());
        System.out.println("-------------------------------------");*/

        if (ScoreController.isLevelComplete() || gameController.isGameComplete()) {
            keyCodeStack.clear();
        }
    }

    /**
     * function to automate key presses by feeding the input into the handle key
     *
     * @param keyCodes the key passed in to simulate
     */
    public void feedInput(List<KeyCode> keyCodes) {
        this.keyCodeStack = keyCodes;
        /*System.out.println("-------------FEED INPUT--------------");
        System.out.println("Pop top element: " + pop());*/
        pop();
        for (KeyCode keyCode : keyCodeStack) {
            /*System.out.println(keyCode);*/
            handleKey(keyCode, false);
        }
        /*System.out.println("Size of stack: " + keyCodeStack.size());
        System.out.println("-------------------------------------");*/
    }

    /**
     * function to initialize and start the sprite timer
     */
    public void spriteTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if (counter == 1) {
                GraphicObject.setPathKeeper("stopUp.png");
                up = 0;
            } else if (counter == 2) {
                GraphicObject.setPathKeeper("stopRight.png");
            } else if (counter == 3) {
                GraphicObject.setPathKeeper("stopDown.png");
                down = 0;
            } else if (counter == 4) {
                GraphicObject.setPathKeeper("stopLeft.png");
            }
            GraphicObject.initSprite();
            gameController.reloadGrid();
        }));
        timeline.stop();
    }

    /**
     * function to stop the sprite timer
     */
    public void spriteTimerStop() {
        timeline.stop();
    }

    /**
     * @param code set of key codes for the key events
     * @param human boolean to check if input is automated or human
     */
    public void handleKey(KeyCode code, Boolean human) {
        switch (code) { //Added WASD compatibility as well as arrow keys to move
            case UP:
            case W:
                counter = 1;
                if (up == 3 && !GraphicObject.getPathKeeper().equals("stopUp.png")) {
                    GraphicObject.setPathKeeper("stopUp.png");
                    up = 0;
                } else if (up == 0) {
                    GraphicObject.setPathKeeper("moveUpRight.png");
                    up = 1;
                } else if (up == 1
                        && !GraphicObject.getPathKeeper().equals("stopUp.png")) {
                    GraphicObject.setPathKeeper("stopUp.png");
                    up = 2;
                } else if (up == 2) {
                    GraphicObject.setPathKeeper("moveUpLeft.png");
                    up = 3;
                }

                if (human) {
                    timeline.stop();
                    timeline.play();
                }

                if (move(new Point(-1, 0)) && human) {
                    push(code);
                }
                break;

            case RIGHT:
            case D:
                counter = 2;
                if (!GraphicObject.getPathKeeper().equals("stopRight.png")) {
                    GraphicObject.setPathKeeper("stopRight.png");
                } else {
                    GraphicObject.setPathKeeper("moveRight.png");
                }

                if (human) {
                    timeline.stop();
                    timeline.play();
                }

                if (move(new Point(0, 1)) && human) {
                    push(code);
                }
                break;

            case DOWN:
            case S:
                counter = 3;
                if (down == 3 && !GraphicObject.getPathKeeper().equals("stopDown.png")) {
                    GraphicObject.setPathKeeper("stopDown.png");
                    down = 0;
                } else if (down == 0) {
                    GraphicObject.setPathKeeper("moveDownRight.png");
                    down = 1;
                } else if (down == 1
                        && !GraphicObject.getPathKeeper().equals("stopDown.png")) {
                    GraphicObject.setPathKeeper("stopDown.png");
                    down = 2;
                } else if (down == 2) {
                    GraphicObject.setPathKeeper("moveDownLeft.png");
                    down = 3;
                }

                if (human) {
                    timeline.stop();
                    timeline.play();
                }

                if (move(new Point(1, 0)) && human) {
                    push(code);
                }
                break;

            case LEFT:
            case A:
                counter = 4;
                if (!GraphicObject.getPathKeeper().equals("stopLeft.png")) {
                    GraphicObject.setPathKeeper("stopLeft.png");
                } else {
                    GraphicObject.setPathKeeper("moveLeft.png");
                }

                if (human) {
                    timeline.stop();
                    timeline.play();
                }

                if (move(new Point(0, -1)) && human) {
                    push(code);
                }
                break;

            default:
                // TODO: implement something funny.
        }

        if (GameController.isDebugActive()) {
            System.out.println(code);
        }

        GraphicObject.initSprite();
    }

    /**
     *
     * @param delta the change in coordinates
     * @return boolean if move is success
     */
    public boolean move(Point delta) {
        if (gameController.isGameComplete()) {      // TODO
            return false;                           // These two
        }                                           // if statements
        if (ScoreController.isLevelComplete()) {    // can never be true
            return false;                           // move is never called
        }                                           // when these are true

        Point keeperPosition = currentLevel.getKeeperPosition();
        GameObject keeper = currentLevel.getObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = currentLevel.getObjectAt(targetObjectPoint);

        if (GameController.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf(
                    "Target object: %s at [%s] ", keeperTarget, targetObjectPoint);
        }

        boolean keeperMoved = false;

        switch (keeperTarget) {

            case WALL:
                break;

            case CRATE:
                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                currentLevel.moveGameObjectBy(keeperTarget, targetObjectPoint, delta);
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            case FLOOR:
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                GameController.logger.severe("The object to be moved was not " +
                        "a recognised GameObject.");
                throw new AssertionError("This should not have " +
                        "happened. Report this problem to the developer.");
        }

        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            gameController.setMoveComplete(true);
            gameController.setMoveCount((gameController.getMoveCount()) + 1);

            if (currentLevel.isComplete()) {
                if (GameController.isDebugActive()) {
                    System.out.println("\nLevel complete!");
                }
                ScoreController.levelComplete = true;
                currentLevel = gameController.getNextLevel();
            }
            return true;
        }
        return false;
    }

    /**
     * Function that binds keys to only the game scene
     */
    public void setEventFilter() {
        handler = event -> {
            handleKey(event.getCode(), true);
            gameController.reloadGrid();
        };
        StartController.gameScene.addEventFilter(KeyEvent.KEY_PRESSED, handler);
    }

    /**
     * Function to unbind keys from teh game scene
     */
    public void removeEventFilter() {
        StartController.gameScene.removeEventFilter(KeyEvent.KEY_PRESSED, handler);
    }
}
