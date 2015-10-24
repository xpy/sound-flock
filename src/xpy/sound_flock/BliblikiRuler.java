package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import processing.core.PApplet;
import xpy.sound_flock.Body.BoidBody;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Distortions.PhraseDistortionGenerator;
import xpy.sound_flock.Instruments.*;

import java.util.ArrayList;
import java.util.List;

/**
 * BliblikiRuler
 * Created by xpy on 07-Oct-15.
 */
public class BliblikiRuler {

    public int blibliki;
    public int numOfInstances = 0;
    public int maxInstances;
    public int numOfPreludeInstances = 0;
    public int maxPreludeInstances;
    public int preluder;
    public int leaver;
    public int returner;

    public static PApplet     pa;
    public static AudioOutput out;

    public static final int B_SYNTH = 0;
    public static final int B_SPARK = 1;
    public static final int B_KICK  = 2;
    public static final int B_SNARE = 3;
    public static final int B_TONE  = 4;
    public static final int B_TSIK  = 5;

    public List<Blibliki> bliblikia = new ArrayList<>();

    public BliblikiRuler(int blibkiki, int maxInstances, int maxPreludeInstances, int preluder, int leaver, int returner) {
        this.blibliki = blibkiki;
        this.maxInstances = maxInstances;
        this.maxPreludeInstances = maxPreludeInstances;
        this.preluder = preluder;
        this.leaver = leaver;
        this.returner = returner;
    }

    public boolean canPrelude() {
        return numOfInstances < maxInstances && numOfPreludeInstances < maxPreludeInstances;
    }

    public void addBlibliki(Blibliki blibliki) {
        this.bliblikia.add(blibliki);
    }

    public void addBlibliki() {
        this.bliblikia.add(createBlibliki(blibliki));
        numOfInstances++;

    }

    public Blibliki createBlibliki(int bliblikiIndex) {


        switch (bliblikiIndex) {
            default:
                return new Blibliki(Phrases.synthPhrase(4), new SynthInstrumentGenerator(), new BoidBody(pa), out);
            case B_SPARK:
                return new Blibliki(Phrases.tinyPhrase(4), new SparkInstrumentGenerator(), new BoidBody(pa), out);
            case B_KICK:
                return new Blibliki(Phrases.kickPhrase(4), new KickInstrumentGenerator(), new BoidBody(pa), out);
            case B_SNARE:
                return new Blibliki(Phrases.widePhrase(4), new SnareInstrumentGenerator(), new BoidBody(pa), out);
            case B_TONE:
                return new Blibliki(Phrases.tonePhrase(4), new ToneInstrumentGenerator(), new BoidBody(pa), out);
            case B_TSIK:
                return new Blibliki(Phrases.tinyPhrase(4), new TsikInstrumentGenerator(), new BoidBody(pa), out);
        }

    }

    public void handleDistortions() {
        for (Blibliki blibliki : bliblikia) {
            blibliki.handleDistortion(0);
        }

    }

    public boolean hasSpace() {
        return numOfInstances < maxInstances;
    }
}
