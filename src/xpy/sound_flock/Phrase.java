package xpy.sound_flock;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * xpy.sound_flock.Phrase
 * Created by xpy on 09-Sep-15.
 */
public class Phrase extends PApplet {

    public List<Note> notes;

    public int phraseLength = 1;
    public int meterLength = 4;
    public int numOfNotes = 3;
    public int repeatNotes = 1;

    public float baseNoteLength = 0;
    public float baseNotePitch = 440f;
    float pitchFeed = baseNotePitch;


    public int pitchPattern = 0;
    public int durationPattern = 0;

    public long beatTime = (long) (60000f / 120f);

    public static final int PITCH_PATTERN_RANDOM = 0;
    public static final int PITCH_PATTERN_AROUND = 1;
    public static final int PITCH_PATTERN_ASC = 2;
    public static final int PITCH_PATTERN_DESC = 3;

    public static final int DURATION_PATTERN_RANDOM = 0;
    public static final int DURATION_PATTERN_FIXED = 1;
    public static final int DURATION_PATTERN_UNIFORM_METER = 2;
    public static final int DURATION_PATTERN_UNIFORM_PHRASE = 3;
    public static final int DURATION_PATTERN_METER_DIVISIONS = 4;

    Phrase() {

    }

    public void generatePhrase() {

        List<Note> noteList = new ArrayList<>();
        float noteLength;
        pitchFeed = baseNotePitch;
        switch (durationPattern) {
            case DURATION_PATTERN_FIXED:
                noteList.add(new Note(pitchFeed, baseNoteLength));
                for (int i = 1; i < numOfNotes; i++) {
                    noteList.add(new Note(getPitchByPattern(pitchFeed), baseNoteLength));
                }
                break;
            case DURATION_PATTERN_UNIFORM_METER:
                noteLength = ((float) meterLength / numOfNotes) / repeatNotes;
                noteList.add(new Note(pitchFeed, noteLength));
                for (int i = 1; i < numOfNotes; i++) {
                    noteList.add(new Note(getPitchByPattern(pitchFeed), noteLength));
                }
                for (int j = 1; j < repeatNotes; j++) {
                    for (int i = 0; i < numOfNotes; i++) {
                        noteList.add(new Note(noteList.get(i)));
                    }
                }

                break;
            case DURATION_PATTERN_UNIFORM_PHRASE:
                noteLength = (float) getPhraseMeters() / numOfNotes;
                noteList.add(new Note(pitchFeed, noteLength));

                for (int i = 1; i < numOfNotes; i++) {
                    noteList.add(new Note(getPitchByPattern(pitchFeed), noteLength));
                }

                break;
            case DURATION_PATTERN_METER_DIVISIONS:
                Random r = new Random();
                for (int i = 0; i < numOfNotes; i++) {
                    noteList.add(new Note(getPitchByPattern(pitchFeed), (float) (meterLength / Math.pow(2.0, (double) r.nextInt(3) + 1))));
                }

                break;
            case DURATION_PATTERN_RANDOM:
                switch (pitchPattern) {
                    case PITCH_PATTERN_RANDOM:
                        noteList = getRandomPhrase(phraseLength, meterLength, numOfNotes);
                        break;
                    case PITCH_PATTERN_AROUND:
                        noteList = getRandomPhraseAroundPitch(pitchFeed, phraseLength, meterLength, numOfNotes);
                        break;
                }
                break;
        }

/*
        for (Note n : noteList)
            println("Note: " + n.pitch);
        println("============");
*/
        notes = noteList;
    }

    public static float getPitchByPattern(int pattern, float pitch) {

        switch (pattern) {
            case PITCH_PATTERN_AROUND:
                return Note.getRandomPitchAroundPitch(pitch);
            case PITCH_PATTERN_ASC:
                return Note.getRandomPitchAbove(pitch);
            case PITCH_PATTERN_DESC:
                return Note.getRandomPitchBelow(pitch);
            default:
                return Note.getPitchOfIndex((new Random()).nextInt(20));
        }
    }

    public float getPitchByPattern(float pitch) {

        pitchFeed = getPitchByPattern(pitchPattern, pitch);

        return pitchFeed;
    }

    public void randomizePhrase() {
        Random r = new Random();

        this.phraseLength = r.nextInt(3) + 1;
        this.meterLength = r.nextInt(1) + 3;

        int phraseMeters = getPhraseMeters();

        numOfNotes = r.nextInt(phraseMeters + phraseMeters / 2) + phraseMeters / 2;

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

    public static List<Note> getRandomPhraseAroundPitch(float pitch, int phraseLength, int meterLength, int numOfNotes) {

        List<Note> noteList = new ArrayList<>();

        int phraseMeters = phraseLength * meterLength;
        float currentDuration = 0;
        float biggestNoteDuration;

        // Just in case I need to make it more "average"?
        float averageDuration = (float) phraseMeters / (float) numOfNotes;

        for (int i = 1; i < numOfNotes; i++) {

            biggestNoteDuration = Math.min(2f, phraseMeters - currentDuration - ((numOfNotes - i) * .25f));

            println("biggestNoteDuration: " + biggestNoteDuration);

            Note noteToAdd = new Note(Note.getRandomPitchAroundPitch(pitch), Note.getRandomDuration(biggestNoteDuration));
            currentDuration += noteToAdd.duration;

            println("Added Note Duration: " + noteToAdd.duration);
            println("currentDuration: " + currentDuration);
            println("--End of Note Creation--");

            noteList.add(noteToAdd);
        }

        biggestNoteDuration = phraseMeters - currentDuration;
        Note noteToAdd = new Note(Note.getRandomPitchAroundPitch(pitch), Note.getRandomDuration(biggestNoteDuration));
        currentDuration += noteToAdd.duration;

        println("---Adding Last Note");
        println("biggestNoteDuration: " + biggestNoteDuration);
        println("Added Note Duration: " + noteToAdd.duration);
        println("currentDuration: " + currentDuration);
        println("--End of Last Note Creation--");

        noteList.add(noteToAdd);
        return noteList;
    }

    public int getPhraseMeters() {
        return meterLength * phraseLength;
    }

    public long getDuration() {
        return meterLength * phraseLength * beatTime;
    }

}
