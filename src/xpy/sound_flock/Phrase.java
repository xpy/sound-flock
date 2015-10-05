package xpy.sound_flock;

import processing.core.PApplet;
import xpy.sound_flock.Distortions.Distortion;

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
    public int meterLength  = 4;
    public int numOfNotes   = 3;
    public int repeatNotes  = 1;

    public  float baseNoteLength  = 0;
    public  float baseNotePitch   = 440f;
    private float pitchFeed       = baseNotePitch;
    public  int   numOfPitchPeaks = 1;
    public  int   pitchPeakIndex  = 0;
    public  int   pitchIndex      = 0;

    public List<Integer> pitchPeakList = new ArrayList<>();
    public List<Boolean> legatos       = new ArrayList<>();

    public int pitchPattern    = 0;
    public int durationPattern = 0;
    public int positionPattern = 0;

    public long beatTime = (long) (60000f / 120f);

    public static final int PITCH_PATTERN_RANDOM         = 0;
    public static final int PITCH_PATTERN_AROUND         = 1;
    public static final int PITCH_PATTERN_ASC            = 2;
    public static final int PITCH_PATTERN_DESC           = 3;
    public static final int PITCH_PATTERN_PEAKS          = 4;
    public static final int PITCH_PATTERN_ABOVE          = 5;
    public static final int PITCH_PATTERN_BELOW          = 6;
    public static final int PITCH_PATTERN_INVERTED_PEAKS = 7;

    public static final int DURATION_PATTERN_RANDOM          = 0;
    public static final int DURATION_PATTERN_FIXED           = 1;
    public static final int DURATION_PATTERN_UNIFORM_METER   = 2;
    public static final int DURATION_PATTERN_UNIFORM_PHRASE  = 3;
    public static final int DURATION_PATTERN_METER_DIVISIONS = 4;


    public static final int POSITION_START = 0;
    public static final int POSITION_END   = 1;


    public Phrase () {

    }

    public void generatePhrase () {
        List<Note> noteList = new ArrayList<>();
        float      noteLength;
        pitchFeed = baseNotePitch;
        if (pitchPattern == PITCH_PATTERN_PEAKS || pitchPattern == PITCH_PATTERN_INVERTED_PEAKS) {
            pitchIndex = 0;
            pitchPeakIndex = (int) Math.floor(numOfNotes / (Math.max(numOfPitchPeaks, 1)) + 1);
            println("numOfPitchPeaks: " + numOfPitchPeaks);
            println("numOfNotes: " + numOfNotes);
            println("pitchPeakIndex: " + pitchPeakIndex);

            int pitchPeakDir = 0;
            for (int i = 0; i < numOfNotes; i++) {
                if (i % pitchPeakIndex == 0) {
                    if (pitchPattern == PITCH_PATTERN_PEAKS)
                        pitchPeakDir = pitchPeakDir == PITCH_PATTERN_ASC ? PITCH_PATTERN_DESC : PITCH_PATTERN_ASC;
                    else
                        pitchPeakDir = pitchPeakDir == PITCH_PATTERN_DESC ? PITCH_PATTERN_ASC : PITCH_PATTERN_DESC;
                }
                pitchPeakList.add(pitchPeakDir);
            }
        }
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
                noteList.add(new Note(pitchFeed, (float) (meterLength / Math.pow(2.0, (double) r.nextInt(3) + 1))));

                for (int i = 1; i < numOfNotes; i++) {
                    noteList.add(new Note(getPitchByPattern(pitchFeed), (float) (meterLength / Math.pow(2.0, (double) r.nextInt(3) + 1))));
                }

                break;
            case DURATION_PATTERN_RANDOM:
                switch (pitchPattern) {
                    case PITCH_PATTERN_RANDOM:
                        noteList = getRandomPhrase(phraseLength, meterLength, numOfNotes, positionPattern);
                        break;
                    case PITCH_PATTERN_AROUND:
                        noteList = getRandomPhraseAroundPitch(pitchFeed, phraseLength, meterLength, numOfNotes, positionPattern);
                        break;
                    default:
                        int phraseMeters = phraseLength * meterLength;
                        float currentDuration = 0;
                        float biggestNoteDuration;

                        for (int i = 1; i < numOfNotes; i++) {
                            biggestNoteDuration = Math.min(2f, phraseMeters - currentDuration - ((numOfNotes - i) * .25f));
                            Note noteToAdd = new Note(getPitchByPattern(pitchFeed), Note.getRandomDuration(biggestNoteDuration));
                            currentDuration += noteToAdd.duration;
                            noteList.add(noteToAdd);
                        }
                        biggestNoteDuration = phraseMeters - currentDuration;
                        Note noteToAdd = new Note(baseNotePitch, biggestNoteDuration);
                        noteList.add(0, noteToAdd);
                        break;
                }
                break;
        }

        if (positionPattern == POSITION_END) {
            float noteLengthSum = 0;
            for (Note aNoteList : noteList) {
                noteLengthSum += aNoteList.duration;
            }
            if (noteLengthSum < getPhraseMeters()) {
                noteList.add(0, new Note(0, getPhraseMeters() - noteLengthSum));
            }
        }
        Random r = new Random();
        for (int i = 0; i < noteList.size(); i++) {
            legatos.add(r.nextBoolean());
        }
        notes = noteList;
    }

    public static float getPitchByPattern (int pattern, float pitch) {

        switch (pattern) {
            case PITCH_PATTERN_AROUND:
                return Note.getRandomPitchAroundPitch(pitch);
            case PITCH_PATTERN_ASC:
                return Note.getRandomPitchAbove(pitch);
            case PITCH_PATTERN_DESC:
                return Note.getRandomPitchBelow(pitch);
            case PITCH_PATTERN_RANDOM:
                return Note.getPitchOfIndex((new Random()).nextInt(96) - 64);
            default:
                return pitch;
        }
    }

    public float getPitchByPattern (float pitch) {

        if (pitchPattern == PITCH_PATTERN_ABOVE) {
            pitchFeed = getPitchByPattern(PITCH_PATTERN_ASC, baseNotePitch);
        } else if (pitchPattern == PITCH_PATTERN_BELOW) {
            pitchFeed = getPitchByPattern(PITCH_PATTERN_DESC, baseNotePitch);
        } else if (pitchPattern == PITCH_PATTERN_PEAKS || pitchPattern == PITCH_PATTERN_INVERTED_PEAKS) {
            pitchFeed = getPitchByPattern(pitchPeakList.get(++pitchIndex), pitch);
        } else {
            pitchFeed = getPitchByPattern(pitchPattern, pitch);
        }

        return pitchFeed;
    }

    public void randomizePhrase () {
        Random r = new Random();

        this.phraseLength = r.nextInt(3) + 1;
        this.meterLength = r.nextInt(1) + 3;

        int phraseMeters = getPhraseMeters();

        numOfNotes = r.nextInt(phraseMeters + phraseMeters / 2) + phraseMeters / 2;

    }

    public static List<Note> getRandomPhrase (int phraseLength, int meterLength, int numOfNotes, int positionPattern) {

        List<Note> noteList = new ArrayList<>();

        int   phraseMeters    = phraseLength * meterLength;
        float currentDuration = 0;
        float biggestNoteDuration;

        // Just in case I need to make it more "average"?
        float averageDuration = (float) phraseMeters / (float) numOfNotes;

        for (int i = 1; i < numOfNotes; i++) {
            biggestNoteDuration = Math.min(2f, phraseMeters - currentDuration - ((numOfNotes - i) * .25f));

            Note noteToAdd = Note.getRandomNote(biggestNoteDuration);
            currentDuration += noteToAdd.duration;

            noteList.add(noteToAdd);
        }
        biggestNoteDuration = phraseMeters - currentDuration;
        Note noteToAdd;
        if (positionPattern == POSITION_START)
            noteToAdd = new Note(Note.getRandomPitch(), biggestNoteDuration);
        else {
            noteToAdd = Note.getRandomNote(biggestNoteDuration);
            float noteLengthSum = 0;
            for (Note aNoteList : noteList) {
                noteLengthSum += aNoteList.duration;
            }
            if (noteLengthSum < phraseMeters) {
                noteList.add(0, new Note(0, phraseMeters - noteLengthSum));
            }

        }
        currentDuration += noteToAdd.duration;
        noteList.add(noteToAdd);


        return noteList;
    }

    public static List<Note> getRandomPhraseAroundPitch (float pitch, int phraseLength, int meterLength, int numOfNotes, int positionPattern) {

        List<Note> noteList = new ArrayList<>();

        int   phraseMeters    = phraseLength * meterLength;
        float currentDuration = 0;
        float biggestNoteDuration;

        // Just in case I need to make it more "average"?
        float averageDuration = (float) phraseMeters / (float) numOfNotes;

        for (int i = 1; i < numOfNotes; i++) {

            biggestNoteDuration = Math.min(2f, phraseMeters - currentDuration - ((numOfNotes - i) * .25f));


            Note noteToAdd = new Note(Note.getRandomPitchAroundPitch(pitch), Note.getRandomDuration(biggestNoteDuration));
            currentDuration += noteToAdd.duration;

            noteList.add(noteToAdd);
        }

        biggestNoteDuration = phraseMeters - currentDuration;

        Note noteToAdd = new Note(Note.getRandomPitchAroundPitch(pitch), Note.getRandomDuration(biggestNoteDuration));
        currentDuration += noteToAdd.duration;

        noteList.add(noteToAdd);
        if(positionPattern == POSITION_END){
            float noteLengthSum = 0;
            for (Note aNoteList : noteList) {
                noteLengthSum += aNoteList.duration;
            }
            if (noteLengthSum < phraseMeters) {
                noteList.add(0, new Note(0, phraseMeters - noteLengthSum));
            }

        }
        return noteList;
    }

    public int getPhraseMeters () {
        return meterLength * phraseLength;
    }

    public long getDuration () {
        return meterLength * phraseLength * beatTime;
    }

    public void tune (Integer[] noteIndexes, Integer[] tuneAmounts) {
        if (noteIndexes.length != tuneAmounts.length) {
            System.err.println("noteIndexes != tuneAmounts");
            return;

        }
        for (int i = 0; i < noteIndexes.length; i++) {
            if (noteIndexes[i] > numOfNotes) {
                System.err.println("noteIndex " + i + " (" + noteIndexes[i] + ") bigger than numOfNotes");
                return;
            }
        }


        for (int i = 0; i < tuneAmounts.length; i++) {

            notes.get(noteIndexes[i]).tune(tuneAmounts[i]);
        }

    }

    public void tune (int tuneAmount) {
        for (Note note : notes) {
            note.tune(tuneAmount);
        }

    }


    public void reset () {
        notes.forEach(xpy.sound_flock.Note::reset);
    }

    @Override
    public String toString () {
        return "Phrase{" +
               ", phraseLength=" + phraseLength +
               ", meterLength=" + meterLength +
               ", numOfNotes=" + numOfNotes +
               ", repeatNotes=" + repeatNotes +
               ", baseNoteLength=" + baseNoteLength +
               ", baseNotePitch=" + baseNotePitch +
//               ", pitchFeed=" + pitchFeed +
               ", numOfPitchPeaks=" + numOfPitchPeaks +
//               ", pitchPeakIndex=" + pitchPeakIndex +
//               ", pitchIndex=" + pitchIndex +
//               ", pitchPeakList=" + pitchPeakList +
               ", pitchPattern=" + pitchPattern +
               ", durationPattern=" + durationPattern +
//               ", beatTime=" + beatTime +
               '}';
    }
}
