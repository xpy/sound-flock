package xpy.sound_flock;

import ddf.minim.ugens.Frequency;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Note
 * Created by xpy on 06-Sep-15.
 */
public class Note extends PApplet {

    public float duration;
    public float pitch;
    public String pitchName;

    Note(float pitch, float duration) {

        this.duration = duration;
        this.pitch = pitch;
    }

    Note(String pitchName, float duration) {

        this.duration = duration;
        this.pitchName = pitchName;
        this.pitch = Frequency.ofPitch(pitchName).asHz();
    }


    public static String getRandomPitch() {

        Random r = new Random();
        char c = (char) (r.nextInt(6) + 'A');

        return c + "2";
    }

    public static float getRandomDuration() {

        return getRandomDuration(1f);

    }

    public static float getRandomDuration(float maxDuration) {

        return Math.min((new Random().nextInt(7) + 1) / 4f, maxDuration);

    }

    public static Note getRandomNote(float maxDuration) {
        return new Note(Note.getRandomPitch(), Note.getRandomDuration(maxDuration));
    }

    public static List<Note> getRandomPhrase(int phraseLength, int meterLength, int numOfNotes) {

        List<Note> noteList = new ArrayList<>();

        int phraseMeters = phraseLength * meterLength;
        float currentDuration = 0;
        float biggestNoteDuration ;
        // Just in case I need to make it more "average"?
        float averageDuration = (float)phraseMeters/(float)numOfNotes;

        for (int i = 1; i < numOfNotes; i++) {
            biggestNoteDuration = Math.min(2f,phraseMeters - currentDuration - ((numOfNotes - i) * .25f));
            println("biggestNoteDuration: "+biggestNoteDuration);
            Note noteToAdd = Note.getRandomNote(biggestNoteDuration);
            currentDuration += noteToAdd.duration;
            println("Added Note Duration: "+noteToAdd.duration);
            println("currentDuration: "+currentDuration);
            println("--End of Note Creation--");
            noteList.add(noteToAdd);
        }
        biggestNoteDuration = phraseMeters - currentDuration;
        Note noteToAdd = new Note(Note.getRandomPitch(), biggestNoteDuration);
        currentDuration += noteToAdd.duration;
        println("---Adding Last Note");
        println("biggestNoteDuration: "+biggestNoteDuration);
        println("Added Note Duration: "+noteToAdd.duration);
        println("currentDuration: "+currentDuration);
        println("--End of Last Note Creation--");
        noteList.add(noteToAdd);
        return noteList;
    }
}
