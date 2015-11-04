package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.Sink;
import processing.core.PApplet;

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
    public  int     numOfLoops       = 100;
    private boolean isPrelude        = true;
    private int     preludeEnd       = 10;
    private int     preludeEndChance = 0;
    public  boolean forcePut         = true;

    public final static Sink sink = new Sink();


    public boolean             lastForAll     = false;
    public List<BliblikiRuler> bliblikiRulers = new ArrayList<>();
    private boolean silence;

    public static void main(String args[]) {
        // full-screen mode can be activated via parameters to PApplets main method.
        PApplet.main(new String[]{"xpy.sound_flock.Sound_flock"});
    }

    public Maestro(PApplet pa, AudioOutput out) {
        this.out = out;
        this.pa = pa;

        BliblikiRuler.out = this.out;
        BliblikiRuler.pa = this.pa;

        sink.patch(out);

        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SYNTH, Phrases.P_SYNTH_PHRASE, 1, 1, 10, 0, 10));
//        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SYNTH, Phrases.P_TINY_PHRASE, 1, 1, 5, 2, 2));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SYNTH, Phrases.P_TONE, 1, 0, 1, 2, 2));

        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SPARK, Phrases.P_TINY_PHRASE, 1, 1, 8, 2, 1));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SPARK, Phrases.P_TONE, 1, 1, 1, 2, 1));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SPARK, Phrases.P_WIDE_PHRASE, 1, 1, 1, 2, 1));

        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_KICK, Phrases.P_KICK_PHRASE, 1, 0, 0, 0, 10));

        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SNARE, Phrases.P_WIDE_PHRASE, 3, 0, 0, 2, 1));

        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_TONE, Phrases.P_TONE, 1, 1, 3, 1, 3));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_TONE, Phrases.P_WIDE_PHRASE, 1, 0, 3, 1, 1));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_TONE, Phrases.P_TINY_PHRASE, 1, 0, 1, 1, 2));

        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_TSIK, Phrases.P_TINY_PHRASE, 1, 1, 5, 2, 1));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_TSIK, Phrases.P_WIDE_PHRASE, 1, 1, 1, 2, 1));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_TSIK, Phrases.P_TONE, 1, 1, 1, 2, 1));
    }


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
                PApplet.println("Maestro Bliblikia:" + numOfBliblikia());
                int numOfLast = 0;
                int numOfPreLast = 0;
                // GET numOfLast

                for (BliblikiRuler bliblikiRuler : bliblikiRulers) {
                    for (Blibliki blibliki : bliblikiRuler.bliblikia) {
//                        if (blibliki.hasStarted && !blibliki.isPaused) {
//                            PApplet.println("blibliki.isLastLoop(): " + blibliki.isLastLoop(loops));
//                            PApplet.println("blibliki.startingLoop: " + blibliki.startingLoop);
//                            PApplet.println("blibliki.loops: " + loops);
//                            PApplet.println("blibliki.getPhrase().phraseLength: " + blibliki.getPhrase().phraseLength);
//                        }
                        if (silence /*|| (loops - blibliki.startingLoop - blibliki.getPhrase().phraseLength == 0)*/) {
                            if (!blibliki.isPaused /*&& bliblikiRuler.leaver > 0*/) {
                                blibliki.isPaused = silence /*|| r.nextInt(20) < bliblikiRuler.leaver*/;
                                if (blibliki.isPaused)
                                    blibliki.leaved = true;
                            }
                        }
                        if (blibliki.hasStarted && !blibliki.isPaused) {
                            if (blibliki.isLastLoop(loops))
                                numOfLast++;
                            if (blibliki.isLastLoop(loops + 1))
                                numOfPreLast++;
                        }
                    }
                }

                int numOfPlayingBliblikia = numOfPlayingBliblikia();
                PApplet.println("Maestro Playing Bliblikia:" + numOfPlayingBliblikia);
                PApplet.println("numOfLast: " + numOfLast);
                PApplet.println("numOfPreLast: " + numOfPreLast);
                silence = (numOfPlayingBliblikia - numOfPreLast == 0) && r.nextInt(100) < 20;
                if (silence)
                    PApplet.println("---------------------------SILENCE!");

                if (forcePut || (r.nextInt(10) > 2 && numOfLast == numOfPlayingBliblikia)) {
                    forcePut = false;
                    addBliblikiByRules();
                }

                for (BliblikiRuler bliblikiRuler : bliblikiRulers) {
                    for (Blibliki blibliki : bliblikiRuler.bliblikia) {

                        if (!silence && !blibliki.leaved) {
                            if (blibliki.isPaused /*&& bliblikiRuler.returner > 0 */ && numOfLast == numOfPlayingBliblikia) {
                                blibliki.isPaused = false;//r.nextInt(20) > bliblikiRuler.returner;
//                                if (!blibliki.isPaused)
                                    blibliki.startingLoop = loops - blibliki.getPhrase().phraseLength;
                            }
                        }

                        if (!blibliki.isPaused) {
                            if (!blibliki.hasStarted) {
                                blibliki.setNotes(meterLength);
                                blibliki.hasStarted = true;
                                blibliki.startingLoop = loops;
                            } else if (blibliki.isLastLoop(loops)) {
                                blibliki.startingLoop = loops;

                                blibliki.setNotes(meterLength);
                            }
                        }
                        blibliki.leaved = false;
                    }

                    bliblikiRuler.handleDistortions();
                }


            }
            nextCheck = System.currentTimeMillis() + nextMeterStart + 100;

            loops++;

            out.resumeNotes();
        }

        for (BliblikiRuler bliblikiRuler : bliblikiRulers) {
            for (Blibliki aBliblikia : bliblikiRuler.bliblikia) {
                if (aBliblikia.hasBody())
                    aBliblikia.getBody().update();
            }
        }


    }

    public int numOfBliblikia() {
        int sum = 0;
        for (BliblikiRuler bliblikiRuler : bliblikiRulers) {
            sum += bliblikiRuler.bliblikia.size();
        }
        return sum;
    }

    public int numOfPlayingBliblikia() {
        int sum = 0;
        for (BliblikiRuler bliblikiRuler : bliblikiRulers) {
/*
            for (Blibliki blibliki : bliblikiRuler.bliblikia) {
                if (!blibliki.isPaused)
                    sum += 1;
            }
*/
            sum += bliblikiRuler.bliblikia.stream().filter(blibliki -> !blibliki.isPaused && blibliki.hasStarted).count();
        }
        return sum;
    }


    public void addBlibliki(BliblikiRuler bliblikiRuler) {
        bliblikiRuler.addBlibliki();
        bliblikiRuler.numOfInstances++;
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
                    pm.getKey().addBlibliki();
                    pm.getKey().numOfPreludeInstances++;
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
                    validBlibliki.add(i);
            }
            if (validBlibliki.size() == 0)
                return false;
            int nextBlibliki = r.nextInt(validBlibliki.size());
            bliblikiRulers.get(validBlibliki.get(nextBlibliki)).addBlibliki();

        }
        return false;
    }
}
