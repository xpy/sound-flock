package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.util.Random;

/**
 * KickInstrumentGenerator
 * Created by xpy on 24-Sep-15.
 */
public class KickInstrumentGenerator extends BaseInstrumentGenerator {

    Template template;
    public float amplitude = .65f;

    public KickInstrumentGenerator () {
        template = createTemplate();
    }

    public Template createTemplate () {
        return new Template();
    }

    @Override
    public KickInstrumentGenerator.Template getTemplate () {
        return template;
    }

    @Override
    public BaseInstrument createInstrument (float frequency, float amplitude, AudioOutput out) {
        return new KickInstrument(frequency, amplitude, out);
    }

    @Override
    public float getMaxDuration () {
        return template.maxDuration;
    }


    public class KickInstrument extends BaseInstrument {

        Oscil    osc;
        ADSR     adsr;
        ADSR     adsrModulator;
        Constant c;

        public KickInstrument (float frequency, float amplitude, AudioOutput out) {
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.releaseTime = .3f;
            this.out = out;

            Wavetable wave = WavetableGenerator.gen9(4096, new float[]{1}, new float[]{1}, new float[]{0});

            c = new Constant(2 * frequency * template.frequencyAmp);

            osc = new Oscil(frequency * template.frequencyAmp, amplitude, wave);
            adsr = new ADSR(amplitude, 0.001f, 0.05f, amplitude, releaseTime);
            adsrModulator = new ADSR(1f, 0.001f, 0.05f, .2f, 0.3f);
            c.patch(adsrModulator).patch(osc.frequency);
            osc.patch(adsr);
        }

        public void noteOn (float dur) {
            adsrModulator.noteOn();
            adsr.noteOn();
            patch(adsr, dur);
            isPlaying = true;
        }

        public void noteOff () {
            adsr.unpatchAfterRelease(out);
            adsrModulator.noteOff();
            adsr.noteOff();

            setComplete();
        }

    }


    public static class Template extends BaseInstrumentGenerator.BaseTemplate {

        float maxDuration = .5f;
        float frequencyAmp;

        public Template () {
            Random r = new Random();
            frequencyAmp = (r.nextInt(4) + 4) * .125f;
//            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
        }
    }

}
