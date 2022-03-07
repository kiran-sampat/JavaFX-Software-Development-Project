package com.app.model;

import com.app.controller.GameController;
import com.app.engine.GameObject;

import java.awt.*;
import java.util.Iterator;

/**
 * Manages the objects and diamonds grid of the game
 *
 */
public class GameGrid implements Iterable {

    final int COLUMNS;
    final int ROWS;
    private final GameObject[][] gameObjects;

    /**
     * @param columns number of columns in the game grid
     * @param rows    number of rows in the game grid
     */
    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;

        gameObjects = new GameObject[COLUMNS][ROWS];
    }

    /**
     * @param sourceLocation the location for the source point in the game grid
     * @param delta          is the distance away from the source point
     * @return the coordinates of the new location on the grid
     */
    static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     * @return is the dimensions of the grid in (col, row)
     */
    public Dimension getDimension() {
        return new Dimension(COLUMNS, ROWS);
    }

    /**
     * @param source gets the source point
     * @param delta  gets the delta point
     * @return the translated point
     */
    GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * @param col gets the column location
     * @param row gets the row location
     * @return returns the object coordinate
     * @throws ArrayIndexOutOfBoundsException if coordinates are outside the bounds of grid
     */
    public GameObject getGameObjectAt(int col, int row)
            throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            if (GameController.isDebugActive()) {
                System.out.printf("Trying to get null GameObject " +
                        "from COL: %d  ROW: %d", col, row);
            }
            throw new ArrayIndexOutOfBoundsException(
                    "The point [" + col + ":" + row + "] is outside the map.");
        }

        return gameObjects[col][row];
    }

    /**
     * @param p gets location of game object
     * @return coordinates of the game object
     */
    public GameObject getGameObjectAt(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    /**
     * @param position on the grid
     * @return returns the game object and its position
     */
    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }

    /**
     * @param gameObject gets the game object
     * @param x          gets the x coordinate
     * @param y          gets the y coordinate
     * @return returns true if the coordinates of the game object are set correctly
     */
    public boolean putGameObjectAt(GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        gameObjects[x][y] = gameObject;
        return gameObjects[x][y] == gameObject;
    }

    /**
     * @param gameObject gets game object
     * @param p          gets location of object
     * @return if the point is not null, the object and its coordinates as ints are returned
     */
    public boolean putGameObjectAt(GameObject gameObject, Point p) {
        return p != null && putGameObjectAt(
                gameObject, (int) p.getX(), (int) p.getY());
    }

    /**
     * @param x x coordinate of the object
     * @param y y coordinate of the object
     * @return x if less than 0 or greater than or equal to the number of columns or
     * y if less than 0 or greater than or equal to the number of rows
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     * @param p gets the location of object
     * @return returns true or false, based on whether the object x and y coordinates are out of bounds
     */
    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }

    /**
     * @return changes the integer coordinates of the game objects into a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gameObjects.length);

        for (GameObject[] gameObject : gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = GameObject.DEBUG_OBJECT;
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * @return iterator for the game grid
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }

    public class GridIterator implements Iterator<GameObject> {
        int row = 0;
        int column = 0;

        /**
         * @return true or false depending on if row and columns have been set correctly
         */
        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

        /**
         * @return the next coordinate on the grid
         */
        @Override
        public GameObject next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}