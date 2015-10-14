package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.Sink;
import processing.core.PApplet;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Distortions.*;
import xpy.sound_flock.Instruments.*;

import java.util.*;

/**
 * Maestro
 * Created by xpy on 05-Oct-15.
 */
public class Maestro {
    private AudioOutput out;

    private long nextCheck;
    public  int loops       = 0;
    private int meterLength = 4;
    private PApplet pa;
    private int     numOfLoops       = 50;
    private boolean isPrelude        = true;
    private int     preludeEnd       = 10;
    private int     preludeEndChance = 0;

    public final static Sink sink = new Sink();

    public static final int B_SYNTH = 0;
    public static final int B_SPARK = 1;
    public static final int B_KICK  = 2;
    public static final int B_SNARE = 3;
    public static final int B_TONE  = 4;
    public static final int B_TSIK  = 5;

    public boolean             lastForAll     = false;
    public List<BliblikiRuler> bliblikiRulers = new ArrayList<>();

    public static void main(String args[]) {
        // full-screen mode can be activated via parameters to PApplets main method.
        PApplet.main(new String[]{"xpy.sound_flock.Sound_flock"});
    }

    public Maestro(PApplet pa, AudioOutput out) {
        this.out = out;
        this.pa = pa;
        sink.patch(out);

        bliblikiRulers.add(new BliblikiRuler(B_SYNTH, 2, 1, 10, 0, 0));
        bliblikiRulers.add(new BliblikiRuler(B_SPARK, 2, 1, 8, 5, 5));
        bliblikiRulers.add(new BliblikiRuler(B_KICK, 1, 1, 7, 0, 0));
        bliblikiRulers.add(new BliblikiRuler(B_SNARE, 2, 0, 0, 2, 3));
        bliblikiRulers.add(new BliblikiRuler(B_TONE, 2, 1, 3, 2, 8));
        bliblikiRulers.add(new BliblikiRuler(B_TSIK, 3, 1, 5, 5, 5));
    }

    private List<Blibliki> bliblikia = new ArrayList<>();

    public void start() {
        nextCheck = System.currentTimeMillis() + out.nextMeterStart(meterLength) + 100;
    }

    public void update() {
        Random r = new Random();
        if (System.currentTimeMillis() - nextCheck >= 0) {
            out.pauseNotes();
            int nextMeterStart = out.nextMeterStart(meterLength);

            if (loops < numOfLoops) {
                PApplet.println("Maestro Loops:" + loops);
                PApplet.println("Maestro Bliblikia:" + bliblikia.size());
                int numOfLast = 0;

                for (Blibliki aBliblikia : bliblikia) {
                    if (aBliblikia.isPlaying && aBliblikia.getPhrase().phraseLength - (loops - aBliblikia.startingLoop) == 1 || aBliblikia.getPhrase().phraseLength == 1) {
                        numOfLast++;
                    }
                    if (!aBliblikia.isPlaying) {
                        aBliblikia.setNotes(meterLength);

                        aBliblikia.isPlaying = true;
                        aBliblikia.startingLoop = loops;
                    } else if (loops - aBliblikia.startingLoop == aBliblikia.getPhrase().phraseLength) {

                        // LOOPS
                        aBliblikia.startingLoop = loops;
//                        if (r.nextInt(10) > 2 && numOfLast != bliblikia.size())
                        aBliblikia.setNotes(meterLength);

                        // DISTORTIONS
                        if (aBliblikia.loops % 2 == 0 && aBliblikia.loops > 0 && aBliblikia.loops % 4 != 0) {
                            if (aBliblikia.distortions.size() < 1)
                                aBliblikia.addDistortion(PhraseDistortionGenerator.createDistortion(1, aBliblikia));
                            aBliblikia.applyDistortion(0);
                            if (r.nextBoolean())
                                aBliblikia.applyDistortion(0);

                        }
                        if (aBliblikia.loops % 4 == 0 && aBliblikia.loops > 0) {
                            aBliblikia.revertDistortion(0);
                            if (r.nextBoolean())
                                aBliblikia.revertDistortion(0);

                        }

                    }
                }

                PApplet.println("numOfLast: " + numOfLast);

                if (r.nextInt(10) > 2 && numOfLast == bliblikia.size()) {
                    addBliblikiByRules();
//                    addBlibliki(createBlibliki(B_SYNTH));
                }

            }
            nextCheck = System.currentTimeMillis() + nextMeterStart + 100;

            loops++;

            out.resumeNotes();
        }

        for (Blibliki aBliblikia : bliblikia) {
            if (aBliblikia.hasBody())
                aBliblikia.getBody().update();
        }


    }

    public void addBlibliki(Blibliki blibliki) {
        this.bliblikia.add(blibliki);
    }

    public void addBlibliki(BliblikiRuler bliblikiRuler) {
        this.bliblikia.add(createBlibliki(bliblikiRuler.blibliki));
        bliblikiRuler.numOfInstances++;
    }

    public Blibliki createBlibliki() {

        Random r = new Random();

        return createBlibliki(r.nextInt(5));

    }

    public Blibliki createBlibliki(int bliblikiIndex) {


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
            case B_TSIK:
                return new Blibliki(Phrases.tinyPhrase(4), new TsikInstrumentGenerator(), new CircleBody(pa), out);
        }

    }

    public boolean addPreludeBlibliki() {
        Random                          r          = new Random();
        boolean                         added      = false;
        HashMap<BliblikiRuler, Integer> preludeMap = new HashMap<>();
        int                             chanceSum  = 0;

        for (BliblikiRuler bliblikiRuler : bliblikiRulers) {
            if (bliblikiRuler.preluder > 0 && bliblikiRuler.canPrelude()) {
                chanceSum += bliblikiRuler.preluder;
                preludeMap.put(bliblikiRuler, chanceSum);
            }
        }
        if (chanceSum > 0) {
            int next = r.nextInt(chanceSum);
            for (HashMap.Entry<BliblikiRuler, Integer> pm : preludeMap.entrySet()) {
                if (pm.getValue() > next) {
                    addBlibliki(createBlibliki(pm.getKey().blibliki));
                    pm.getKey().numOfPreludeInstances++;
                    pm.getKey().numOfInstances++;
                    added = true;
                    break;
                }

            }
        }
        int pe = r.nextInt(preludeEnd);
//            PApplet.println("pe: "+pe);
//            PApplet.println("preludeEndChance: "+preludeEndChance);
        isPrelude = pe > preludeEndChance++;
        return added;

    }

    public boolean addBliblikiByRules() {

        Random r = new Random();
        PApplet.println("isPrelude: " + isPrelude);
        if (isPrelude) {
            return addPreludeBlibliki();
        } else {
            List<Integer> validBlibliki = new ArrayList<>();

            for (int i = 0; i < bliblikiRulers.size(); i++) {
                if (bliblikiRulers.get(i).hasSpace())
                    validBlibliki.add(bliblikiRulers.get(i).blibliki);
            }
            if (validBlibliki.size() == 0)
                return false;
            int nextBlibliki = r.nextInt(validBlibliki.size());
            bliblikiRulers.get(validBlibliki.get(nextBlibliki)).numOfInstances++;
            addBlibliki(createBlibliki(validBlibliki.get(nextBlibliki)));

        }
        return false;
    }
}
