package tests;

import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import processing.core.PApplet;
import sun.security.krb5.internal.PAData;
import xpy.sound_flock.*;
import xpy.sound_flock.Body.Body;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Distortions.ModulatorFactorDistortion;
import xpy.sound_flock.Instruments.*;
import xpy.sound_flock.Maestro;

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

    public void setup() {
        // initialize the drawing window
        size(512, 200);

        // initialize the minim and out objects
        minim = new Minim(this);
//        minim.debugOn();
        out = minim.getLineOut(Minim.MONO, 2048);
        out.setTempo(120);

        xpy.sound_flock.Maestro a = new Maestro(this,out);

        BaseInstrumentGenerator generator = new SnareInstrumentGenerator();

        int k =0;

        for (int i = -24; i < -12; i++) {
            float freq = Note.getPitchOfIndex(i);
            InstrumentGenerator.Instrument inst = generator.createInstrument(freq, generator.getAmplitude(), out);
            println(freq);
            float duration =  generator.normalizeDuration(.025f);
            println("duration: "+duration);
            out.playNote(k+=1, duration, inst);
//            out.playNote(k+=1, 0.025f, inst);
//            out.playNoteAtBeat(4,k+=1, 0.025f, inst);
        }
    }

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


    }

}
