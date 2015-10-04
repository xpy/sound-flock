package xpy.sound_flock;

import processing.core.PApplet;

import java.util.Random;

/**
 * Created by xpy on 04-Oct-15.
 */
public class Phrases {


    public static Phrase tonePhrase (int meterLength) {

        Integer pitchPatterns[]    = new Integer[]{1, 4, 5, 6};
        Integer divisionPatterns[] = new Integer[]{0};
        Integer durationPatterns[] = new Integer[]{0, 4};
        Random  r                  = new Random();
        Phrase  phrase             = new Phrase();


        phrase.meterLength = meterLength;
        phrase.phraseLength = r.nextInt(2) + 1;
        phrase.numOfNotes = meterLength * phrase.phraseLength + r.nextInt(phrase.phraseLength * 4 + 1);
        phrase.baseNotePitch = Note.getPitchOfIndex(r.nextInt(57) - 32);

        phrase.baseNoteLength = (float) phrase.getPhraseMeters() / (float) phrase.numOfNotes;

        phrase.pitchPattern = pitchPatterns[r.nextInt(pitchPatterns.length)];
        if (phrase.pitchPattern == Phrase.PITCH_PATTERN_PEAKS)
            phrase.numOfPitchPeaks = r.nextInt(3) + 1;

        if (phrase.numOfNotes % 2 == 0)
            phrase.durationPattern = durationPatterns[r.nextInt(durationPatterns.length)];
        else
            phrase.durationPattern = divisionPatterns[r.nextInt(divisionPatterns.length)];

        phrase.generatePhrase();
        PApplet.println(phrase);
        return phrase;
    }

    public static Phrase synthPhrase (int meterLength) {

        Integer pitchPatterns[]    = new Integer[]{1, 5, 6};
        Integer divisionPatterns[] = new Integer[]{1};
        Integer durationPatterns[] = new Integer[]{1, 3};
        Random  r                  = new Random();
        Phrase  phrase             = new Phrase();


        phrase.meterLength = meterLength;
        phrase.phraseLength = r.nextInt(3) + 1;
        phrase.numOfNotes = (int) Math.pow(2, r.nextInt(2));
        int pitchIndex = r.nextInt(57) - 32;
        PApplet.println("pitchIndex: "+pitchIndex);
        phrase.baseNotePitch = Note.getPitchOfIndex(pitchIndex);

        phrase.baseNoteLength = (float) phrase.getPhraseMeters() / (float) phrase.numOfNotes;

        phrase.pitchPattern = pitchPatterns[r.nextInt(pitchPatterns.length)];
        if (phrase.pitchPattern == Phrase.PITCH_PATTERN_PEAKS)
            phrase.numOfPitchPeaks = r.nextInt(3) + 1;

        if (phrase.numOfNotes % 2 == 0 && phrase.numOfNotes >= 2)
            phrase.durationPattern = durationPatterns[r.nextInt(durationPatterns.length)];
        else
            phrase.durationPattern = divisionPatterns[r.nextInt(divisionPatterns.length)];

        phrase.generatePhrase();
        PApplet.println(phrase);
        return phrase;
    }

    public static Phrase kickPhrase (int meterLength) {

        Integer pitchPatterns[]    = new Integer[]{1, 2, 3, 5, 6};
        Integer divisionPatterns[] = new Integer[]{2, 4};
        Integer durationPatterns[] = new Integer[]{1, 4};
        Random  r                  = new Random();
        Phrase  phrase             = new Phrase();


        phrase.meterLength = meterLength == 4 ? (int) Math.pow(2, r.nextInt(3)) : meterLength;
        phrase.phraseLength = 1;
        phrase.numOfNotes = (int) Math.pow(2, r.nextInt(3));
        phrase.baseNotePitch = Note.getPitchOfIndex(((r.nextInt(32) + 1) * -1) - 16);

        phrase.baseNoteLength = (float) phrase.getPhraseMeters() / (float) phrase.numOfNotes;

        phrase.pitchPattern = pitchPatterns[r.nextInt(pitchPatterns.length)];
        if (phrase.pitchPattern == Phrase.PITCH_PATTERN_PEAKS)
            phrase.numOfPitchPeaks = r.nextInt(3) + 1;

        if (phrase.numOfNotes % 2 == 0 && phrase.numOfNotes >= 2)
            phrase.durationPattern = durationPatterns[r.nextInt(durationPatterns.length)];
        else
            phrase.durationPattern = divisionPatterns[r.nextInt(divisionPatterns.length)];

        phrase.generatePhrase();
        PApplet.println(phrase);
        return phrase;

    }
}
