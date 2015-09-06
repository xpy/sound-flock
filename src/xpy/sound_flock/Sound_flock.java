package xpy.sound_flock;

import processing.core.*;
import ddf.minim.Minim;
import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

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
        println(out.sampleRate());
        blibliki = new Blibliki(new ToneInstrument(75f, 0.49f, out), out,1);
        blibliki2 = new Blibliki(new ToneInstrument(175f, 0.49f, out), out,.5f);

        // pause time when adding a bunch of notes at once
   /*      out.pauseNotes();

        // make four repetitions of the same pattern
       for (int i = 0; i < 4; i++) {
            // add some low notes
            out.playNote(1.25f + i * 2.0f, 0.3f,new ToneInstrument(75f , 0.49f,out));
            out.playNote(2.50f+ i * 2.0f, 0.3f , new ToneInstrument(75, 0.49f,out));

            // add some middle notes
            out.playNote(1.75f + i * 2.0f, 0.3f, new ToneInstrument(175f, 0.4f,out));
            out.playNote(2.75f + i * 2.0f, 0.3f, new ToneInstrument(175f, 0.4f,out));

            // add some high notes
            out.playNote(1.25f + i * 2.0f, 0.3f, new ToneInstrument(3750f, 0.07f,out));
            out.playNote(1.5f + i * 2.0f, 0.3f, new ToneInstrument(1750f, 0.02f,out));
            out.playNote(1.75f + i * 2.0f, 0.3f, new ToneInstrument(3750f, 0.07f,out));
            out.playNote(2.0f + i * 2.0f, 0.3f, new ToneInstrument(1750f, 0.02f,out));
            out.playNote(2.25f + i * 2.0f, 0.3f, new ToneInstrument(3750f, 0.07f,out));
            out.playNote(2.5f + i * 2.0f, 0.3f, new ToneInstrument(5550f, 0.09f,out));
            out.playNote(2.75f + i * 2.0f, 0.3f, new ToneInstrument(3750f, 0.07f,out));

        }
*/
        // resume time after a bunch of notes are added at once
        blibliki.start();
        blibliki2.start();
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
        blibliki.update();
        blibliki2.update();


    }


}


