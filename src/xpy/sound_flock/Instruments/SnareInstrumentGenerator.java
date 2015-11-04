package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;
import processing.core.PApplet;
import sun.security.krb5.internal.PAData;
import xpy.sound_flock.Note;

import java.util.Random;

/**
 * KickInstrumentGenerator
 * Created by xpy on 24-Sep-15.
 */
public class SnareInstrumentGenerator extends BaseInstrumentGenerator {

    Template template;


    public SnareInstrumentGenerator() {
        template = createTemplate();
        maxDuration = .075f;
        minDuration = .075f;

        minPitch = Note.getPitchOfIndex(-24);
        maxPitch = Note.getPitchOfIndex(12);

    }

    public Template createTemplate() {
        return new Template();
    }

    @Override
    public SnareInstrumentGenerator.Template getTemplate() {
        return template;
    }

    @Override
    public BaseInstrument createInstrument(float frequency, float amplitude, AudioOutput out) {
        return new SnareInstrument(frequency, amplitude, out);
    }

    @Override
    public float getMaxDuration() {
        return maxDuration;
    }

    @Override
    public float getAmplitude() {
        return amplitude;
    }


    public class SnareInstrument extends BaseInstrument {

        Oscil    osc;
        ADSR     adsrModulator;
        Constant c;


        public SnareInstrument(float frequency, float amplitude, AudioOutput out) {


            this.frequency = normalizePitch(frequency);
            this.amplitude = amplitude*.5f;

            this.out = out;

            c = new Constant(this.frequency);
            Multiplier ml = new Multiplier(this.frequency);
            osc = new Oscil(this.frequency, this.amplitude, template.wavetable);
            adsrModulator = new ADSR(1f, .0001f, .05f, .1f, .001f);
            setMoog(new MoogFilter(this.frequency * template.targetMoogFactor *4, .1f, MoogFilter.Type.LP));
//            c.patch(adsrModulator).patch(osc.frequency);
//            adsrModulator.patch(osc.amplitude);
            preFinalUgen = osc;

        }

        public void noteOn(float dur) {
            adsrModulator.noteOn();
            super.noteOn(dur);
        }

        // every instrumentGenerator must have a noteOff() method
        public void noteOff() {
            adsrModulator.noteOff();
            super.noteOff();
        }

    }


    public static class Template extends BaseInstrumentGenerator.BaseTemplate {


        public ADSR getFinalADSR(float amplitude) {
            return new ADSR(amplitude * .25f, fAdsrAttack, fAdsrDelay, amplitude, fAdsrRelease);
        }

        float     frequencyAmp;
        Wavetable wavetable;

        public Template() {
            Random r = new Random();

            fAdsrAttack = .025f;
            fAdsrDelay = .05f;
            fAdsrRelease = .0001f;

            moogFrequency = 800;

            frequencyAmp = (r.nextInt(8) + 1) * .125f;
            wavetable = new Wavetable(Waves.SAW);
            wavetable = Waves.randomNoise();
//            wavetable.warp(1f, 1f);
//            wavetable.warp(.5f, .2f);
//            wavetable.addNoise(1f);
            wavetable.normalize();
//            this.maxDuration = Math.max(r.nextFloat() / 2, .2f);
        }
    }

}
