package com.app.scores;

/**
 * Contains scoreboard information for the level
 *
 */
public class LevelInfo {

    private final String m_levelName;
    private final int m_levelNumber;

    /**
     * constructor for level info
     *
     * @param levelName name of level as string
     * @param levelNumber the level number as an int
     */
    public LevelInfo(String levelName, int levelNumber) {
        this.m_levelName = levelName;
        this.m_levelNumber = levelNumber;
    }

    public String getLevelName() {
        return m_levelName;
    }

    public int getLevelNumber() {
        return m_levelNumber;
    }
}
