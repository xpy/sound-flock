package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.util.Random;

/**
 * ToneInstrumentGenerator
 * Created by xpy on 05-Sep-15.
 */
class ToneInstrumentGenerator implements InstrumentGenerator {

    Template template;
    public float amplitude = .65f;

    ToneInstrumentGenerator () {
        template = createTemplate();
    }

    public Template createTemplate () {
        return new Template();
    }

    @Override
    public Instrument createInstrument (float frequency, float amplitude, AudioOutput out) {
        return new ToneInstrument(frequency, amplitude, out);
    }

    @Override
    public float getAmplitude () {
        return amplitude;
    }

    @Override
    public float getMaxDuration () {
        return template.maxDuration;
    }

    class ToneInstrument implements Instrument {

        Oscil sineOsc;
        Oscil modulator;
        ADSR  adsr;

        public AudioOutput out;
        public float       frequency;
        public float       amplitude;

        MoogFilter moogFilter;


        ToneInstrument (float frequency, float amplitude, AudioOutput out) {
            // create new instances of any UGen objects as necessary
            Random r = new Random();

            Wavetable wave = WavetableGenerator.gen9(4096, new float[]{1}, new float[]{amplitude}, new float[]{1});
            modulator = new Oscil(frequency * template.modulatorFactor, amplitude, wave);
            sineOsc = new Oscil(frequency, amplitude, Waves.SQUARE);
            adsr = new ADSR(amplitude, 0.01f, 0.05f, amplitude, 0.5f);
            this.out = out;
            this.frequency = frequency;
            this.amplitude = amplitude;
            moogFilter = new MoogFilter(frequency * template.moogFactor, .5f, MoogFilter.Type.LP);

            // patch everything together up to the final output
//            modulator.patch(sineOsc).patch(moogFilter).patch(adsr);
           (sineOsc).patch(moogFilter).patch(adsr);
        }

        public void noteOn (float dur) {
            adsr.noteOn();

            adsr.patch(out);
        }

        // every instrumentGenerator must have a noteOff() method
        public void noteOff () {
            adsr.unpatchAfterRelease(out);
            adsr.noteOff();
        }

    }

    public static class Template implements InstrumentGenerator.Template {

        float maxDuration;
        float modulatorFactor;
        float moogFactor;

        public Template () {
            Random r = new Random();

            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
            this.modulatorFactor = (r.nextInt(10) + 1) * .05f;
            this.moogFactor = r.nextFloat() +.25f;
        }
    }
}