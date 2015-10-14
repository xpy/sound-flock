package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.Sink;
import processing.core.PApplet;
import xpy.sound_flock.Distortions.*;

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


    public boolean             lastForAll     = false;
    public List<BliblikiRuler> bliblikiRulers = new ArrayList<>();

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

        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SYNTH, 2, 1, 10, 0, 0));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SPARK, 2, 1, 8, 5, 5));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_KICK, 1, 1, 7, 0, 0));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_SNARE, 2, 0, 0, 2, 3));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_TONE, 2, 1, 3, 2, 8));
        bliblikiRulers.add(new BliblikiRuler(BliblikiRuler.B_TSIK, 3, 1, 5, 5, 5));
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

                for (BliblikiRuler bliblikiRuler : bliblikiRulers) {
                    for (Blibliki blibliki : bliblikiRuler.bliblikia) {
                        if (blibliki.hasStarted && blibliki.getPhrase().phraseLength - (loops - blibliki.startingLoop) == 1 || blibliki.getPhrase().phraseLength == 1) {
                            numOfLast++;
                        }
                        if (!blibliki.hasStarted) {
                            blibliki.setNotes(meterLength);
                            blibliki.hasStarted = true;
                            blibliki.startingLoop = loops;
                        } else if (loops - blibliki.startingLoop == blibliki.getPhrase().phraseLength) {

                            // LOOPS
                            blibliki.startingLoop = loops;
                            if (!blibliki.isPaused)
                                blibliki.setNotes(meterLength);
                        }
                    }

                    bliblikiRuler.handleDistortions();
                }

                PApplet.println("numOfLast: " + numOfLast);

                if (r.nextInt(10) > 2 && numOfLast == numOfBliblikia()) {
                    addBliblikiByRules();
//                    addBlibliki(createBlibliki(B_SYNTH));
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
                    validBlibliki.add(bliblikiRulers.get(i).blibliki);
            }
            if (validBlibliki.size() == 0)
                return false;
            int nextBlibliki = r.nextInt(validBlibliki.size());
            bliblikiRulers.get(validBlibliki.get(nextBlibliki)).addBlibliki();


        }
        return false;
    }
}
