package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;
import processing.core.PApplet;
import xpy.sound_flock.Note;

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


    public SynthInstrumentGenerator() {
        maxPitch = Note.getPitchOfIndex(-24);
        minPitch = Note.getPitchOfIndex(12);
        this.template = createTemplate();
        println(this.template);
        maxDuration = 120f;
        minDuration = .25f;
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
        return maxDuration;
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
//            this.amplitude = amplitudeByFrequency(amplitude,  template.oscillatorFrequencyFactor.get(1));
            this.amplitude = amplitude*.5f;

            for (int i = 0; i < template.numOfOscillators; i++) {
                Oscil osc = new Oscil(initialFrequency * template.oscillatorFrequencyFactor.get(i), this.amplitude / (template.numOfOscillators), getWaveTable(template.oscillatorWave.get(i)));
                oscillators.add(osc);
            }

            // this.modulator = new Oscil(initialFrequency, amplitude / (template.numOfOscillators + 1), getWaveTable(template.modulatorWaveTable));

            for (int i = 0; i < template.numOfOscillators; i++) {
                (oscillators.get(i)).patch(s);
            }

            ml = new Multiplier(template.getTargetMoog());

            setMoog(new MoogFilter(template.getTargetMoog(), .0f, MoogFilter.Type.LP));
            moogModulator = new Oscil(template.moogModulatorFrequency, 1, template.moogModulatorWavetable);
//            moogModulator.setPhase(template.moogModulatorPhase);
            moogModulator.patch(ml).patch(moogFilter.frequency);

            preFinalUgen = s;
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
        float     moogModulatorPhase;

        Template() {

            Random r = new Random();
            fAdsrAttack = 0.1f;
            fAdsrDelay = .02f;
            fAdsrRelease = 0.1f;
            moogFrequency = 440 * (r.nextInt(2) + 1);
            setFullAmpDelay(15);
            Integer[] mogModulatorWaveTables = new Integer[]{0, 1, 3};
            int       a                      = r.nextInt(mogModulatorWaveTables.length);
            switch (mogModulatorWaveTables[a]) {
                default:
                    moogModulatorPhase = .25f;
                    break;
                case 1:
                    moogModulatorPhase = 0;
                    break;
                case 3:
                    moogModulatorPhase = .5f;
                    break;
            }
            if (mogModulatorWaveTables[a] == 0) {
                moogModulatorPhase = .25f;
            }
            moogModulatorWavetable = new Wavetable(getWaveTable(mogModulatorWaveTables[a]));
            moogModulatorWavetable.offset(1f);
            moogModulatorWavetable.normalize();
            moogModulatorWavetable.offset(.3f);
            moogModulatorWavetable.normalize();


            moogModulatorFrequency = (float) Math.pow(2, r.nextInt(7) - 2);

            this.numOfOscillators = 2;
//            this.modulatorWaveTable = 0;

            for (int i = 0; i < this.numOfOscillators; i++) {
//                oscillatorFrequencyFactor.add(1 - i * .25f);
//                oscillatorWave.add(r.nextInt(6));
            }
            oscillatorFrequencyFactor.add(1f);
            oscillatorFrequencyFactor.add((r.nextInt(6) + 1) * .125f);

            oscillatorWave.add(0);
            oscillatorWave.add(r.nextInt(6));


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
