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
    public float defaultDuration;

    public float pitch;
    public float defaultPitch;

    public int pitchIndex;
    public int defaultPitchIndex;

    public int indexPosition;
    public int defaultIndexPosition;

    public static float key = 440f;
    public String pitchName;

    public static float  twelfthRootOfTwo      = (float) Math.pow(2f, 1f / 12f);
    public static double LogOfTwelfthRootOfTwo = Math.log(twelfthRootOfTwo);

    public static ArrayList<Integer> octaveNoteDistance = initializeOctaveNoteDistance();

    Note(float pitch, float duration) {

        this.duration = duration;
        this.pitch = pitch;
        this.pitchIndex = getIndexOfPitch(pitch);
        this.indexPosition = getIndexPosition(pitchIndex);
        this.save();
    }

    Note(String pitchName, float duration) {

        this.duration = duration;
        this.pitchName = pitchName;
        this.pitch = Frequency.ofPitch(pitchName).asHz();
        this.pitchIndex = getIndexOfPitch(pitch);
        this.save();
    }

    Note(Note note) {
        this.duration = note.duration;
        this.pitch = note.pitch;
        this.pitchIndex = note.pitchIndex;
        this.indexPosition = note.indexPosition;
        this.save();
    }

    public static int getIndexPosition(int pitchIndex) {
        int indexPosition = (pitchIndex + 9) % 12;
        return toNormalIndex(indexPosition < 0 ? 12 + indexPosition : indexPosition);
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

    public static int toNormalIndex(int index) {

        switch (index) {
            case 0:
                return 0;
            case 2:
                return 1;
            case 4:
                return 2;
            case 5:
                return 3;
            case 7:
                return 4;
            case 9:
                return 5;
            case 11:
                return 6;

        }
        return 0;
    }


    /**
     * Save the Notes variables
     */
    public void save() {
        defaultDuration = duration;
        defaultIndexPosition = indexPosition;
        defaultPitch = pitch;
        defaultPitchIndex = pitchIndex;
    }

    /**
     * Restore the note Variables to the saved ones
     */
    public void reset() {
        duration = defaultDuration;
        indexPosition = defaultIndexPosition;
        pitch = defaultPitch;
        pitchIndex = defaultPitchIndex;

    }

    /**
     * Get a random pitch as a text e.g. A4,C2...
     *
     * @return String
     */
    public static String getRandomPitch() {

        Random r = new Random();
        char   c = (char) (r.nextInt(6) + 'A');

        return c + "4";
    }

    /**
     * Get a Random pitch into a give octave
     *
     * @param octave The octave number
     * @return String
     */
    public static String getRandomPitch(int octave) {

        Random r = new Random();
        char   c = (char) (r.nextInt(6) + 'A');

        return c + "" + octave;
    }

    /**
     * Get a random Pitch around a given pitch
     * fn = f0 * (a)n
     *
     * @param pitch The Pitch that will be used as a center pitch
     * @return Float
     */
    public static float getRandomPitchAroundPitch(float pitch) {

        int indexOfPitch = getIndexOfPitch(pitch);

        Random r           = new Random();
        int    nextIndex   = r.nextInt(6);
        int    halfNoteSum = 0;
        int    factor      = (nextIndex < 3 ? -1 : 1);
        for (int i = 3; i != nextIndex; i += factor) {
            halfNoteSum += octaveNoteDistance.get(i) * factor;
        }

        return getPitchOfIndex(indexOfPitch + halfNoteSum);
    }

    /**
     * Get a Random pitch above a given pitch
     *
     * @param pitch The pitch that will be used as a starting pitch
     * @return Float
     */
    public static float getRandomPitchAbove(float pitch) {

        return getPitchOffset(pitch, (new Random().nextInt(6) + 1));

    }

    /**
     * Get a Random pitch below a given pitch
     *
     * @param pitch The pitch that will be used as a starting pitch
     * @return Float
     */
    public static float getRandomPitchBelow(float pitch) {

        return getPitchOffset(pitch, (new Random().nextInt(6) * -1 - 1));

    }


    /**
     * indices are half tones.
     * index 0 = A4, C4 = index -9
     *
     * @param index The pitch Index
     * @return Index of Pitch
     */
    public static float getPitchOfIndex(int index) {
        return key * (float) Math.pow(twelfthRootOfTwo, index);
    }

    public static int getIndexOfPitch(float pitch) {
        return (int) Math.round(Math.log(pitch / key) / LogOfTwelfthRootOfTwo);
    }


    public static float getRandomDuration() {

        return getRandomDuration(1f);

    }

    public static float getRandomDuration(float maxDuration) {

//        return Math.min((new Random().nextInt(7) + 1) / 4f, maxDuration);

        for (int i = 6; i >0; i--) {
            if(((Math.max(0, (i - 4)) + i) * .25f)<maxDuration){
                maxDuration = (Math.max(0, (i - 4)) + i) * .25f;
                break;
            }

        }
//        return Math.min((float)Math.pow(2,new Random().nextInt(4)-1) / 4f, maxDuration);
        int x = new Random().nextInt(6) + 1;
        return Math.min((Math.max(0, (x - 4)) + x) * .5f, maxDuration);

    }

    public static Note getRandomNote(float maxDuration) {
        return new Note(Note.getRandomPitch(), Note.getRandomDuration(maxDuration));
    }

    public float pitchOffset(int offset) {
        if (offset == 0)
            return pitch;

        int factor      = (offset < 0 ? -1 : 1);
        int halfNoteSum = 0;
        int normalizedIndexPosition;

        for (int i = 0; i != offset; i += factor) {
            normalizedIndexPosition = ((indexPosition + i) % 7);

            normalizedIndexPosition = normalizedIndexPosition < 0 ? 7 + normalizedIndexPosition : normalizedIndexPosition;

            halfNoteSum += octaveNoteDistance.get(normalizedIndexPosition) /* * factor */;
        }

        return getPitchOfIndex(pitchIndex + halfNoteSum * factor);
    }

    public static float getPitchOffset(float pitch, int offset) {
        if (offset == 0)
            return pitch;
        int index         = getIndexOfPitch(pitch);
        int indexPosition = (index + 9) % 12;
        indexPosition = toNormalIndex(indexPosition < 0 ? 12 + indexPosition : indexPosition);
        int factor = (offset < 0 ? -1 : 1);

        int halfNoteSum = 0;

        int normalizedIndexPosition;
        for (int i = 0; i != offset; i += factor) {
            normalizedIndexPosition = ((indexPosition + i) % 7);

            normalizedIndexPosition = normalizedIndexPosition < 0 ? 7 + normalizedIndexPosition : normalizedIndexPosition;

            halfNoteSum += octaveNoteDistance.get(normalizedIndexPosition) /* * factor */;
        }

        return getPitchOfIndex(index + halfNoteSum * factor);
    }

    public void tune(int amount) {
        this.pitchIndex += amount;
        this.pitch = getPitchOfIndex(pitchIndex);
        this.indexPosition = getIndexPosition(pitchIndex);
    }

}
