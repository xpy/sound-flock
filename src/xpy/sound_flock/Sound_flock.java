package xpy.sound_flock;

import processing.core.*;
import ddf.minim.Minim;
import ddf.minim.AudioOutput;
import xpy.sound_flock.Body.Body;
import xpy.sound_flock.Instruments.*;

/**
 * Sound Flock
 * Created by xpy on 05-Sep-15.
 */


public class Sound_flock extends PApplet {

    /* ADSRExample<br/>
   is an example of using the ADSR envelope within an instrument.
   <p>
   For more information about Minim and additional features,
   visit http://code.compartmental.net/minim/
   <p>
   author: Anderson Mills<br/>
   Anderson Mills's work was supported by numediart (www.numediart.org)
*/

// import everything necessary to make sound.

    // create all of the variables that will need to be accessed in
// more than one methods (setup(), draw(), stop()).
    Minim       minim;
    AudioOutput out;
    Blibliki    blibliki;
    Blibliki    blibliki2;
    Blibliki    blibliki3;
    Blibliki    blibliki4;
    Blibliki    blibliki5;
    Long        startTime;

    Body body;

    // Every instrument must implement the Instrument interface so
    // playNote() can call the instrument's methods.


    // setup is run once at the beginning
    public void setup () {
        // initialize the drawing window
        size(512, 200);

        // initialize the minim and out objects
        minim = new Minim(this);
        out = minim.getLineOut(Minim.MONO, 2048);
        out.setTempo(120);

        SynthInstrumentGenerator synthGenerator = new SynthInstrumentGenerator();
        KickInstrumentGenerator  kickGenerator  = new KickInstrumentGenerator();
        SparkInstrumentGenerator sparkGenerator = new SparkInstrumentGenerator();
        SnareInstrumentGenerator snareGenerator = new SnareInstrumentGenerator();
        ToneInstrumentGenerator  toneGenerator  = new ToneInstrumentGenerator();
//        blibliki = Blibliki.createRandomBlibliki( out);
//        blibliki2 = Blibliki.createRandomBlibliki(out);

        Phrase phrase1 = new Phrase();
        phrase1.meterLength = 4;
        phrase1.numOfNotes = 2;
        phrase1.phraseLength = 2;
        phrase1.pitchPattern = Phrase.PITCH_PATTERN_DESC;
        phrase1.durationPattern = Phrase.DURATION_PATTERN_UNIFORM_PHRASE;
        phrase1.baseNotePitch = 220f;
        phrase1.generatePhrase();

//        blibliki.createPhraseAroundPitch(87.31f);
        blibliki = new Blibliki(phrase1, synthGenerator,body, out);
        blibliki.start();


        Phrase phrase2 = new Phrase();
        phrase2.baseNoteLength = .5f;
        phrase2.meterLength = 4;
//        phrase2.repeatNotes = 2;
        phrase2.numOfNotes = 6;
        phrase2.phraseLength = 1;
        phrase2.baseNotePitch = 40;
        phrase2.numOfPitchPeaks = 2;
        phrase2.pitchPattern = Phrase.PITCH_PATTERN_AROUND;
        phrase2.durationPattern = Phrase.DURATION_PATTERN_RANDOM;
        phrase2.generatePhrase();


        Phrase phrase3 = new Phrase();
        phrase3.baseNoteLength = .5f;
        phrase3.meterLength = 4;
//        phrase2.repeatNotes = 2;
        phrase3.numOfNotes = 4;
        phrase3.phraseLength = 1;
        phrase3.baseNotePitch = 100;
        phrase3.numOfPitchPeaks = 2;
//        phrase3.pitchPattern = Phrase.PITCH_PATTERN_BELOW;
        phrase3.durationPattern = Phrase.DURATION_PATTERN_UNIFORM_METER;
        phrase3.generatePhrase();

        Phrase phrase4 = new Phrase();
        phrase4.baseNoteLength = .5f;
        phrase4.meterLength = 4;
        phrase4.numOfNotes = 6;
        phrase4.phraseLength = 2;
        phrase4.baseNotePitch = 80;
        phrase4.numOfPitchPeaks = 2;
        phrase4.pitchPattern = Phrase.PITCH_PATTERN_BELOW;
        phrase4.durationPattern = Phrase.DURATION_PATTERN_METER_DIVISIONS;
        phrase4.generatePhrase();

        Phrase phrase5 = new Phrase();
        phrase5.baseNoteLength = .5f;
        phrase5.meterLength = 4;
        phrase5.numOfNotes = 6;
        phrase5.phraseLength = 1;
        phrase5.baseNotePitch = 220;
        phrase5.numOfPitchPeaks = 1;
        phrase5.pitchPattern = Phrase.PITCH_PATTERN_PEAKS;
        phrase5.durationPattern = Phrase.DURATION_PATTERN_METER_DIVISIONS;
        phrase5.generatePhrase();

        blibliki2 = new Blibliki(phrase2, sparkGenerator,body, out);
        blibliki2.start();


        blibliki3 = new Blibliki(phrase3, kickGenerator,body, out);
        blibliki3.start();

        blibliki4 = new Blibliki(phrase4, snareGenerator,body, out);
        blibliki4.start();

        blibliki5 = new Blibliki(phrase5, toneGenerator,body, out);
        blibliki5.start();

        startTime = System.currentTimeMillis();

    }

    // draw is run many times
    public void draw () {
        // erase the window to black
        background(0);
        // draw using a white stroke
        stroke(255);
        // draw the waveforms
        for (int i = 0; i < out.bufferSize() - 1; i++) {
            // find the x position of each buffer value
            float x1 = map(i, 0, out.bufferSize(), 0, width);
            float x2 = map(i + 1, 0, out.bufferSize(), 0, width);
            // draw a line from one buffer position to the next for both channels
            line(x1, 50 + out.left.get(i) * 50, x2, 50 + out.left.get(i + 1) * 50);
            line(x1, 150 + out.right.get(i) * 50, x2, 150 + out.right.get(i + 1) * 50);
        }
        blibliki.update();
        if (blibliki.loops >= 2)
            blibliki2.update();
        if (blibliki.loops >= 4)
            blibliki3.update();
        if (blibliki.loops >= 5)
            blibliki4.update();
        if (blibliki.loops >= 6)
            blibliki5.update();


    }


}


