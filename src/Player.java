import javafx.scene.media.AudioClip;

import java.util.*;

/**
 * Created by celinaperalta on 5/17/17.
 */
public class Player {

    //loop has 16 beats, each beat contains arraylist for different instruments

    private Sound[][] audioClips = new Sound[16][17];
    private boolean[][] beats = new boolean[16][17];
    private static int beat_count = 0;


    public Player() throws Exception{

        //initialize instrument grid
        for (int i = 0; i < audioClips.length; i++) {
            for (int j = 0; j < audioClips[i].length; j++) {
                audioClips[i][j] = new Sound(DrumSounds.audioNames[j]);
            }
        }

    }

    public void updateLoop(int instrument, boolean[] newLoop) {

        //will be called after each loop

        //assuming instrument is the index of the appropriate audioclip in the array
        for (int i = 0; i < audioClips.length; i++) {
            beats[i][instrument] = newLoop[i];
        }

    }

    public void clearLoop(int instrument) {

        for (int i = 0; i < audioClips.length; i++) {
            beats[i][instrument] = false;
        }

    }

    public void play() {

        if (beat_count == 15)
            beat_count = 0;
        else
            beat_count++;

        for (int j = 0; j < audioClips[beat_count].length; j++) {
            if (beats[beat_count][j])
                audioClips[beat_count][j].play();
        }

    }
}
