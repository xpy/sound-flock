package xpy.sound_flock;

import processing.core.*;
import ddf.minim.Minim;
import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import static xpy.sound_flock.SynthInstrumentGenerator.*;


/**
 * tests
 * Created by xpy on 17-Sep-15.
 */
public class tests extends PApplet {

    Minim       minim;
    AudioOutput out;
    Blibliki    blibliki;
    Blibliki    blibliki2;
    Long        startTime;
    float amp       = .5f;
    float partial2  = 0f;
    float frequency = 100;
    Wavetable wave;
    Wavetable wave2;
//    Oscil     oscil;

    ToneInstrumentGenerator instrument;

    SynthInstrumentGenerator.Template sit = new SynthInstrumentGenerator.Template();

    public void setup () {
        size(600, 800);
        // initialize the minim and out objects
        minim = new Minim(this);
        out = minim.getLineOut(Minim.MONO, 2048);
        out.setTempo(120);
//        wave = WavetableGenerator.gen9(4096, new float[]{1, 2f}, new float[]{.5f, .5f}, new float[]{0, 0});
//       wave = WavetableGenerator.gen9(4096, new float[]{1}, new float[] { 1f }, new float[] { 0});
        instrument = new ToneInstrumentGenerator();
//        oscil = new Oscil(baseFrequency, 0.5f, wave);
        // patch the Oscil to the output
//        oscil.patch(out);
    }

    public void draw () {


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
/*

        Wavetable wave = (Wavetable) new ToneInstrumentGenerator(instrument).sineOsc.getWaveform();

        stroke(255, 0, 0);

        for (int i = 0; i < wave.size() - 1; i++) {
            // find the x position of each buffer value
            float x1 = map(i, 0, wave.size(), 0, width);
            float x2 = map(i + 1, 0, wave.size(), 0, width);
            // draw a line from one buffer position to the next for both channels
            line(x1, 500 + wave.get(i) * 50, x2, 500 + wave.get(i + 1) * 50);
        }
*/

    }

    public void keyPressed () {
        boolean waveChanged = false;
        if (key == CODED) {
            if (keyCode == UP) {
                waveChanged = true;
                amp += .05f;
            } else if (keyCode == DOWN) {
                waveChanged = true;

                amp -= .05f;
            } else if (keyCode == LEFT) {
                waveChanged = true;


                partial2 -= .5f;
            } else if (keyCode == RIGHT) {
                waveChanged = true;

                partial2 += .5f;
            }

        } else {
            if (key == 'q') {
                waveChanged = true;
                frequency += 10;
            } else if (key == 'a') {
                frequency -= 10;
                waveChanged = true;

            } else if (key == ' ') {
                return;
//                waveChanged = true;
//                out.playNote(0, 4f, new SynthInstrumentGenerator(440, .5f, out, sit));
//                out.playNote(4f, 4f, new SynthInstrumentGenerator(440 * .75f, .5f, out, sit));
//                out.playNote(8f, 4f, new SynthInstrumentGenerator(440 * .5f, .5f, out, sit));
            }

        }
/*
        if (waveChanged) {
            wave = WavetableGenerator.gen9(4096, new float[]{1, 2}, new float[]{amp, 1f - amp}, new float[]{0, partial2});
            wave2 = Waves.add(new float[]{.3f, .3f, .3f}, Waves.SQUARE, Waves.SINE, Waves.SAW);
            oscil.setWaveform(wave2);
            oscil.setFrequency(baseFrequency);

        }
*/
    }


}
