package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

/**
 * ToneInstrumentGenerator
 * Created by xpy on 05-Sep-15.
 */
class ToneInstrumentGenerator implements InstrumentGenerator {


    ToneInstrumentGenerator(){

    }

    class ToneInstrument implements Instrument {

        Oscil sineOsc;
        ADSR  adsr;

        public AudioOutput out;
        public float       frequency;
        public float       amplitude;

        ToneInstrument (float frequency, float amplitude, AudioOutput out) {
            // create new instances of any UGen objects as necessary
            Wavetable wave = WavetableGenerator.gen9(4096, new float[]{1, 2}, new float[]{1, 1}, new float[]{0, 0});
            sineOsc = new Oscil(frequency, amplitude, Waves.SQUARE);
            adsr = new ADSR(0.5f, 0.01f, 0.05f, 0.5f, 0.5f);
            this.out = out;
            this.frequency = frequency;
            this.amplitude = amplitude;
            // patch everything together up to the final output
            sineOsc.patch(adsr);
        }

        public void noteOn (float dur) {
            adsr.noteOn();

            adsr.patch(out);
        }

        // every instrument must have a noteOff() method
        public void noteOff () {
            adsr.unpatchAfterRelease(out);
            adsr.noteOff();
        }

    }

    public Template createTemplate () {
        return null;
    }

    @Override
    public Instrument createInstrument (float frequency, float amplitude, AudioOutput out) {
        return new ToneInstrument(frequency, amplitude, out);
    }
}