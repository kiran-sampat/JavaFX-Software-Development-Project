package com.app.model;

import com.app.controller.GameController;
import com.app.engine.GameObject;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static com.app.model.GameGrid.translatePoint;

/**
 * Manages the level properties and type
 *
 */
public final class Level implements Iterable<GameObject> {

    private final String name;
    private final String mapSetName;
    private final GameGrid objectsGrid;
    private final GameGrid diamondsGrid;
    private final int index;
    private int numberOfDiamonds = 0;
    private Point keeperPosition = new Point(0, 0);
    int rows, columns;

    /**
     *
     * @param mapSetName the name of the map set
     * @param levelName  the name of the level
     * @param levelIndex the index of the level
     * @param raw_level  the levels layout as a list
     */
    public Level(String mapSetName,
                 String levelName, int levelIndex, List<String> raw_level) {
        if (GameController.isDebugActive()) {
            System.out.printf(
                    "[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        this.mapSetName = mapSetName;
        this.name = levelName;
        this.index = levelIndex;

        this.rows = raw_level.size();
        this.columns = raw_level.get(0).trim().length();

        this.objectsGrid = new GameGrid(rows, columns);
        this.diamondsGrid = new GameGrid(rows, columns);

        for (int row = 0; row < raw_level.size(); row++) {

            // Loop over the string one char at a time because it should be the fastest way:
            // http://stackoverflow.com/questions/8894258/fastest-way-to-iterate-over-all-the-chars-in-a-string
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                // The game object is null when the we're adding a floor or a diamond
                GameObject curTile =
                        GameObject.fromChar(raw_level.get(row).charAt(col));

                if (curTile == GameObject.DIAMOND) {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.FLOOR;
                } else if (curTile == GameObject.KEEPER) {
                    keeperPosition = new Point(row, col);
                } else if (curTile == GameObject.CRATE_ON_DIAMOND) {
                    numberOfDiamonds++;
                    curTile = GameObject.DIAMOND;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.CRATE;
                }

                objectsGrid.putGameObjectAt(curTile, row, col);
                curTile = null;
            }
        }

        for(int row = 0; row < raw_level.size(); row++) {
            for(int col = 0; col < raw_level.get(row).length(); col++) {
                if(voidChecker(row,col)) {
                    objectsGrid.putGameObjectAt(GameObject.VOID, row, col);
                }
            }
        }
    }

    /**
     * checks if object is a void space
     *
     * @param row row number as int
     * @param col column number as int
     * @return boolean flags the void space as true
     */
    public boolean voidChecker(int row, int col) {
        boolean isVoid = true;

        for(int a = row-1; a < row+2; a++) {
            for(int b = col-1; b < col+2; b++) {
                if(isValid(a,b) &&
                        (objectsGrid.getGameObjectAt(a, b) != GameObject.WALL
                        && objectsGrid.getGameObjectAt(a, b) != GameObject.VOID)) {
                    isVoid = false;
                }
            }
        }
        return isVoid;
    }

    /**
     * checks if coordinate is valid
     *
     * @param a int value x coordinate
     * @param b int value y coordinate
     * @return boolean if coordinate is valie
     */
    boolean isValid(int a, int b) {
        if(a >= 0 && a < objectsGrid.ROWS && b >= 0 && b < objectsGrid.COLUMNS) {
            return true;
        }
        return false;
    }

    /**
     * @return checks whether number of diamonds that have been crated,
     * is greater than or equal to the number of diamonds
     */
    boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if (objectsGrid.getGameObjectAt(col, row) == GameObject.CRATE
                        && diamondsGrid.getGameObjectAt(col, row)
                        == GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }

        return cratedDiamondsCount >= numberOfDiamonds;
    }

    /**
     * @return returns the map sets name
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * @return returns the levels name
     */
    public String getName() {
        return name;
    }

    /**
     * @return returns the level index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return returns the position of the keeper
     */
    Point getKeeperPosition() {
        return keeperPosition;
    }

    /**
     * @param source of the spawn point
     * @param delta  the distance away from the source point
     * @return the objects source and distance points on the grid
     */
    GameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * @param p the object location
     * @return the current location of the game object
     */
    GameObject getObjectAt(Point p) {
        return objectsGrid.getGameObjectAt(p);
    }

    /**
     * @param object the game object
     * @param source the source point on the grid
     * @param delta  the distance away from a point
     */
    void moveGameObjectBy(GameObject object, Point source, Point delta) {
        moveGameObjectTo(object, source, translatePoint(source, delta));
    }

    /**
     * @param object      the game object
     * @param source      the source point on the grid
     * @param destination the destination point
     */
    public void moveGameObjectTo(
            GameObject object, Point source, Point destination) {
        objectsGrid.putGameObjectAt(getObjectAt(destination), source);
        objectsGrid.putGameObjectAt(object, destination);
    }

    /**
     * returns the diamonds grid
     *
     * @return the diamonds grid
     */
    public GameGrid getDiamondsGrid() {
        return diamondsGrid;
    }

    /**
     * returns the objects grid
     *
     * @return the objects grid
     */
    public GameGrid getObjectsGrid() {
        return objectsGrid;
    }

    /**
     * @return convert the objects grid into a string
     */
    @Override
    public String toString() {
        return objectsGrid.toString();
    }

    /**
     * @return an iterator for the game object
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }


    public class LevelIterator implements Iterator<GameObject> {

        int column = 0;
        int row = 0;

        /**
         * @return return true if row is not equal to grid rows minus one and column is not equal to grid columns
         */
        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.ROWS - 1 && column == objectsGrid.COLUMNS);
        }

        /**
         * @return the game object
         */
        @Override
        public GameObject next() {
            if (column >= objectsGrid.COLUMNS) {
                column = 0;
                row++;
            }

            GameObject object = objectsGrid.getGameObjectAt(column, row);
            GameObject diamond = diamondsGrid.getGameObjectAt(column, row);
            GameObject retObj = object;

            column++;

            if (diamond == GameObject.DIAMOND) {
                if (object == GameObject.CRATE) {
                    retObj = GameObject.CRATE_ON_DIAMOND;
                } else if (object == GameObject.FLOOR) {
                    retObj = diamond;
                } else {
                    retObj = object;
                }
            }

            return retObj;
        }

        /**
         * @return the current coordinates on the grid as (col, row)
         */
        public Point getCurrentPosition() {
            return new Point(column, row);
        }
    }
}