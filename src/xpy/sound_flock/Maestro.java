package xpy.sound_flock;

import ddf.minim.AudioOutput;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Maestro
 * Created by xpy on 05-Oct-15.
 */
public class Maestro {
    private AudioOutput out;

    private long nextCheck;
    private int loops       = 0;
    private int meterLength = 4;

    public Maestro (AudioOutput out) {
        this.out = out;
    }

    private List<Blibliki> bliblikia = new ArrayList<>();

    public void start () {
        nextCheck = System.currentTimeMillis() + out.nextMeterStart(meterLength) + 100;
    }

    public void update () {

        if (System.currentTimeMillis() - nextCheck >= 0 && loops < 100) {
            PApplet.println("Maestro Loops:" + loops);
            int nextMeterStart;
            out.pauseNotes();
            nextMeterStart = out.nextMeterStart(meterLength);

            for (Blibliki aBliblikia : bliblikia) {

                PApplet.println("aBliblikia Loops:" + (loops - aBliblikia.startingLoop));
                if (!aBliblikia.isPlaying) {
                    aBliblikia.setNotes(meterLength);


                    aBliblikia.isPlaying = true;
                    aBliblikia.startingLoop = loops;
                } else if (loops - aBliblikia.startingLoop == aBliblikia.getPhrase().phraseLength) {
                    aBliblikia.startingLoop = loops;
                    aBliblikia.setNotes(meterLength);
                }
                out.resumeNotes();
            }
            loops++;
            nextCheck = System.currentTimeMillis() + nextMeterStart + 100;

            PApplet.println("nextCheck: " + nextCheck);
            out.resumeNotes();
        }

        for (Blibliki aBliblikia : bliblikia) {
            if (aBliblikia.hasBody())
                aBliblikia.getBody().update();
        }


    }

    public void addBlibliki (Blibliki blibliki) {
        this.bliblikia.add(blibliki);
    }
}
