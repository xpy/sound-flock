package xpy.sound_flock;

import processing.core.PApplet;

import java.util.Random;

/**
 * Phrases
 * Created by xpy on 04-Oct-15.
 */
public class Phrases {

    public static boolean debug = true;

    public static Phrase tonePhrase(int meterLength) {

        Integer pitchPatterns[]    = new Integer[]{1, 4, 5, 6, 7};
        Integer divisionPatterns[] = new Integer[]{0};
        Integer durationPatterns[] = new Integer[]{0, 4};
        Random  r                  = new Random();
        Phrase  phrase             = new Phrase();


        phrase.legato = true;
        phrase.meterLength = meterLength;
        phrase.phraseLength = r.nextInt(2) + 1;
        phrase.numOfNotes = Math.max(r.nextInt(meterLength) + 2, r.nextInt(phrase.phraseLength * meterLength));

        phrase.baseNotePitch = Note.getPitchOfIndex(r.nextInt(48)-24);

        phrase.baseNoteLength = (float) phrase.getPhraseMeters() / (float) phrase.numOfNotes;

        phrase.pitchPattern = pitchPatterns[r.nextInt(pitchPatterns.length)];
        if (phrase.pitchPattern == Phrase.PITCH_PATTERN_PEAKS || phrase.pitchPattern == Phrase.PITCH_PATTERN_INVERTED_PEAKS)
            phrase.numOfPitchPeaks = r.nextInt(3) + 1;

        if (phrase.numOfNotes % 2 == 0)
            phrase.durationPattern = durationPatterns[r.nextInt(durationPatterns.length)];
        else
            phrase.durationPattern = divisionPatterns[r.nextInt(divisionPatterns.length)];

        if (debug)
            PApplet.println(phrase);
        phrase.generatePhrase();
        return phrase;
    }

    public static Phrase tinyPhrase(int meterLength) {

        Integer pitchPatterns[]    = new Integer[]{1, 4, 5, 6, 7};
        Integer divisionPatterns[] = new Integer[]{0};
        Integer durationPatterns[] = new Integer[]{0, 1, 4};
        Random  r                  = new Random();
        Phrase  phrase             = new Phrase();


        phrase.meterLength = meterLength;
        phrase.phraseLength = (int) Math.pow(2, r.nextInt(3));
        phrase.numOfNotes = r.nextInt(6) + 3;
        phrase.baseNotePitch = Note.getPitchOfIndex(r.nextInt(40) - 20);

        phrase.baseNoteLength = .125f;
        phrase.positionPattern = Phrase.POSITION_END;
        phrase.pitchPattern = pitchPatterns[r.nextInt(pitchPatterns.length)];
        if (phrase.pitchPattern == Phrase.PITCH_PATTERN_PEAKS || phrase.pitchPattern == Phrase.PITCH_PATTERN_INVERTED_PEAKS)
            phrase.numOfPitchPeaks = r.nextInt(3) + 1;

        if (phrase.numOfNotes % 2 == 0)
            phrase.durationPattern = durationPatterns[r.nextInt(durationPatterns.length)];
        else
            phrase.durationPattern = divisionPatterns[r.nextInt(divisionPatterns.length)];

        if (debug)
            PApplet.println(phrase);
        phrase.generatePhrase();
        return phrase;
    }

