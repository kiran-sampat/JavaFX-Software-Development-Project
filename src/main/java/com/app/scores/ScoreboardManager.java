package com.app.scores;

/**
 * Manages scoreboard information
 *
 */
public class ScoreboardManager implements Comparable<ScoreboardManager> {

    private final String m_mapSetName;

    private final String m_levelName;
    private final int m_levelNumber;

    private final String m_username;
    private final int m_moves;
    private final String m_time;

    /**
     * constructor for scoreboard manager
     *
     * @param mapSetName map set name as string
     * @param info instance of level info
     * @param stats instance of player stats
     */
    public ScoreboardManager(String mapSetName, LevelInfo info, PlayerStats stats) {
        this.m_mapSetName = mapSetName;
        this.m_levelName = info.getLevelName();
        this.m_levelNumber = info.getLevelNumber();
        this.m_username = stats.getUsername();
        this.m_moves = stats.getMoves();
        this.m_time = stats.getTime();
    }

    /**
     * compare function to help with sorting
     *
     * @param toSort the entry to sort
     * @return int value representing higher, lower or same
     */
    @Override
    public int compareTo(ScoreboardManager toSort) {
        if (this.m_time.equals(toSort.m_time)) {
            //if time is equal then compare moves in ASC
            return Integer.compare(this.m_moves, toSort.m_moves);
        } else {
            //return toSort.time.compareTo(this.time); // DESC
            //for comparing strings as numbers else '2' > '100' in ASC
            //return Integer.compare(Integer.parseInt(this.time),
            //Integer.parseInt(toSort.time));
            //solution okay for comparing times in format mm:ss:mm in ASC
            return this.m_time.compareTo(toSort.m_time);
        }
    }

    /**
     * the function to convert into comma separated line
     *
     * @return the csv row string
     */
    @Override
    public String toString() {
        return (m_mapSetName + ","
                + m_levelName + "," + m_levelNumber
                + "," + m_username + "," + m_moves + "," + m_time);
    }
}
