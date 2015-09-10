package xpy.sound_flock;

import ddf.minim.AudioOutput;
import processing.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Blibliki
 * Created by xpy on 05-Sep-15.
 */
public class Blibliki extends PApplet implements BitListener {

    AudioOutput out;
    ToneInstrument instrument;

//    long duration;

    long nextCheck;
    int loops = 0;
    long beatTime = (long) (60000f / 120f);
    int offset =0;

    private Phrase phrase;

    Blibliki(AudioOutput out) {
        this.out = out;
    }

    Blibliki(int numOfNotes, int meterLength, int phraseLength, /*ToneInstrument instrument,*/ AudioOutput out) {
        this.out = out;
    }

    public static Blibliki createRandomBlibliki(AudioOutput out) {

        Blibliki ret = new Blibliki(out);
        Phrase p = new Phrase();
        p.randomizePhrase();
        ret.addPhrase(p);
        return ret;
    }

    public void addPhrase(Phrase phraseToAdd) {

        phrase = phraseToAdd;
    }

    public void setNotes(/*float offset*/) {
        out.pauseNotes();
        float i = 0;
        if(loops % 2 == 0){
            offset--;
        }

        for (Note note : phrase.notes) {
            i += note.duration;
            out.playNoteAtBeat(phrase.getPhraseMeters(), i, 0.1f, new ToneInstrument(note.pitchOffset(offset), 0.49f, out));
        }

        out.resumeNotes();
    }


    @Override
    public void tick() {

        out.playNote(0, 0.1f, new ToneInstrument(instrument));
    }

    public void update() {

        if (System.currentTimeMillis() - nextCheck >= 0 && loops < 20) {
            println("loops:" + loops);
            loops++;
            setNotes();
            nextCheck = System.currentTimeMillis() + phrase.getDuration() ;
        }
    }

    public float millisToBeats(long millis) {
        println(millis);
        return ((float) millis / (float) beatTime);
    }

    public void start() {
//        setNotes();
        nextCheck = System.currentTimeMillis();
//        loopEnd = System.currentTimeMillis();
//        update();
    }
}
