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

    long duration;

    int numOfNotes;
    int meterLength;
    int phraseMeters;
    int phraseLength;
    long nextCheck;
    int loops = 0;
    long beatTime;

    private List<Note> notes = new ArrayList<>();


    Blibliki( AudioOutput out) {
        this.out = out;
//        this.instrument = instrument;

        numOfNotes = 20;
        meterLength = 4;
        phraseLength = 2;
        phraseMeters = meterLength * phraseLength;
        beatTime = (long) (60000f / 120f);
        duration = meterLength * phraseLength * beatTime;
        println(beatTime);
        println(duration);
        println("==========================");
    }

    Blibliki(int numOfNotes, int meterLength, int phraseLength, /*ToneInstrument instrument,*/ AudioOutput out) {
        this.out = out;
//        this.instrument = instrument;

        this.numOfNotes = numOfNotes;
        this.meterLength = meterLength;
        this.phraseLength = phraseLength;

        phraseMeters = meterLength * phraseLength;
        beatTime = (long) (60000f / 120f);
        duration = meterLength * phraseLength * beatTime;
/*
        println(beatTime);
        println(duration);
        println("==========================");
*/
    }

    public static Blibliki createRandomBlibliki(/*ToneInstrument instrument,*/ AudioOutput out){
        Random r = new Random();
        int phraseLength = r.nextInt(3)+1;
        int meterLength = r.nextInt(1)+3;
        int phraseMeters = meterLength * phraseLength;

        int numOfNotes = r.nextInt(phraseMeters+phraseMeters/2)+phraseMeters/2;

        return new Blibliki(numOfNotes,meterLength,phraseLength,out);
    }

    public void createPhrase() {

        notes = Note.getRandomPhrase(phraseLength, meterLength, numOfNotes);

    }
    public void createPhraseAroundPitch(float pitch) {

        notes = Note.getRandomPhraseAroundPitch(pitch,phraseLength, meterLength, numOfNotes);

    }

    public void setNotes(/*float offset*/) {
        out.pauseNotes();
        float i = 0;
        for (Note note : notes) {
//            ToneInstrument inst = new ToneInstrument(note.pitch, 0.49f,out);
            i += note.duration;
            out.playNoteAtBeat(phraseMeters, i, 0.1f, new ToneInstrument(note.pitch, 0.49f, out));
        }

        /*
        for (int i = 0; i < numOfNotes; i++) {
            out.playNoteAtBeat(phraseMeters, i * 1, 0.1f, new ToneInstrument(instrument));
        }*/

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
            nextCheck = System.currentTimeMillis()+ duration/2;
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
//        update();
    }
}
