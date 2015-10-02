package xpy.sound_flock.Distortions;

import processing.core.PApplet;
import xpy.sound_flock.Phrase;

import java.util.Random;

/**
 * FullToneDistortion
 * Created by xpy on 01-Oct-15.
 */
public class FullToneDistortion implements Distortion {

    private int    tuneAmount;
    private Phrase phrase;

    public FullToneDistortion (Phrase phrase) {
        this.phrase = phrase;
        Random r = new Random();
        tuneAmount = (r.nextInt(4) + 1) * (r.nextInt(2) * 2 - 1);
    }


    @Override
    public void apply () {
        PApplet.println("tuneAmount: " + tuneAmount);
        phrase.tune(tuneAmount);
    }

    @Override
    public void revert () {
        phrase.tune(tuneAmount * -1);
    }
}
