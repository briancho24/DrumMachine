

/**
 * Created by celinaperalta on 5/17/17.
 */
public class Player {

    //loop has 16 beats, each beat contains arraylist for different instruments

    private Sound[][] audioClips = new Sound[16][DrumSounds.NUM_SOUNDS];
    private boolean[][] beats = new boolean[16][DrumSounds.NUM_SOUNDS];
    private static int beat_count = 0;
    private boolean timeSignature4 = true;
    private short totalBeats = 15;

    public Player() throws Exception {

        //initialize instrument grid
        for (int i = 0; i < audioClips.length; i++) {
            for (int j = 0; j < audioClips[i].length; j++) {
                Sound newSound = new Sound(DrumSounds.audioNames[j]);
                newSound.setGain(-25);
                audioClips[i][j] = newSound;
            }
        }
    }

    public void addLoop(int instrument, boolean[] newLoop) {

        //will be called after each loop

        //assuming instrument is the index of the appropriate audioclip in the array
        for (int i = 0; i < audioClips.length; i++) {
            beats[i][instrument] = newLoop[i];
        }
    }

    public synchronized void clearLoop() {
        for (int y = 0; y < beats.length; y++) {
            for (int x = 0; x < beats[y].length; x++)
                beats[y][x] = false;
        }

        beat_count = 0;
    }


    public void play() {

        for (int j = 0; j < audioClips[beat_count].length; j++) {
            if (beats[beat_count][j]) {
                audioClips[beat_count][j].play();
            }
        }

        if (beat_count >= totalBeats)
            beat_count = 0;
        else
            beat_count++;

    }

    public int getBeat() {
        return beat_count;
    }

    public void setTimeSignature4(boolean timeSignature) {
        timeSignature4 = timeSignature;
        if (timeSignature4) {
            totalBeats = 15;
        } else if (!timeSignature4) {
            totalBeats = 11;
        }
    }

    public void setGain(int instrument, float gain) {
        try {
            for (int j = 0; j < audioClips.length; j++) {
                Sound newSound = new Sound(DrumSounds.audioNames[instrument]);
                newSound.setGain(gain);
                audioClips[j][instrument] = newSound;
            }
        } catch (Exception e) {
        }
    }

    public boolean isTime4() {
        return timeSignature4;
    }
}