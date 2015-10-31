package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.UGen;
import ddf.minim.ugens.*;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.Random;

import static processing.core.PApplet.pow;
import static processing.core.PApplet.println;


/**
 * ToneInstrumentGenerator
 * Created by xpy on 05-Sep-15.
 */
public class ToneInstrumentGenerator extends BaseInstrumentGenerator {

    //    protected Template template;

    protected Template template;

    public ToneInstrumentGenerator() {

        amplitude = .5f;
        template = createTemplate();
        maxDuration = .25f;
        minDuration = .125f;
    }

    @Override
    public ToneInstrumentGenerator.Template getTemplate() {
        return template;
    }

    public Template createTemplate() {
        return new Template();
    }

    @Override
    public BaseInstrument createInstrument(float frequency, float amplitude, AudioOutput out) {
        return new ToneInstrument(frequency, amplitude, out);
    }


    @Override
    public float getMaxDuration() {
        return maxDuration;
    }


    class ToneInstrument extends BaseInstrument {

        Oscil osc;

        ToneInstrument(float frequency, float amplitude, AudioOutput out) {

            this.out = out;
            this.frequency = frequency;
            this.amplitude = amplitude;
            if (template.modulatorFrequencyAmp > 32)
                this.amplitude = amplitude * .5f;

            finalADSR = template.getFinalADSR(this.amplitude);
            Wavetable moogModulatorWavetable = WavetableGenerator.gen9(4086, new float[]{1}, new float[]{1}, new float[]{1});
//            Wavetable moogModulatorWavetable =new Wavetable(Waves.SQUARE);
            moogModulatorWavetable.offset(1f);
            moogModulatorWavetable.normalize();
            moogModulatorWavetable.offset(.5f);
            moogModulatorWavetable.normalize();

            osc = new Oscil(this.frequency, this.amplitude / 2, getWaveTable(0));
            Oscil      osc2 = new Oscil(this.frequency * template.modulatorFrequencyAmp, this.amplitude / 2, getWaveTable(template.modulatorWaveIndex));
            Multiplier ml   = new Multiplier(this.frequency);
            Multiplier ml2  = new Multiplier(this.frequency * template.modulatorFrequencyAmp);

            (new Oscil(template.frequencyModulatorFrequency, 1, moogModulatorWavetable)).patch(ml).patch(osc.frequency);
            (new Oscil(template.frequencyModulatorFrequency, 1, moogModulatorWavetable)).patch(ml2).patch(osc2.frequency);

            osc2.patch(osc);
            setMoog(new MoogFilter(template.moogFrequency * template.targetMoogFactor, .5f, MoogFilter.Type.LP));
            Delay del = new Delay(0.075f, .75f, true, true);
            osc.patch(del);
            preFinalUgen = del;

        }


    }

    public static class Template extends BaseInstrumentGenerator.BaseTemplate {

        int   waveIndex;
        int   modulatorWaveIndex;
        float frequencyModulatorFrequency;

        public Template() {
            Random r = new Random();
            moogFrequency = 880;

            fAdsrAttack = .001f;
            fAdsrDelay = .1f;
            fAdsrRelease = .75f;
            waveIndex = 0;//r.nextInt(6);
            modulatorWaveIndex = r.nextInt(6);
            frequencyModulatorFrequency = (float) Math.pow(2, r.nextInt(9) + 8);
            modulatorFrequencyAmp = (float) Math.pow(2, r.nextInt(4) - 2);

        }

        @Override
        public String toString() {
            return "Template{" +
                   ", waveIndex=" + waveIndex +
                   ", modulatorWaveIndex=" + modulatorWaveIndex +
                   ", modulatorFrequencyAmp=" + modulatorFrequencyAmp +
                   ", frequencyModulatorFrequency=" + frequencyModulatorFrequency +

                   "} " + super.toString();
        }
    }


}