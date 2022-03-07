package com.app.scores;

/**
 * Contains scoreboard information for the player
 *
 */
public class PlayerStats {

    private final String m_username;
    private final int m_moves;
    private final String m_time;

    /**
     * the player stats for the leaderboard
     *
     * @param username the player name as a string
     * @param moves the player moves as int
     * @param time the completion time as a string
     */
    public PlayerStats(String username, int moves, String time) {
        this.m_username = username;
        this.m_moves = moves;
        this.m_time = time;
    }

    public String getUsername() {
        return m_username;
    }

    public int getMoves() {
        return m_moves;
    }

    public String getTime() {
        return m_time;
    }
}
