package xpy.sound_flock;

import com.sun.org.apache.xpath.internal.operations.Mult;
import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

/**
 * ToneInstrument
 * Created by xpy on 05-Sep-15.
 */
class ToneInstrument implements Instrument {
    // create all variables that must be used throughout the class
    Oscil sineOsc;
    ADSR adsr;
    public AudioOutput out;
    public float frequency;
    public float amplitude;

    Line fl;
    // constructor for this instrument
    ToneInstrument(float frequency, float amplitude, AudioOutput out) {
        // create new instances of any UGen objects as necessary
        Wavetable wave = WavetableGenerator.gen9(4096, new float[]{1, 2}, new float[] { 1, 1 }, new float[] { 0, 0 });
        sineOsc = new Oscil(frequency, amplitude,  Waves.SQUARE);
        adsr = new ADSR(0.5f, 0.01f, 0.05f, 0.5f, 0.5f);
        this.out = out;
        this.frequency = frequency;
        this.amplitude = amplitude;
        // patch everything together up to the final output
        sineOsc.patch(adsr);
    }

    ToneInstrument(ToneInstrument newInstrument) {
        Wavetable wave = Waves.SINE;

        fl = new Line(0.02f, 660f, 80f);
        Oscil ol = new Oscil(1f, 1,Waves.SINE);
        Multiplier ml = new Multiplier(660);
        sineOsc = new Oscil(120f, newInstrument.amplitude, wave);
        ol.patch(ml);

        ml.patch(sineOsc.frequency);
        adsr = new ADSR(1f, 0.001f, 0.1f, .7f, .5f);
        this.out = newInstrument.out;
        // patch everything together up to the final output
        sineOsc.patch(adsr);
    }

    public void setWave(Wavetable wave){
        sineOsc.setWaveform(wave);

    }

    // every instrument must have a noteOn( float ) method
    public void noteOn(float dur) {
        // turn on the ADSR
        fl.activate();
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