package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

/**
 * ToneInstrument
 * Created by xpy on 05-Sep-15.
 */
class ToneInstrument implements Instrument {
    // create all variables that must be used througout the class
    Oscil sineOsc;
    ADSR adsr;
    public AudioOutput out;
    public float frequency;
    public float amplitude;

    // constructor for this instrument
    ToneInstrument(float frequency, float amplitude, AudioOutput out) {
        // create new instances of any UGen objects as necessary
        sineOsc = new Oscil(frequency, amplitude, Waves.TRIANGLE);
        adsr = new ADSR(0.5f, 0.01f, 0.05f, 0.5f, 0.5f);
        this.out = out;
        this.frequency = frequency;
        this.amplitude = amplitude;
        // patch everything together up to the final output
        sineOsc.patch(adsr);
    }

    ToneInstrument(ToneInstrument newInstrument) {
        sineOsc = new Oscil(newInstrument.frequency, newInstrument.amplitude, Waves.TRIANGLE);
        adsr = new ADSR(0.5f, 0.1f, 0.05f, 0.5f, 0.1f);
        this.out = newInstrument.out;
        // patch everything together up to the final output
        sineOsc.patch(adsr);
    }

    // every instrument must have a noteOn( float ) method
    public void noteOn(float dur) {
        // turn on the ADSR
        adsr.noteOn();
        // patch to the output
        adsr.patch(out);
    }

    // every instrument must have a noteOff() method
    public void noteOff() {
        // tell the ADSR to unpatch after the release is finished
        adsr.unpatchAfterRelease(out);
        // call the noteOff
        adsr.noteOff();
    }
}