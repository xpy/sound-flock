package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

/**
 * GenericInstrument
 * Created by xpy on 20-Sep-15.
 */
public abstract class GenericInstrument implements Instrument {

    Oscil osc;
    ADSR  adsr;
    public AudioOutput out;
    public float       frequency;
    public float       amplitude;

    GenericInstrument () {
    }

    GenericInstrument (float frequency, float amplitude, AudioOutput out) {
        // create new instances of any UGen objects as necessary
        Wavetable wave = WavetableGenerator.gen9(4096, new float[]{1, 2}, new float[]{1, 1}, new float[]{0, 0});
        osc = new Oscil(frequency, amplitude,  Waves.SQUARE);
        adsr = new ADSR(0.5f, 0.01f, 0.05f, 0.5f, 0.5f);
        this.out = out;
        this.frequency = frequency;
        this.amplitude = amplitude;
        // patch everything together up to the final output
        osc.patch(adsr);
    }
/*
    GenericInstrument(ToneInstrument newInstrument) {
        Wavetable wave = Waves.SINE;

        fl = new Line(0.02f, 660f, 80f);
        Oscil ol = new Oscil(1f, 1,Waves.SINE);
        Multiplier ml = new Multiplier(660);
        osc = new Oscil(120f, newInstrument.amplitude, wave);
        ol.patch(ml);

        ml.patch(osc.frequency);
        adsr = new ADSR(1f, 0.001f, 0.1f, .7f, .5f);
        this.out = newInstrument.out;
        // patch everything together up to the final output
        osc.patch(adsr);
    }*/
}
