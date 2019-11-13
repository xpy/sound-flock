package xpy.sound_flock;

import ddf.minim.AudioOutput;
import processing.core.PApplet;
import xpy.sound_flock.Body.*;
import xpy.sound_flock.Distortions.Distortion;
import xpy.sound_flock.Distortions.PhraseDistortionGenerator;
import xpy.sound_flock.Instruments.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BliblikiRuler
 * Created by xpy on 07-Oct-15.
 */
public class BliblikiRuler {

    public int instrument;
    public int phraseType;
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

    public BliblikiRuler(int instrument, int phraseType, int maxInstances, int maxPreludeInstances, int preluder, int leaver, int returner) {
        this.instrument = instrument;
        this.phraseType = phraseType;
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
        this.bliblikia.add(createBlibliki(instrument));
        numOfInstances++;

    }

    public Blibliki createBlibliki(int bliblikiIndex) {


        switch (bliblikiIndex) {
            default:
                return new Blibliki(Phrases.getPhrase(phraseType, 4), new SynthInstrumentGenerator(), new ConstellationBody(pa), out);
            case B_SPARK:
                return new Blibliki(Phrases.getPhrase(phraseType, 4), new SparkInstrumentGenerator(), new ConstellationBody(pa), out);
            case B_KICK:
                return new Blibliki(Phrases.getPhrase(phraseType, 4), new KickInstrumentGenerator(), new ConstellationBody(pa), out);
            case B_SNARE:
                return new Blibliki(Phrases.getPhrase(phraseType, 4), new SnareInstrumentGenerator(), new ConstellationBody(pa), out);
            case B_TONE:
                return new Blibliki(Phrases.getPhrase(phraseType, 4), new ToneInstrumentGenerator(), new ConstellationBody(pa), out);
            case B_TSIK:
                return new Blibliki(Phrases.getPhrase(phraseType, 4), new TsikInstrumentGenerator(), new ConstellationBody(pa), out);
        }

    }

    public void handleDistortions() {

        for (Blibliki blibliki : bliblikia) {
            if (blibliki.distortionApplications.size() < 1)
                blibliki.addDistortion(PhraseDistortionGenerator.createDistortion((new Random()).nextInt(4), blibliki));
            for (int i = 0; i < blibliki.distortionApplications.size(); i++) {

                blibliki.handleDistortion(i);
            }
        }

    }

    public boolean hasSpace() {
        return numOfInstances < maxInstances;
    }
}
