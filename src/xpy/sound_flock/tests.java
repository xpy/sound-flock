package xpy.sound_flock;

import processing.core.*;
import ddf.minim.Minim;
import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;


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
    float frequency = 440;
    Wavetable wave;
    Wavetable wave2;
//    Oscil     oscil;

    SnareInstrumentGenerator instrumentGenerator;


    public void setup () {
        size(600, 300);
        // initialize the minim and out objects
        minim = new Minim(this);
        out = minim.getLineOut(Minim.MONO, 2048);
        out.setTempo(120);

        instrumentGenerator = new SnareInstrumentGenerator();

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

        Wavetable wave = (Wavetable) new SnareInstrumentGenerator(instrument).sineOsc.getWaveform();

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


        } else {
            if (key == ' ') {
                waveChanged = true;
                out.playNote(0, .5f, instrumentGenerator.createInstrument(frequency, .5f, out));
//                out.playNote(1f, .05f, instrumentGenerator.createInstrument(frequency/2, .5f, out));
//                out.playNote(1.5f, .05f, instrumentGenerator.createInstrument(frequency/4, .5f, out));
//                instrumentGenerator.template.wooo *=1.125;
//                println(instrumentGenerator.template.wooo);
//                frequency*=.75f;
            }
            if(key == 'q'){
                frequency+=5;
            }else if(key == 'a'){
                frequency-=5;


            }

        }
        if (waveChanged) {
            waveChanged = false;
        }
    }


}
