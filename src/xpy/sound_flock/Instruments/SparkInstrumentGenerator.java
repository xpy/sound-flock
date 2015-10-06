package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.util.Random;

/**
 * KickInstrumentGenerator
 * Created by xpy on 24-Sep-15.
 */
public class SparkInstrumentGenerator extends BaseInstrumentGenerator {

    Template template;
    public float amplitude = .5f;

    public SparkInstrumentGenerator () {
        template = createTemplate();
    }

    public Template createTemplate () {
        return new Template();
    }

    @Override
    public SparkInstrumentGenerator.Template getTemplate () {
        return template;
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


        public SparkInstrument (float frequency, float amplitude, AudioOutput out) {
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.out = out;

            setMoog(new MoogFilter(template.getTargetMoog(), .2f, MoogFilter.Type.BP));
            osc = new Oscil(frequency, amplitude, template.wavetable);

            preFinalUgen = osc;
        }


    }


    public static class Template extends BaseInstrumentGenerator.BaseTemplate {

        float maxDuration = .05f;
        float frequencyAmp;

        Wavetable wavetable;

        public Template () {


            fAdsrAttack = .001f;
            fAdsrDelay = .0f;
            fAdsrRelease = .001f;
            moogFrequency = 2000;

            frequencyAmp = 1;//(r.nextInt(4)+4)*.125f;
            wavetable = Waves.randomNoise();
            wavetable.warp(1f, .1f);

//            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
        }
    }

}
