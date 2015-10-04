package tests;

import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import ddf.minim.ugens.Wavetable;
import processing.core.PApplet;
import xpy.sound_flock.Blibliki;
import xpy.sound_flock.Body.Body;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Distortions.PhraseDistortionGenerator;
import xpy.sound_flock.Instruments.SnareInstrumentGenerator;
import xpy.sound_flock.Instruments.ToneInstrumentGenerator;
import xpy.sound_flock.Phrase;

/**
 * Piano
 * Created by xpy on 03-Oct-15.
 */
public class Piano extends PApplet {

    Minim       minim;
    AudioOutput out;
    Blibliki    blibliki;
    Body        body;
    Long        startTime;
    boolean tuned = false;

    public void setup () {
        // initialize the drawing window
        size(512, 200);

        // initialize the minim and out objects
        minim = new Minim(this);
//        minim.debugOn();
        out = minim.getLineOut(Minim.MONO, 2048);
        out.setTempo(120);

        ToneInstrumentGenerator toneGenerator = new ToneInstrumentGenerator();

        Phrase phrase = new Phrase();
        phrase.baseNoteLength = .5f;
        phrase.meterLength = 3;
        phrase.numOfNotes = 3;
        phrase.phraseLength = 2;
        phrase.baseNotePitch = 220;
        phrase.numOfPitchPeaks = 1;
        phrase.repeatNotes = 2;
        phrase.pitchPattern = Phrase.PITCH_PATTERN_ASC;
        phrase.durationPattern = Phrase.DURATION_PATTERN_UNIFORM_METER;
        phrase.generatePhrase();

        body = new CircleBody(this);

        blibliki = new Blibliki(phrase, toneGenerator, body, out);
        blibliki.addDistortion(PhraseDistortionGenerator.createRandomDistortion(PhraseDistortionGenerator.DISTORTION_TONE_FULL,phrase));
        blibliki.addBody(body);
        blibliki.start();
        blibliki.addLoopEvent(new Blibliki.LoopEvent() {
            @Override
            public void fire (int loopNum) {

            }
        });
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
