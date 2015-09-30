package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.util.Random;

import static processing.core.PApplet.println;


/**
 * ToneInstrumentGenerator
 * Created by xpy on 05-Sep-15.
 */
public class ToneInstrumentGenerator implements InstrumentGenerator {

    Template template;
    public  float   amplitude   = .65f;

    public ToneInstrumentGenerator () {
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

        Oscil osc;
        Oscil modulator;
        ADSR  adsr;

        public AudioOutput out;
        public Sink             sink             = new Sink();
        public EnvelopeFollower envelopeFollower = new EnvelopeFollower(0, .2f, 256);

        public float frequency;
        public float amplitude;

        private boolean isComplete  = false;
        private long    completesAt = 0;

        MoogFilter moogFilter;


        ToneInstrument (float frequency, float amplitude, AudioOutput out) {

            this.out = out;
            this.frequency = frequency;
            this.amplitude = amplitude;

            Wavetable wave = WavetableGenerator.gen9(4096, new float[]{1}, new float[]{amplitude}, new float[]{1});
//            modulator = new Oscil(frequency * template.modulatorFactor, amplitude, wave);
            osc = new Oscil(frequency, amplitude, Waves.SQUARE);
            adsr = new ADSR(amplitude, 0.01f, 0.05f, amplitude, 0.5f);
            moogFilter = new MoogFilter(frequency * template.moogFactor, .5f, MoogFilter.Type.LP);

            // patch everything together up to the final output
//            modulator.patch(sineOsc).patch(moogFilter).patch(adsr);
            (osc).patch(moogFilter).patch(adsr);
        }

        public void noteOn (float dur) {
            adsr.noteOn();

            adsr.patch(envelopeFollower).patch(sink).patch(out);
            adsr.patch(out);
        }

        // every instrumentGenerator must have a noteOff() method
        public void noteOff () {
            adsr.unpatchAfterRelease(out);
            adsr.noteOff();
            isComplete = true;
            completesAt = System.currentTimeMillis() + ((long) (0.5f * 1000));
        }

        @Override
        public Sink getSink () {
            return sink;
        }

        @Override
        public EnvelopeFollower getEnvFollower () {
            return envelopeFollower;
        }

        @Override
        public boolean isComplete () {
            return isComplete && System.currentTimeMillis() > completesAt;
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
            this.moogFactor = r.nextFloat() + .5f;
        }
    }
}