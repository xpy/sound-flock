package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static processing.core.PApplet.println;


/**
 * SynthInstrumentGenerator
 * Created by xpy on 20-Sep-15.
 */


public class SynthInstrumentGenerator extends BaseInstrumentGenerator {

    Template template;
    public float amplitude = .65f;


    public SynthInstrumentGenerator() {

        this.template = createTemplate();
        println(this.template);
    }

    SynthInstrumentGenerator(Template template) {

        this.template = template;
    }

    @Override
    public float getAmplitude() {
        return amplitude;
    }

    public SynthInstrumentGenerator.Template createTemplate() {

        return new SynthInstrumentGenerator.Template();
    }

    @Override
    public SynthInstrumentGenerator.Template getTemplate() {
        return template;
    }

    public BaseInstrument createInstrument(float frequency, float amplitude, AudioOutput out) {
        return new SynthInstrument(frequency, amplitude, out);
    }

    @Override
    public float getMaxDuration() {
        return 120f;
    }


    public class SynthInstrument extends BaseInstrument {

        List<Oscil> oscillators = new ArrayList<>();


        Summer s = new Summer();

        Multiplier ml;

        float baseFrequency;
        float initialFrequency;

        Oscil modulator;
        Oscil moogModulator;


        public SynthInstrument(float frequency, float amplitude, AudioOutput out) {


            this.out = out;
            this.baseFrequency = frequency;
            this.initialFrequency = baseFrequency;
            this.amplitude = amplitude;

            for (int i = 0; i < template.numOfOscillators; i++) {
                Oscil osc = new Oscil(initialFrequency * template.oscillatorFrequencyFactor.get(i), amplitude / (template.numOfOscillators + 1), getWaveTable(template.oscillatorWave.get(i)));
                oscillators.add(osc);
            }

            this.modulator = new Oscil(initialFrequency, amplitude / (template.numOfOscillators + 1), getWaveTable(template.modulatorWaveTable));
//            ampLine.patch(modulator.amplitude);
            for (int i = 0; i < template.numOfOscillators; i++) {
                this.modulator.patch(oscillators.get(i)).patch(s);
//                ampLine.patch(oscillators.get(i).amplitude);

            }

//        ml = new Multiplier((r.nextInt(7) + 2) * initialFrequency);
            ml = new Multiplier(template.getTargetMoog());

            setMoog(new MoogFilter(template.getTargetMoog(), .7f, MoogFilter.Type.LP));
            moogModulator = new Oscil(template.moogModulatorFrequency, 1, template.moogModulatorWavetable);
            moogModulator.patch(ml).patch(moogFilter.frequency);
//        moogFilter = new MoogFilter((r.nextInt(7) + 2) * initialFrequency, .7f, MoogFilter.Type.LP);

            preFinalUgen = s;
        }

        @Override
        public void noteOn(float dur) {
            super.noteOn(dur);
        }

        public void setInitialFrequency(float frequency) {
            this.initialFrequency = frequency;
        }


    }

    public static class Template extends BaseInstrumentGenerator.BaseTemplate {

        List<Float>   oscillatorFrequencyFactor = new ArrayList<>();
        List<Integer> oscillatorWave            = new ArrayList<>();

        int   modulatorWaveTable;
        float moogModulatorFrequency;

        int       numOfOscillators;
        Wavetable moogModulatorWavetable;

        Template() {

            fAdsrAttack = 0.3f;
            fAdsrDelay = .3f;
            fAdsrRelease = 0.3f;
            moogFrequency = 400;
            setFullAmpDelay(15);

            Random r = new Random();
            moogModulatorFrequency = 1f;//(r.nextInt(32) + 1) / 4f;
            this.numOfOscillators = r.nextInt(4) + 1;
            this.modulatorWaveTable = r.nextInt(4);

            for (int i = 0; i < this.numOfOscillators; i++) {
                oscillatorFrequencyFactor.add(1 - i * .25f);
                oscillatorWave.add(r.nextInt(6));
            }
            moogModulatorWavetable = WavetableGenerator.gen9(4086, new float[]{1}, new float[]{1}, new float[]{0});
            moogModulatorWavetable.offset(1f);
            moogModulatorWavetable.normalize();
            moogModulatorWavetable.offset(.7f);
            moogModulatorWavetable.normalize();


        }

        @Override
        public String toString() {
            return "Template{" +
                   "oscillatorFrequencyFactor=" + oscillatorFrequencyFactor +
                   ", oscillatorWave=" + oscillatorWave +
                   ", modulatorWaveTable=" + modulatorWaveTable +
                   ", moogModulatorFrequency=" + moogModulatorFrequency +
                   ", numOfOscillators=" + numOfOscillators +
                   '}';
        }
    }


}
