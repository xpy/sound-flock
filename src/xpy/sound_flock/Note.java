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

    public static float twelfthRootOfTwo = (float) Math.pow(2f, 1f / 12f);
    public static double LogOfTwelfthRootOfTwo = Math.log(twelfthRootOfTwo);

    public static ArrayList<Integer> octaveNoteDistance = initializeOctaveNoteDistance();

    Note(float pitch, float duration) {

        this.duration = duration;
        this.pitch = pitch;
    }

    Note(String pitchName, float duration) {

        this.duration = duration;
        this.pitchName = pitchName;
        this.pitch = Frequency.ofPitch(pitchName).asHz();
    }

    private static ArrayList<Integer> initializeOctaveNoteDistance() {
        ArrayList<Integer> NoteDistance = new ArrayList<>();
        NoteDistance.add(0, 2);
        NoteDistance.add(1, 2);
        NoteDistance.add(2, 1);
        NoteDistance.add(3, 2);
        NoteDistance.add(4, 2);
        NoteDistance.add(5, 2);
        NoteDistance.add(6, 1);

        return NoteDistance;
    }


    public static String getRandomPitch() {

        Random r = new Random();
        char c = (char) (r.nextInt(6) + 'A');

        return c + "4";
    }

    public static String getRandomPitch(int octave) {

        Random r = new Random();
        char c = (char) (r.nextInt(6) + 'A');

        return c + "" + octave;
    }

    /**
     * fn = f0 * (a)n
     *
     * @param pitch The Pitch
     * @return Random Pitch
     */
    public static float getRandomPitchAroundPitch(float pitch) {

        int indexOfPitch = getIndexOfPitch(pitch);
        int normalizedIndex = indexOfPitch + 9;
        Random r = new Random();
        int nextIndex = r.nextInt(6);
        int halfNoteSum = 0;
        int factor = (nextIndex<3?-1:1);
        for(int i = 3;i!=nextIndex; i+= factor){
            halfNoteSum+= octaveNoteDistance.get(i)*factor;
        }
        println(halfNoteSum);
        println(halfNoteSum);
        println(indexOfPitch);
        println(indexOfPitch+halfNoteSum);
        return getPitchOfIndex(indexOfPitch+halfNoteSum);
    }

    /**
     * indices are half tones.
     * index 0 = A4, C4 = index -9
     *
     * @param index The pitch Index
     * @return Index of Pitch
     */
    public static float getPitchOfIndex(int index) {
        return 440f * (float) Math.pow(twelfthRootOfTwo, index);
    }

    public static int getIndexOfPitch(float pitch) {
        return (int) Math.ceil(Math.log(pitch / 440) / LogOfTwelfthRootOfTwo);
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
        float biggestNoteDuration;

        // Just in case I need to make it more "average"?
        float averageDuration = (float) phraseMeters / (float) numOfNotes;

        for (int i = 1; i < numOfNotes; i++) {
            biggestNoteDuration = Math.min(2f, phraseMeters - currentDuration - ((numOfNotes - i) * .25f));

            println("biggestNoteDuration: " + biggestNoteDuration);

            Note noteToAdd = Note.getRandomNote(biggestNoteDuration);
            currentDuration += noteToAdd.duration;

            println("Added Note Duration: " + noteToAdd.duration);
            println("currentDuration: " + currentDuration);
            println("--End of Note Creation--");

            noteList.add(noteToAdd);
        }
        biggestNoteDuration = phraseMeters - currentDuration;
        Note noteToAdd = new Note(Note.getRandomPitch(), biggestNoteDuration);
        currentDuration += noteToAdd.duration;
        println("---Adding Last Note");
        println("biggestNoteDuration: " + biggestNoteDuration);
        println("Added Note Duration: " + noteToAdd.duration);
        println("currentDuration: " + currentDuration);
        println("--End of Last Note Creation--");
        noteList.add(noteToAdd);
        return noteList;
    }

    public static List<Note> getRandomPhraseAroundPitch(float pitch,int phraseLength, int meterLength, int numOfNotes) {

        List<Note> noteList = new ArrayList<>();

        int phraseMeters = phraseLength * meterLength;
        float currentDuration = 0;
        float biggestNoteDuration;

        // Just in case I need to make it more "average"?
        float averageDuration = (float) phraseMeters / (float) numOfNotes;

        for (int i = 1; i < numOfNotes; i++) {

            biggestNoteDuration = Math.min(2f, phraseMeters - currentDuration - ((numOfNotes - i) * .25f));

            println("biggestNoteDuration: " + biggestNoteDuration);

            Note noteToAdd = new Note(Note.getRandomPitchAroundPitch(pitch), Note.getRandomDuration(biggestNoteDuration) );
            currentDuration += noteToAdd.duration;

            println("Added Note Duration: " + noteToAdd.duration);
            println("currentDuration: " + currentDuration);
            println("--End of Note Creation--");

            noteList.add(noteToAdd);
        }

        biggestNoteDuration = phraseMeters - currentDuration;
        Note noteToAdd = new Note(Note.getRandomPitchAroundPitch(pitch),Note.getRandomDuration(biggestNoteDuration) );
        currentDuration += noteToAdd.duration;

        println("---Adding Last Note");
        println("biggestNoteDuration: " + biggestNoteDuration);
        println("Added Note Duration: " + noteToAdd.duration);
        println("currentDuration: " + currentDuration);
        println("--End of Last Note Creation--");

        noteList.add(noteToAdd);
        return noteList;
    }
}
