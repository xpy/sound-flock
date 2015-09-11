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

    public Note(Note note) {
        this.duration = note.duration;
        this.pitch = note.pitch;

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

        Random r = new Random();
        int nextIndex = r.nextInt(6);
        int halfNoteSum = 0;
        int factor = (nextIndex < 3 ? -1 : 1);
        for (int i = 3; i != nextIndex; i += factor) {
            halfNoteSum += octaveNoteDistance.get(i) * factor;
        }
        println(halfNoteSum);
        println(halfNoteSum);
        println(indexOfPitch);
        println(indexOfPitch + halfNoteSum);
        return getPitchOfIndex(indexOfPitch + halfNoteSum);
    }

    public static float getRandomPitchAbove(float pitch) {

        return getPitchOffset(pitch, (new Random().nextInt(6) + 1));

    }

    public static float getRandomPitchBelow(float pitch) {

        return getPitchOffset(pitch, (new Random().nextInt(6)*-1 - 1));

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
        return (int) Math.round(Math.log(pitch / 440) / LogOfTwelfthRootOfTwo);
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

    public float pitchOffset(int offset) {
        return getPitchOffset(pitch, offset);
    }

    public static float getPitchOffset(float pitch, int offset) {
        if (offset == 0)
            return pitch;
        int index = getIndexOfPitch(pitch);
        int indexPosition = (index + 9) % 12;
//        println("index: " + index);
//        println("index+9: " + (index + 9));
//        println("indexPosition: " + indexPosition);
//        println("offset: " + offset);
        indexPosition = toNormalIndex(indexPosition < 0 ? 12 + indexPosition : indexPosition);
//        println("normalIndexPosition: " + indexPosition);
        int factor = (offset < 0 ? -1 : 1);

        int halfNoteSum = 0;

        int normalizedIndexPosition;
        for (int i = 0; i != offset; i += factor) {
            normalizedIndexPosition = ((indexPosition + i) % 7);

            normalizedIndexPosition = normalizedIndexPosition < 0 ? 7 + normalizedIndexPosition : normalizedIndexPosition;

            halfNoteSum += octaveNoteDistance.get(normalizedIndexPosition) /* * factor */;
        }
//        println("halfNoteSum: " + halfNoteSum);


        return getPitchOfIndex(index + halfNoteSum * factor);
    }
}
