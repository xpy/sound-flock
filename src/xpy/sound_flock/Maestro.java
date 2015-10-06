package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.Sink;
import processing.core.PApplet;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Distortions.FullToneDistortion;
import xpy.sound_flock.Distortions.PartialToneDistortion;
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

    public static final int B_SYNTH = 0;
    public static final int B_SPARK = 1;
    public static final int B_KICK  = 2;
    public static final int B_SNARE = 3;
    public static final int B_TONE  = 4;

    public List<BliblikiRuler> bliblikiRulers = new ArrayList<>();

    public Maestro (PApplet pa, AudioOutput out) {
        this.out = out;
        this.pa = pa;
        sink.patch(out);

        bliblikiRulers.add(new BliblikiRuler(B_SYNTH, 3));
        bliblikiRulers.add(new BliblikiRuler(B_SPARK, 5));
        bliblikiRulers.add(new BliblikiRuler(B_KICK, 2));
        bliblikiRulers.add(new BliblikiRuler(B_SNARE, 5));
        bliblikiRulers.add(new BliblikiRuler(B_TONE, 1));
    }

    private List<Blibliki> bliblikia = new ArrayList<>();

    public void start () {
        nextCheck = System.currentTimeMillis() + out.nextMeterStart(meterLength) + 100;
    }

    public void update () {
        Random r = new Random();
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
                    if (aBliblikia.loops % 2 == 0 && aBliblikia.loops > 0 && aBliblikia.loops % 4 != 0) {
                        if (aBliblikia.distortions.size() < 1)
                            aBliblikia.addDistortion(new FullToneDistortion(aBliblikia.getPhrase()));
                        aBliblikia.applyDistortion(0);

                    }
                    if (aBliblikia.loops % 4 == 0 && aBliblikia.loops > 0) {
                        aBliblikia.revertDistortion(0);

                    }

                    }
                out.resumeNotes();
            }
            loops++;
            nextCheck = System.currentTimeMillis() + nextMeterStart + 100;
            if (r.nextInt(10) > 4) {
                addBliblikiByRules();
            }
/*
            if (bliblikia.size() > 7) {
                bliblikia.remove(0);
            }
*/
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

        return createBlibliki(r.nextInt(5));

    }

    public Blibliki createBlibliki (int bliblikiIndex) {


        switch (bliblikiIndex) {
            default:
                return new Blibliki(Phrases.synthPhrase(4), new SynthInstrumentGenerator(), new CircleBody(pa), out);
            case B_SPARK:
                return new Blibliki(Phrases.tinyPhrase(4), new SparkInstrumentGenerator(), new CircleBody(pa), out);
            case B_KICK:
                return new Blibliki(Phrases.kickPhrase(4), new KickInstrumentGenerator(), new CircleBody(pa), out);
            case B_SNARE:
                return new Blibliki(Phrases.widePhrase(4), new SnareInstrumentGenerator(), new CircleBody(pa), out);
            case B_TONE:
                return new Blibliki(Phrases.tonePhrase(4), new ToneInstrumentGenerator(), new CircleBody(pa), out);
        }

    }

    public boolean addBliblikiByRules () {

        Random        r             = new Random();
        List<Integer> validBlibliki = new ArrayList<>();
        for (int i = 0; i < bliblikiRulers.size(); i++) {
            if (bliblikiRulers.get(i).hasSpace())
                validBlibliki.add(i);
        }
        if (validBlibliki.size() == 0)
            return false;
        int nextBlibliki = r.nextInt(validBlibliki.size());
        bliblikiRulers.get(validBlibliki.get(nextBlibliki)).numOfInstances++;
        addBlibliki(createBlibliki(validBlibliki.get(nextBlibliki)));
        return false;
    }
}