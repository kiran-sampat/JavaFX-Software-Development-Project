package com.app.controller;

import com.app.engine.GameObject;
import com.app.model.GameGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameGridTest {
    private GameGrid gameGrid;
    private final int rows = 20;
    private final int columns = 20;

    @BeforeEach
    void setUp() {
        gameGrid = new GameGrid(columns, rows);
    }

    @Test
    void putGameObjectAt1() {
        int rowNum = rows + 1; //out of bounds
        int colNum = columns + 1;
        assertFalse(gameGrid.putGameObjectAt(GameObject.WALL, rowNum, colNum));
    }

    @Test
    void putGameObjectAt2() {
        int rowNum = rows - 1;
        int colNum = columns - 1;
        assertTrue(gameGrid.putGameObjectAt(GameObject.WALL, rowNum, colNum));
    }

    @Test
    void putGameObjectAt3() {
        int rowNum = rows; //out of bounds
        int colNum = columns;
        assertFalse(gameGrid.putGameObjectAt(GameObject.WALL, rowNum, colNum));
    }

    @Test
    void putGameObjectAt4() {
        gameGrid.putGameObjectAt(GameObject.CRATE, 0, 0);
        assertTrue(gameGrid.putGameObjectAt(GameObject.CRATE, 0, 0));
    }
}
