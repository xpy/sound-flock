package xpy.sound_flock;

import processing.core.*;
import ddf.minim.Minim;
import ddf.minim.AudioOutput;

import java.util.Random;

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
    Minim minim;
    AudioOutput out;
    Blibliki blibliki;
    Blibliki blibliki2;
    Long startTime;

    // Every instrument must implement the Instrument interface so
    // playNote() can call the instrument's methods.


    // setup is run once at the beginning
    public void setup() {
        // initialize the drawing window
        size(512, 200);

        // initialize the minim and out objects
        minim = new Minim(this);
        out = minim.getLineOut(Minim.MONO, 2048);
        out.setTempo(120);

        blibliki = new Blibliki(4, 4, 1, out);
//        blibliki = Blibliki.createRandomBlibliki( out);
        blibliki2 = new Blibliki(8, 4, 2, out);
//        blibliki2 = Blibliki.createRandomBlibliki(out);

        Phrase phrase1 = new Phrase();
        phrase1.meterLength = 3;
        phrase1.numOfNotes = 3;
        phrase1.phraseLength = 1;
        phrase1.repeatNotes = 2;
        phrase1.pitchPattern = Phrase.PITCH_PATTERN_DESC;
        phrase1.durationPattern = Phrase.DURATION_PATTERN_UNIFORM_METER;
        Random r = new Random();
        phrase1.baseNotePitch = Note.getPitchOfIndex(r.nextInt(9)*-1 -18);
        phrase1.generatePhrase();

//        blibliki.createPhraseAroundPitch(87.31f);
        blibliki.addPhrase(phrase1);
        blibliki.start();


        Phrase phrase2 = new Phrase();
        phrase2.baseNoteLength = .5f;
        phrase2.meterLength = 3;
//        phrase2.repeatNotes = 2;
        phrase2.numOfNotes = 9;
        phrase2.phraseLength = 2;
        phrase2.baseNotePitch = 349.23f;
        phrase2.numOfPitchPeaks = 2;
        phrase2.pitchPattern = Phrase.PITCH_PATTERN_AROUND;
        phrase2.durationPattern = Phrase.DURATION_PATTERN_UNIFORM_PHRASE;
        phrase2.generatePhrase();


        blibliki2.addPhrase(phrase2);
        blibliki2.start();

        startTime = System.currentTimeMillis();

    }

    // draw is run many times
    public void draw() {
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
//        blibliki.update();
       // if (System.currentTimeMillis() - startTime > 3000)
            blibliki2.update();


    }


}


