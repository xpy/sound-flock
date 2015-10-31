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

    public SparkInstrumentGenerator() {
        template = createTemplate();
        amplitude=.5f;
        maxDuration = .25f;
        minDuration = .125f;

    }

    public Template createTemplate() {
        return new Template();
    }

    @Override
    public SparkInstrumentGenerator.Template getTemplate() {
        return template;
    }

    @Override
    public BaseInstrument createInstrument(float frequency, float amplitude, AudioOutput out) {
        return new SparkInstrument(frequency, amplitude, out);
    }

    @Override
    public float getAmplitude() {
        return amplitude;
    }

    @Override
    public float getMaxDuration() {
        return maxDuration;
    }


    public class SparkInstrument extends BaseInstrument {

        Oscil osc;


        public SparkInstrument(float frequency, float amplitude, AudioOutput out) {
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.out = out;

            setMoog(new MoogFilter(template.getTargetMoog(), .5f, MoogFilter.Type.LP));
            osc = new Oscil(this.frequency, this.amplitude, template.wavetable);

            preFinalUgen = osc;
        }


    }


    public static class Template extends BaseInstrumentGenerator.BaseTemplate {

        float frequencyAmp;

        Wavetable wavetable;

        public Template() {

//            setFullAmpDelay((new Random()).nextInt(5) + 1);
            fAdsrAttack = .001f;
            fAdsrDelay = .0f;
            fAdsrRelease = .001f;
            moogFrequency = 2000;

            frequencyAmp = 1;//(r.nextInt(4)+4)*.125f;
            wavetable = Waves.randomNoise();
            wavetable.normalize();
            wavetable.warp(1f, .1f);

//            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
        }
    }

}
