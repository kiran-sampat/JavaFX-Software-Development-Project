package com.app.model;

import com.app.engine.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    List<String> testList = new ArrayList<>();
    Level test;
    GameGrid grid;

    @BeforeEach
    void setUp() {
        testList = Arrays.asList(
                " WWWWWWWWWWWWWWWWWWWW ",
                " W     W       W    W ",
                " W                  W ",
                " WW                WW ",
                " W                  W ",
                " W                  W ",
                " W                  W ",
                " W                  W ",
                " W          C       W ",
                " W                  W ",
                " W     D            W ",
                " W                  W ",
                " W                  W ",
                " W                  W ",
                " WWWWWWW   WWWWWWWWWW ",
                " W                  W ",
                " W       S          W ",
                " W                  W ",
                " W                  W ",
                " WWWWWWWWWWWWWWWWWWWW ");

        test = new Level("Test Map", "Test Level", 1, testList);
        grid = test.getObjectsGrid();
    }

    @Test
    void translatePoint() {
        assertEquals(new Point(4, 4),
                GameGrid.translatePoint(
                        new Point(2, 2), new Point(2, 2)));
    }

    @Test
    void getTargetFromSource() {
        assertEquals(GameObject.FLOOR, grid.getTargetFromSource(
                        new Point(2, 2), new Point(2,2)));
    }

    @Test
    void GameObject() {
        assertTrue(grid.removeGameObjectAt(new Point(2, 2)));
    }

    @Test
    void putGameObjectAt() {
        int r, c;
        r = 5;
        c = 21;
        assertFalse(grid.putGameObjectAt(GameObject.CRATE, r, c));

        r = 9;
        c = 19;
        assertTrue(grid.putGameObjectAt(GameObject.CRATE, r, c));
    }
}