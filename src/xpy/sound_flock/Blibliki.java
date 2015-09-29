package xpy.sound_flock;

import ddf.minim.AudioOutput;
import processing.core.*;
import xpy.sound_flock.Instruments.InstrumentGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Blibliki
 * Created by xpy on 05-Sep-15.
 */
public class Blibliki extends PApplet/* implements BitListener*/ {

    AudioOutput         out;
    InstrumentGenerator instrumentGenerator;

    List<LoopEvent> loopEvents = new ArrayList<>();

//    long duration;

    long nextCheck;
    public int loops = 0;
    long beatTime   = (long) (60000f / 120f);
    int  offset     = 0;
    int  offsetFlag = 1;
    private Phrase phrase;

    public Blibliki (Phrase phrase, InstrumentGenerator instrumentGenerator, AudioOutput out) {
        this.phrase = phrase;
        this.out = out;
        this.instrumentGenerator = instrumentGenerator;
    }

/*
    public static Blibliki createRandomBlibliki (AudioOutput out) {

        Blibliki ret = new Blibliki(out);
        Phrase   p   = new Phrase();
        p.randomizePhrase();
        ret.addPhrase(p);
        return ret;
    }
*/

    public void addPhrase (Phrase phraseToAdd) {

        phrase = phraseToAdd;
    }

    public void setNotes () {
        out.pauseNotes();
        float i = 0;

        for (LoopEvent loopEvent : loopEvents) {
            println("LOOPEVENT");
            loopEvent.fire(loops);
        }
//            offset += offsetFlag;
//            offsetFlag *= -1;

        for (Note note : phrase.notes) {

            out.playNoteAtBeat(phrase.getPhraseMeters(), i, Math.min(note.duration, instrumentGenerator.getMaxDuration()), instrumentGenerator.createInstrument(note.pitchOffset(offset), instrumentGenerator.getAmplitude(), out));
            i += note.duration;
        }
        out.resumeNotes();
    }


    public void update () {

        if (System.currentTimeMillis() - nextCheck >= 0 && loops < 100) {
            println("loops:" + loops);
            setNotes();
            loops++;
            nextCheck = System.currentTimeMillis() + out.nextMeterStart(phrase.getPhraseMeters()) + 100;
        }
    }

    public float millisToBeats (long millis) {
        return ((float) millis / (float) beatTime);
    }

    public void start () {
        nextCheck = System.currentTimeMillis() + out.nextMeterStart(phrase.meterLength) + 100;
    }

    public void tunePhrase (Integer[] noteIndexes, Integer[] tuneAmounts) {
        phrase.tune(noteIndexes, tuneAmounts);
    }

    public void tunePhrase (Integer tuneAmount) {
        phrase.tune(tuneAmount);
    }

    public void resetPhrase(){
        phrase.reset();
    }
    public void addLoopEvent (LoopEvent loopEvent) {
        loopEvents.add(loopEvent);
    }

    /**
     * Created by xpy on 29-Sep-15.
     */
    public static class LoopEvent {
        public void fire (int loopNum) {

        }
    }
}
