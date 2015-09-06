package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.spi.AudioOut;
import ddf.minim.ugens.Instrument;
import processing.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Blibliki
 * Created by xpy on 05-Sep-15.
 */
public class Blibliki extends PApplet implements BitListener {

    AudioOutput out;
    ToneInstrument instrument;

    long duration;

    int noteLength;
    int meterLength;
    int phraseMeters;
    int phraseLength;
    long nextCheck;
    int loops = 0;
    long beatTime;

    private List<Note> notes = new ArrayList<>();


    Blibliki(ToneInstrument instrument, AudioOutput out) {
        this.out = out;
        this.instrument = instrument;

        noteLength = 8;
        meterLength = 4;
        phraseLength = 1;
        phraseMeters = meterLength * phraseLength;
        beatTime = (long) (60000f / 120f);
        duration = meterLength * phraseLength * beatTime;
        println(beatTime);
        println(duration);
        println("==========================");
    }

    public void createNotes() {

        for (int i = 0; i < noteLength; i++) {
            Note noteToAdd = new Note(Note.getRandomNote(), .5f);
            println(noteToAdd.pitch);
            notes.add(noteToAdd);
        }
    }

    public void setNotes(/*float offset*/) {
        out.pauseNotes();
        int i = 0;
        for (Note note : notes) {
//            ToneInstrument inst = new ToneInstrument(note.pitch, 0.49f,out);

            out.playNoteAtBeat(phraseMeters, i++ * note.duration, 0.1f, new ToneInstrument(note.pitch, 0.49f,out));
        }

        /*
        for (int i = 0; i < noteLength; i++) {
            out.playNoteAtBeat(phraseMeters, i * 1, 0.1f, new ToneInstrument(instrument));
        }*/

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
