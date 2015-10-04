package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.Random;

import static processing.core.PApplet.println;


/**
 * ToneInstrumentGenerator
 * Created by xpy on 05-Sep-15.
 */
public class ToneInstrumentGenerator extends BaseInstrumentGenerator {

    //    protected Template template;
    protected Template template;


    public ToneInstrumentGenerator () {
        template = createTemplate();
    }

    @Override
    public ToneInstrumentGenerator.Template getTemplate () {
        return template;
    }

    public Template createTemplate () {
        return new Template();
    }

    @Override
    public BaseInstrument createInstrument (float frequency, float amplitude, AudioOutput out) {
        return new ToneInstrument(frequency, amplitude, out);
    }


    @Override
    public float getMaxDuration () {
        return template.maxDuration;
    }


    class ToneInstrument extends BaseInstrument {

        Oscil osc;

        ToneInstrument (float frequency, float amplitude, AudioOutput out) {

            this.out = out;
            this.frequency = frequency;
            this.amplitude = amplitude * .5f;

            finalADSR = template.getFinalADSR(this.amplitude);

            osc = new Oscil(frequency, this.amplitude, Waves.TRIANGLE);
            setMoog(new MoogFilter(template.getTargetMoog(), .5f, MoogFilter.Type.BP));

            preFinalUgen = osc;

        }


    }

    public static class Template extends BaseInstrumentGenerator.BaseTemplate {

        float maxDuration;

        public Template () {
            Random r = new Random();
            moogFrequency = 440;
            fAdsrAttack = .01f;
            fAdsrDelay = .1f;
            fAdsrRelease = .1f;

            this.maxDuration = .5f;//Math.max(r.nextFloat() / 2, .01f);
            this.moogFactor = r.nextFloat() + .5f;
            this.targetMoogFactor = moogFactor;
        }
    }


}