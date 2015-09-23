package xpy.sound_flock;

import ddf.minim.AudioOutput;
import processing.core.*;

/**
 * Blibliki
 * Created by xpy on 05-Sep-15.
 */
public class Blibliki extends PApplet/* implements BitListener*/ {

    AudioOutput         out;
    InstrumentGenerator instrumentGenerator;

//    long duration;

    long nextCheck;
    int  loops    = 0;
    long beatTime = (long) (60000f / 120f);
    int  offset   = 0;

    private Phrase phrase;

    Blibliki (AudioOutput out) {
        this.out = out;
    }

    Blibliki (int numOfNotes, int meterLength, int phraseLength, InstrumentGenerator instrumentGenerator, AudioOutput out) {
        this.out = out;
        this.instrumentGenerator = instrumentGenerator;
    }

    public static Blibliki createRandomBlibliki (AudioOutput out) {

        Blibliki ret = new Blibliki(out);
        Phrase   p   = new Phrase();
        p.randomizePhrase();
        ret.addPhrase(p);
        return ret;
    }

    public void addPhrase (Phrase phraseToAdd) {

        phrase = phraseToAdd;
    }

    public void setNotes (/*float offset*/) {
        out.pauseNotes();
        float i = 0;
        if (loops % 2 == 0 && loops != 0) {
            offset++;
        }

        for (Note note : phrase.notes) {
            out.playNoteAtBeat(phrase.getPhraseMeters(), i, note.duration, instrumentGenerator.createInstrument(note.pitchOffset(offset), 0.49f, out));
            i += note.duration;
//            println("Note With Offset: "+note.pitchOffset(offset));

        }

//        println("=============");
        out.resumeNotes();
    }


    public void update () {

        if (System.currentTimeMillis() - nextCheck >= 0 && loops < 20) {
            println("loops:" + loops);
            setNotes();
            loops++;
//            println("nextMeterStart:"+out.nextMeterStart(phrase.getPhraseMeters()));
            nextCheck = System.currentTimeMillis() + out.nextMeterStart(phrase.getPhraseMeters()) + 100;
        }
    }

    public float millisToBeats (long millis) {
        return ((float) millis / (float) beatTime);
    }

    public void start () {
//        setNotes();
        nextCheck = System.currentTimeMillis() + out.nextMeterStart(phrase.getPhraseMeters()) + 100;
//        loopEnd = System.currentTimeMillis();
//        update();
    }
}
