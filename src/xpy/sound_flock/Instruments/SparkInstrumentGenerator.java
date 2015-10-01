package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.util.Random;

/**
 * KickInstrumentGenerator
 * Created by xpy on 24-Sep-15.
 */
public class SparkInstrumentGenerator implements InstrumentGenerator {

    Template template;
    public float amplitude = .3f;

    public SparkInstrumentGenerator () {
        template = createTemplate();
    }

    public Template createTemplate () {
        return new Template();
    }


    @Override
    public BaseInstrument createInstrument (float frequency, float amplitude, AudioOutput out) {
        return new SparkInstrument(frequency, amplitude, out);
    }

    @Override
    public float getAmplitude () {
        return amplitude;
    }

    @Override
    public float getMaxDuration () {
        return template.maxDuration;
    }


    public class SparkInstrument extends BaseInstrument {

        Oscil osc;
        ADSR  adsr;
        Line  l;
        Random r = new Random();

        MoogFilter moogFilter;

        public SparkInstrument (float frequency, float amplitude, AudioOutput out) {
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.out = out;

//            Wavetable wave = WavetableGenerator.gen9(4096, new float[]{1}, new float[]{1}, new float[]{0});

            moogFilter = new MoogFilter(template.wooo, .2f, MoogFilter.Type.BP);

            osc = new Oscil(frequency, amplitude, template.wavetable);
            osc.patch(moogFilter);
        }

        public void noteOn (float dur) {
            patch(moogFilter, dur);
            isPlaying = true;

        }

        // every instrumentGenerator must have a noteOff() method
        public void noteOff () {
            moogFilter.unpatch(out);
            setComplete();
        }


    }


    public static class Template implements InstrumentGenerator.Template {

        float maxDuration = .05f;
        float frequencyAmp;
        float wooo = 2000;
        Wavetable wavetable;

        public Template () {
            frequencyAmp = 1;//(r.nextInt(4)+4)*.125f;
            wavetable = Waves.randomNoise();
            wavetable.warp(1f, .1f);

//            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
        }
    }

}
