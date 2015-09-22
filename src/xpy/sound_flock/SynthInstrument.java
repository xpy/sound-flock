package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;


/**
 * SynthInstrument
 * Created by xpy on 20-Sep-15.
 */


public class SynthInstrument extends GenericInstrument {
    Oscil      osc2;
    Oscil      osc3;
    Multiplier ml;
    Multiplier ml2;
    MoogFilter moog;
    ADSR       adsr2;
    Line       l;

    Summer s = new Summer();

    SynthInstrument (float frequency, float amplitude, AudioOutput out) {

        ml = new Multiplier(10*frequency);
        ml2 = new Multiplier( frequency/2);
        Wavetable a = WavetableGenerator.gen9(4086,new float[]{1},new float[]{1},new float[]{1});
        a.offset(1f);
        a.normalize();
        a.offset(.5f);

        osc = new Oscil( frequency*0.5f, amplitude,Waves.SAW );
        osc2 = new Oscil( frequency, amplitude, Waves.SQUARE);
        osc3 = new Oscil( .2f, amplitude, a);

        adsr = new ADSR(0.7f, 0.01f, .01f, 0.5f, 0.5f);
        adsr2 = new ADSR(0.7f, 0.01f, .01f, 0.5f, 0.5f);
        l = new Line(1,20*frequency,frequency);
        this.out = out;
        this.frequency = frequency;
        this.amplitude = amplitude;
        // patch everything together up to the final output
//        l.patch(ml);
//        ml.patch(osc.frequency);
        moog    = new MoogFilter( 2*frequency, .5f,MoogFilter.Type.LP );
        osc.patch(s);
        osc2.patch(s);
        osc3.patch(s);
//        ml.patch(osc.frequency);
        osc3.patch(ml).patch(moog.frequency);
        s.patch(moog).patch(adsr);

    }

    @Override
    public void noteOn (float v) {
        l.activate();
//        adsr.patch(ml2).patch(moog.frequency);
        adsr.noteOn();
        adsr2.noteOn();
        // patch to the output
        adsr.patch(out);
    }

    @Override
    public void noteOff () {
        adsr.unpatchAfterRelease(out);
        // call the noteOff
        adsr.noteOff();
        adsr2.noteOff();

    }
}
