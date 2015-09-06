package xpy.sound_flock;

import ddf.minim.AudioOutput;
import processing.core.*;

/**
 * Blibliki
 * Created by xpy on 05-Sep-15.
 */
public class Blibliki extends PApplet implements BitListener {

    AudioOutput out;
    ToneInstrument instrument;

    long duration;
    int noteLength;
    public int meterLength;
    int phraseMeters;
    int phraseLength;
    long nextCheck;
    int loops = 0;
    long beatTime;

    float noteValue;

    Blibliki(ToneInstrument instrument, AudioOutput out,float noteValue) {
        this.out = out;
        this.instrument = instrument;
        noteLength = 8;
        meterLength = 4;
        phraseLength = 1;
        phraseMeters = meterLength * phraseLength;
        beatTime = (long) (60000f / 120f);
        duration = meterLength * phraseLength * beatTime;
        this.noteValue =noteValue;
        println(beatTime);
        println(duration);
        println("==========================");
    }

    public void setNotes(/*float offset*/) {
        out.pauseNotes();
        for (int i = 0; i < noteLength; i++) {
            out.playNoteAtBeat(phraseMeters, i * noteValue, 0.1f, new ToneInstrument(instrument));
        }

        out.resumeNotes();
    }

    @Override
    public void tick() {

        out.playNote(0, 0.1f, new ToneInstrument(instrument));
    }

    public void update() {

        if (System.currentTimeMillis() - nextCheck >= 0 && loops < 2) {
            println("loops:" + loops);
            loops++;
            setNotes();
            nextCheck += duration;
        }
    }

    public float millisToBeats(long millis) {
        println(millis);
        return ((float) millis / (float) beatTime);
    }

    public void start() {
//        setNotes();
        nextCheck = System.currentTimeMillis() + duration / 2;
//        loopEnd = System.currentTimeMillis();
        update();
    }
}
