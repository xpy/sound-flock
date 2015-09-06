package xpy.sound_flock;

import ddf.minim.ugens.Frequency;

import java.util.Random;

/**
 * Note
 * Created by xpy on 06-Sep-15.
 */
public class Note {

    public float duration;
    public float pitch;
    public String pitchName;

    Note(float pitch, float duration) {

        this.duration = duration;
        this.pitch = pitch;
    }

    Note(String pitchName, float duration) {

        this.duration = duration;
        this.pitchName = pitchName;
        this.pitch = Frequency.ofPitch(pitchName).asHz();
    }


    public static String getRandomNote(){

        Random r = new Random();
        char c = (char) (r.nextInt(6) + 'A');

        return c+"4";
    }
}
