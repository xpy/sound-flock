package tests;

import Constellation.*;
import ddf.minim.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGraphics3D;
import xpy.sound_flock.Blibliki;
import xpy.sound_flock.BliblikiRuler;
import xpy.sound_flock.Body.Body;
import xpy.sound_flock.Body.ConstellationMember;
import xpy.sound_flock.Body.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Maestro
 * Created by xpy on 05-Oct-15.
 */
public class Maestro extends PApplet {

    Minim                   minim;
    AudioOutput             out;
    Blibliki                blibliki;
    AudioRecorder           recorder;
    xpy.sound_flock.Maestro maestro;

    boolean record = true;
    PGraphics currentLayer;

    public static int numOfLayers = 1;
    public        int layerDelay  = 0;
    List<PGraphics> layers = new ArrayList<>();

    public static void main(String args[]) {
        // full-screen mode can be activated via parameters to PApplets main method.
        PApplet.main(new String[] {"tests.Maestro"});
    }
    public void setup() {
        // initialize the drawing window
        size(800, 600, P3D);

        // initialize the minim and out objects
        minim = new Minim(this);
//        minim.debugOn();
        out = minim.getLineOut(Minim.MONO, 2048);
        out.setTempo(120);
        println("out.getVolume(): " + out.getVolume());
//        recorder = minim.createRecorder(out, "E:\\maestro\\Maestro_" + System.currentTimeMillis() + ".wav");
        recorder = minim.createRecorder(out, "E:\\maestro\\Maestro_1.wav");

        maestro = new xpy.sound_flock.Maestro(this, out);
        if (record)
            recorder.beginRecord();
        maestro.start();
        Constellation.offset = 50;
        Constellation.offsetStep = 50;
        background(0);
        currentLayer = createGraphics(width, height, P3D);

    }


    public void draw() {

        // erase the window to black
/*
        fill(0, 0, 0, 10);
        rect(0, 0, width, height);
*/
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
        background(0);

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
