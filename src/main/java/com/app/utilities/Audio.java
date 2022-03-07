package com.app.utilities;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * For audio in game
 *
 */
public class Audio {

    private static Audio single_instance;
    private MediaPlayer player;
    private boolean created = false;

    /**
     * singleton instance
     *
     * @return the single instance created
     */
    public static Audio getInstance() {
        if (single_instance == null)
            single_instance = new Audio();

        return single_instance;
    }

    /**
     * Creates a new music player, only if a player has not been created before
     */
    public void createPlayer() {
        if (!created) {
            Media music = new Media(getClass().getClassLoader().getResource(
                    "puzzle_theme.wav").toString());
            player = new MediaPlayer(music);
            player.setVolume(0.12);
            player.setCycleCount(AudioClip.INDEFINITE);
            //playMusic(); //todo set as off by default
            created = true;
        }
    }

    /**
     * play music
     */
    public void playMusic() {
        player.play();
    }

    /**
     * pause music
     */
    public void pauseMusic() {
        player.pause();
    }

    /**
     * stop music
     */
    public void stopMusic() {
        player.stop();
    }

    /**
     * Calls different function depending on the value of is playing music
     */
    public void toggleMusic() {
        if (!isPlayingMusic())
            playMusic();
        else
            pauseMusic();
    }

    /**
     * @return returns boolean based on if the status of Music Player is playing
     */
    public boolean isPlayingMusic() {
        return player.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