    public static Phrase synthPhrase(int meterLength) {

        Integer pitchPatterns[]    = new Integer[]{1, 5, 6, 7};
        Integer divisionPatterns[] = new Integer[]{1};
        Integer durationPatterns[] = new Integer[]{1, 3};
        Random  r                  = new Random();
        Phrase  phrase             = new Phrase();


        phrase.meterLength = meterLength;
        phrase.phraseLength = 1;//r.nextInt(2) + 1;
        phrase.numOfNotes = 1;//(int) Math.pow(2, r.nextInt(2));
        int pitchIndex = r.nextInt(40) - 20;
        phrase.baseNotePitch = Note.getPitchOfIndex(pitchIndex);
        phrase.legato = false;
        phrase.baseNoteLength = (float) phrase.getPhraseMeters() / (float) phrase.numOfNotes;

        phrase.pitchPattern = pitchPatterns[r.nextInt(pitchPatterns.length)];
        if (phrase.pitchPattern == Phrase.PITCH_PATTERN_PEAKS || phrase.pitchPattern == Phrase.PITCH_PATTERN_INVERTED_PEAKS)
            phrase.numOfPitchPeaks = r.nextInt(3) + 1;

        if (phrase.numOfNotes % 2 == 0 && phrase.numOfNotes >= 2)
            phrase.durationPattern = durationPatterns[r.nextInt(durationPatterns.length)];
        else
            phrase.durationPattern = divisionPatterns[r.nextInt(divisionPatterns.length)];

        if (debug)
            PApplet.println(phrase);
        phrase.generatePhrase();
        return phrase;
    }

    public static Phrase kickPhrase(int meterLength) {

        Integer pitchPatterns[]    = new Integer[]{1, 5, 6, 7};
        Integer divisionPatterns[] = new Integer[]{2, 4};
        Integer durationPatterns[] = new Integer[]{1, 2};
        Random  r                  = new Random();
        Phrase  phrase             = new Phrase();


        phrase.meterLength = meterLength;
        phrase.phraseLength = 1;
        phrase.repeatNotes = r.nextInt(2) + 1;
        phrase.numOfNotes = 2;
        phrase.baseNotePitch = Note.getPitchOfIndex(((r.nextInt(24) + 1) * -1) - 12);
        phrase.legato = false;

        phrase.baseNoteLength = (float) phrase.getPhraseMeters() / (float) phrase.numOfNotes;

        phrase.pitchPattern = pitchPatterns[r.nextInt(pitchPatterns.length)];
        if (phrase.pitchPattern == Phrase.PITCH_PATTERN_PEAKS || phrase.pitchPattern == Phrase.PITCH_PATTERN_INVERTED_PEAKS)
            phrase.numOfPitchPeaks = r.nextInt(3) + 1;

        if (phrase.numOfNotes % 2 == 0 && phrase.numOfNotes >= 2)
            phrase.durationPattern = durationPatterns[r.nextInt(durationPatterns.length)];
        else
            phrase.durationPattern = divisionPatterns[r.nextInt(divisionPatterns.length)];

        if (debug)
            PApplet.println(phrase);
        phrase.generatePhrase();
        return phrase;

    }


    public static Phrase widePhrase(int meterLength) {

        Integer pitchPatterns[]    = new Integer[]{1, 4, 5, 6, 7};
        Integer divisionPatterns[] = new Integer[]{0, 2, 3, 4};
        Integer durationPatterns[] = new Integer[]{0, 4};
        Random  r                  = new Random();
        Phrase  phrase             = new Phrase();


        phrase.legato = false;
        phrase.meterLength = meterLength;
        phrase.phraseLength = r.nextInt(2) + 1;
        phrase.numOfNotes = Math.max(2, r.nextInt(meterLength * phrase.phraseLength)/2 + 1);
        phrase.baseNotePitch = Note.getPitchOfIndex(r.nextInt(48) - 24);

        phrase.baseNoteLength = (float) phrase.getPhraseMeters() / (float) phrase.numOfNotes;

        phrase.pitchPattern = pitchPatterns[r.nextInt(pitchPatterns.length)];
        if (phrase.pitchPattern == Phrase.PITCH_PATTERN_PEAKS || phrase.pitchPattern == Phrase.PITCH_PATTERN_INVERTED_PEAKS)
            phrase.numOfPitchPeaks = r.nextInt(3) + 1;

        if (phrase.numOfNotes % 2 == 0)
            phrase.durationPattern = durationPatterns[r.nextInt(durationPatterns.length)];
        else
            phrase.durationPattern = divisionPatterns[r.nextInt(divisionPatterns.length)];

        if (debug)
            PApplet.println(phrase);
        phrase.generatePhrase();
        return phrase;
    }
}
