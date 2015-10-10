package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;
import xpy.sound_flock.Note;

import java.util.Random;

/**
 * KickInstrumentGenerator
 * Created by xpy on 24-Sep-15.
 */
public class KickInstrumentGenerator extends BaseInstrumentGenerator {

    Template template;


    public KickInstrumentGenerator() {
        template = createTemplate();
        maxPitch = Note.getPitchOfIndex(-12);
        minPitch = Note.getPitchOfIndex(-24);

        maxDuration = .5f;
        minDuration = .125f;
    }

    public Template createTemplate() {
        return new Template();
    }

    @Override
    public KickInstrumentGenerator.Template getTemplate() {
        return template;
    }

    @Override
    public BaseInstrument createInstrument(float frequency, float amplitude, AudioOutput out) {
        return new KickInstrument(frequency, amplitude, out);
    }

    @Override
    public float getMaxDuration() {
        return maxDuration;
    }


    public class KickInstrument extends BaseInstrument {


        ADSR adsrModulator;

        public KickInstrument(float frequency, float amplitude, AudioOutput out) {
            this.frequency = normalizePitch(frequency);
            this.amplitude = .4f;
            this.out = out;
            Wavetable w = new Wavetable(Waves.SINE);
            w.addNoise(.0025f);
            Constant c   = new Constant(1.5f * this.frequency);
            Oscil    osc = new Oscil(frequency, this.amplitude, w);

            adsrModulator = new ADSR(1f, 0.001f, 0.05f, .2f, 0.3f);
            c.patch(adsrModulator).patch(osc.frequency);
            preFinalUgen = osc;
        }

        public void noteOn(float dur) {
            adsrModulator.noteOn();
            super.noteOn(dur);

        }

        public void noteOff() {
            adsrModulator.noteOff();
            super.noteOff();
        }

    }


    public static class Template extends BaseInstrumentGenerator.BaseTemplate {

        float frequencyAmp;


        public Template() {
            Random r = new Random();
            fAdsrAttack = .001f;
            fAdsrDelay = .05f;
            fAdsrRelease = .3f;

            frequencyAmp = (r.nextInt(4) + 4) * .125f;
//            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
        }
    }

}
