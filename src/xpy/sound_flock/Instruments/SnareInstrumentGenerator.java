package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.util.Random;

/**
 * KickInstrumentGenerator
 * Created by xpy on 24-Sep-15.
 */
public class SnareInstrumentGenerator implements InstrumentGenerator {

    Template template;
    public float amplitude = .65f;

    public SnareInstrumentGenerator () {
        template = createTemplate();
    }

    public Template createTemplate () {
        return new Template();
    }


    @Override
    public BaseInstrument createInstrument (float frequency, float amplitude, AudioOutput out) {
        return new SnareInstrument(frequency, amplitude, out);
    }

    @Override
    public float getAmplitude () {
        return amplitude;
    }

    @Override
    public float getMaxDuration () {
        return template.maxDuration;
    }


    public class SnareInstrument extends BaseInstrument {

        Oscil      osc;
        Oscil      modulator;
        ADSR       adsr;
        ADSR       adsrModulator;
        Line       l;
        MoogFilter moogFilter;

        public SnareInstrument (float frequency, float amplitude, AudioOutput out) {
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.releaseTime = .2f;
            this.out = out;

            l = new Line(frequency * template.frequencyAmp, frequency);

//            Wavetable wave = WavetableGenerator.gen9(4096, new float[]{1}, new float[]{amplitude}, new float[]{1});
            osc = new Oscil(frequency * template.frequencyAmp, amplitude, template.wavetable);
//            Oscil osc2 = new Oscil(frequency,2*amplitude,wave);
            adsr = new ADSR(amplitude * 2, 0.0001f, 0.05f, 0, releaseTime);
            adsrModulator = new ADSR(.1f, 0.1f, 0.5f, 1f, 0.1f);
            moogFilter = new MoogFilter(frequency * 5, .1f, MoogFilter.Type.BP);
            l.patch(adsrModulator).patch(osc.frequency);
//            osc2.patch(osc).patch(adsr);
//            (osc).patch(adsr);
            (osc).patch(moogFilter).patch(adsr);
        }

        public void noteOn (float dur) {
            l.activate();
            adsrModulator.noteOn();
            adsr.noteOn();
            isPlaying = true;

            patch(adsr, dur);
        }

        // every instrumentGenerator must have a noteOff() method
        public void noteOff () {
            adsr.unpatchAfterRelease(out);

            adsrModulator.noteOff();
            adsr.noteOff();
            setComplete();

        }

    }


    public static class Template implements InstrumentGenerator.Template {

        float maxDuration = .2f;
        float     frequencyAmp;
        Wavetable wavetable;

        public Template () {
            Random r = new Random();
            frequencyAmp = (r.nextInt(8) + 1) * .125f;
            wavetable = Waves.randomNoise();
            wavetable.warp(.5f, .2f);
//            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
        }
    }

}
