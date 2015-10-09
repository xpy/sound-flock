package tests;

import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import processing.core.PApplet;
import sun.security.krb5.internal.PAData;
import xpy.sound_flock.Blibliki;
import xpy.sound_flock.Body.Body;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Distortions.ModulatorFactorDistortion;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Instruments.SnareInstrumentGenerator;
import xpy.sound_flock.Instruments.ToneInstrumentGenerator;
import xpy.sound_flock.Note;
import xpy.sound_flock.Phrase;

/**
 * InstrumentCheck
 * Created by xpy on 07-Oct-15.
 */
public class InstrumentCheck extends PApplet {

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

        SnareInstrumentGenerator generator = new SnareInstrumentGenerator();

        for (int i = -20; i < 20; i++) {
            float freq = Note.getPitchOfIndex(i);
            InstrumentGenerator.Instrument inst =generator.createInstrument(freq,1,out);
            println(freq);
            out.playNote(i,.125f,inst);

        }
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



    }

}
