package tests;


import processing.core.*;
import ddf.minim.Minim;
import ddf.minim.AudioOutput;
import xpy.sound_flock.Blibliki;
import xpy.sound_flock.Body.Body;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Distortions.Distortion;
import xpy.sound_flock.Distortions.FullToneDistortion;
import xpy.sound_flock.Distortions.PhraseDistortionGenerator;
import xpy.sound_flock.Instruments.*;
import xpy.sound_flock.Phrase;

/**
 * PhraseTuning
 * Created by xpy on 29-Sep-15.
 */
public class PhraseTuning extends PApplet {

    Minim       minim;
    AudioOutput out;
    Blibliki    blibliki;
    Long        startTime;
    boolean tuned = false;
    Body body;
    Phrase phrase = new Phrase();

    public void setup () {
        // initialize the drawing window
        size(512, 200);

        // initialize the minim and out objects
        minim = new Minim(this);
        out = minim.getLineOut(Minim.MONO, 2048);
        out.setTempo(120);

        ToneInstrumentGenerator toneGenerator = new ToneInstrumentGenerator();

        phrase.baseNoteLength = .5f;
        phrase.meterLength = 1;
        phrase.numOfNotes = 12;
        phrase.phraseLength = 1;
        phrase.baseNotePitch = 220;
        phrase.numOfPitchPeaks = 4;
        phrase.pitchPattern = Phrase.PITCH_PATTERN_PEAKS;
        phrase.durationPattern = Phrase.DURATION_PATTERN_UNIFORM_PHRASE;
        phrase.generatePhrase();
        body = new CircleBody(this);

        blibliki = new Blibliki(phrase, toneGenerator, body, out);
//        blibliki.addDistortion(PhraseDistortionGenerator.createRandomPartialToneDistortion(phrase));
        blibliki.addDistortion(PhraseDistortionGenerator.createDistortion(0, blibliki));
//        println(blibliki.getDistortion(0));
        blibliki.start();


        startTime = System.currentTimeMillis();


    }

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


    }

    public void keyPressed () {
        if (key == 'q') {
            blibliki.applyDistortion(0);
        } else if (key == 'a') {
            blibliki.revertDistortion(0);
        }
    }

}
