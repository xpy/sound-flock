package tests;

import ddf.minim.*;
import processing.core.PApplet;
import xpy.sound_flock.Blibliki;
import xpy.sound_flock.Body.Body;
import xpy.sound_flock.Body.CircleBody;
import xpy.sound_flock.Distortions.PhraseDistortionGenerator;
import xpy.sound_flock.Instruments.ToneInstrumentGenerator;
import xpy.sound_flock.Phrase;

/**
 * Maestro
 * Created by xpy on 05-Oct-15.
 */
public class Maestro extends PApplet {

    Minim                   minim;
    AudioOutput             out;
    Blibliki                blibliki;
    Body                    body;
    AudioRecorder           recorder;
    xpy.sound_flock.Maestro maestro;
    boolean tuned = false;

    boolean record = true;

    public void setup() {
        // initialize the drawing window
        size(512, 200);

        // initialize the minim and out objects
        minim = new Minim(this);
//        minim.debugOn();
        out = minim.getLineOut(Minim.MONO, 2048);
//        recorder = minim.createRecorder(out, "E:\\maestro\\Maestro_" + System.currentTimeMillis() + ".wav");
        recorder = minim.createRecorder(out, "E:\\maestro\\Maestro_1.wav");

        maestro = new xpy.sound_flock.Maestro(this, out);
        out.setTempo(120);
        if (record)
            recorder.beginRecord();
        maestro.start();
    }

    public void draw() {
        // erase the window to black
        background(0);
        // draw using a white stroke
        stroke(255);
        // draw the waveforms
 /*       for (int i = 0; i < out.bufferSize() - 1; i++) {
            // find the x position of each buffer value
            float x1 = map(i, 0, out.bufferSize(), 0, width);
            float x2 = map(i + 1, 0, out.bufferSize(), 0, width);
            // draw a line from one buffer position to the next for both channels
            line(x1, 50 + out.left.get(i) * 50, x2, 50 + out.left.get(i + 1) * 50);
            line(x1, 150 + out.right.get(i) * 50, x2, 150 + out.right.get(i + 1) * 50);
        }
*/
        maestro.update();
        if (record && maestro.loops >= 54 && recorder.isRecording()) {
            recorder.endRecord();
            recorder.save();
        }


    }

    public void keyPressed() {
        if (key == 'q') {
            blibliki.applyDistortion(0);
        } else if (key == 'a') {
            blibliki.revertDistortion(0);
        }
    }
}
