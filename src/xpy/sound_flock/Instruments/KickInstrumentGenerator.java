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
        amplitude = .7f;
        maxDuration = .025f;
        minDuration = .025f;
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
            this.amplitude = amplitudeByFrequency(amplitude,frequency);
            this.out = out;
            Wavetable w = new Wavetable(Waves.SINE);
//            w.addNoise(.0025f);
            Constant c   = new Constant(1.5f * this.frequency);
            Oscil    osc = new Oscil(frequency, this.amplitude, w);

            adsrModulator = new ADSR(1f, 0.002f, 0.015f, .1f, 0.2f);
            c.patch(adsrModulator).patch(osc.frequency);
            preFinalUgen = osc;
        }

        public void noteOn(float dur) {
            super.noteOn(dur);
            adsrModulator.noteOn();

        }

        public void noteOff() {
            super.noteOff();
            adsrModulator.noteOff();
        }

    }


    public static class Template extends BaseInstrumentGenerator.BaseTemplate {


        public Template() {
            Random r = new Random();
            fAdsrAttack = .001f;
            fAdsrDelay = .05f;
            fAdsrRelease = .3f;

//            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
        }
    }

}
