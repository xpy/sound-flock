package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.Sink;
import processing.core.PApplet;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Instruments.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Maestro
 * Created by xpy on 05-Oct-15.
 */
public class Maestro {
    private AudioOutput out;

    private long nextCheck;
    private int loops       = 0;
    private int meterLength = 4;
    private PApplet pa;
    private int numOfLoops = 50;

    public final static Sink sink = new Sink();

    public Maestro (PApplet pa, AudioOutput out) {
        this.out = out;
        this.pa = pa;
        sink.patch(out);
    }

    private List<Blibliki> bliblikia = new ArrayList<>();

    public void start () {
        nextCheck = System.currentTimeMillis() + out.nextMeterStart(meterLength) + 100;
    }

    public void update () {

        if (System.currentTimeMillis() - nextCheck >= 0 && loops < numOfLoops) {
            PApplet.println("Maestro Loops:" + loops);
            PApplet.println("Maestro Bliblikia:" + bliblikia.size());
            int nextMeterStart;
            out.pauseNotes();
            nextMeterStart = out.nextMeterStart(meterLength);

            for (Blibliki aBliblikia : bliblikia) {

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
            if ((new Random()).nextInt(10) >2)
                addBlibliki(createBlibliki());
            if(bliblikia.size() > 10)
                PApplet.println(18);
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

    public Blibliki createBlibliki () {

        Random r = new Random();

        switch (0) {
            default:
                return new Blibliki(Phrases.synthPhrase(4), new SynthInstrumentGenerator(), new CircleBody(pa), out);
            case 1:
                return new Blibliki(Phrases.tinyPhrase(4), new SparkInstrumentGenerator(), new CircleBody(pa), out);
            case 2:
                return new Blibliki(Phrases.kickPhrase(4), new KickInstrumentGenerator(), new CircleBody(pa), out);
            case 3:
                return new Blibliki(Phrases.tonePhrase(4), new SnareInstrumentGenerator(), new CircleBody(pa), out);
            case 4:
                return new Blibliki(Phrases.tonePhrase(4), new ToneInstrumentGenerator(), new CircleBody(pa), out);
        }

    }
}
