package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.util.Random;

import static processing.core.PApplet.println;


/**
 * ToneInstrumentGenerator
 * Created by xpy on 05-Sep-15.
 */
public class ToneInstrumentGenerator extends BaseInstrumentGenerator {

    private Template template;


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
        ADSR  adsr;

        MoogFilter moogFilter;


        ToneInstrument (float frequency, float amplitude, AudioOutput out) {

            this.out = out;
            this.frequency = frequency;
            this.amplitude = amplitude;

//            modulator = new Oscil(frequency * template.modulatorFactor, amplitude, wave);
            osc = new Oscil(frequency, amplitude, Waves.SQUARE);
            adsr = new ADSR(amplitude, 0.01f, 0.05f, amplitude, releaseTime);
            moogFilter = new MoogFilter(440 * template.moogFactor, .5f, MoogFilter.Type.LP);

            // patch everything together up to the final output
//            modulator.patch(sineOsc).patch(moogFilter).patch(adsr);
            (osc).patch(moogFilter).patch(adsr);
        }

        public void noteOn (float dur) {
            Line l = new Line(dur + releaseTime, 440 * template.moogFactor, 440 * template.targetMoogFactor);
            l.activate();
            l.patch(moogFilter.frequency);
            adsr.noteOn();
            patch(adsr, dur);
//            adsr.patch(out);
            isPlaying = true;
            template.moogFactor = template.targetMoogFactor;
        }

        public void noteOff () {
            adsr.unpatchAfterRelease(out);
            adsr.noteOff();
            setComplete();
        }

    }

    public static class Template extends BaseInstrumentGenerator.BaseTemplate {

        float maxDuration;
        float modulatorFactor;

        public Template () {
            Random r = new Random();

            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
            this.modulatorFactor = (r.nextInt(10) + 1) * .05f;
            this.moogFactor = r.nextFloat() + .5f;
            this.targetMoogFactor = moogFactor;
        }
    }


}