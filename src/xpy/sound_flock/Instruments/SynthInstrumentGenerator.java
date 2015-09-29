package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static processing.core.PApplet.println;


/**
 * SynthInstrumentGenerator
 * Created by xpy on 20-Sep-15.
 */


public class SynthInstrumentGenerator implements InstrumentGenerator {

    Template template;
    public float amplitude = .45f;


    public SynthInstrumentGenerator () {

        this.template = createTemplate();
    }

    SynthInstrumentGenerator (Template template) {

        this.template = template;
    }

    public Wavetable getWaveTable (int k) {
        switch (k) {
            default:
                return Waves.SINE;
            case 1:
                return Waves.SAW;
            case 2:
                return Waves.SQUARE;
            case 3:
                return Waves.TRIANGLE;
            case 4:
                return Waves.PHASOR;
            case 5:
                return Waves.QUARTERPULSE;
        }

    }

    public SynthInstrumentGenerator.Template createTemplate () {

        return new SynthInstrumentGenerator.Template();
    }

    public ddf.minim.ugens.Instrument createInstrument (float frequency, float amplitude, AudioOutput out) {
        return new SynthInstrument(frequency, amplitude, out);
    }

    @Override
    public float getAmplitude () {
        return amplitude;
    }

    @Override
    public float getMaxDuration () {
        return 10f;
    }


    public class SynthInstrument implements ddf.minim.ugens.Instrument {

        List<Oscil> oscillators = new ArrayList<>();


        Summer s = new Summer();

        Multiplier ml;

        AudioOutput out;
        float       baseFrequency;
        float       initialFrequency;
        float       amplitude;
        ADSR        adsr;

        Oscil      modulator;
        Oscil      moogModulator;
        Wavetable  moogModulatorWavetable;
        MoogFilter moogFilter;


        public SynthInstrument (float frequency, float amplitude, AudioOutput out) {


            this.out = out;
            this.baseFrequency = frequency;
            this.initialFrequency = baseFrequency;
            this.amplitude = amplitude;

            moogModulatorWavetable = WavetableGenerator.gen9(4086, new float[]{1}, new float[]{1}, new float[]{0});
            moogModulatorWavetable.offset(1f);
            moogModulatorWavetable.normalize();
            moogModulatorWavetable.offset(.7f);
            moogModulatorWavetable.normalize();

            for (int i = 0; i < template.numOfOScillators; i++) {
                Oscil osc = new Oscil(initialFrequency * template.oscillatorFrequencyFactor.get(i), amplitude, getWaveTable(template.oscillatorWave.get(i)));
                oscillators.add(osc);
            }

            this.modulator = new Oscil(initialFrequency, amplitude, getWaveTable(template.modulatorWaveTable));

            for (int i = 0; i < template.numOfOScillators; i++) {
                this.modulator.patch(oscillators.get(i)).patch(s);
            }

//        ml = new Multiplier((r.nextInt(7) + 2) * initialFrequency);
            ml = new Multiplier(5 * initialFrequency);

            moogFilter = new MoogFilter(10 * baseFrequency, .7f, MoogFilter.Type.LP);
            moogModulator = new Oscil(.5f, amplitude, moogModulatorWavetable);
            moogModulator.patch(ml).patch(moogFilter.frequency);
//        moogFilter = new MoogFilter((r.nextInt(7) + 2) * initialFrequency, .7f, MoogFilter.Type.LP);

            adsr = new ADSR(amplitude, 0.3f, .3f, amplitude, 0.3f);
            s.patch(moogFilter).patch(adsr);

        }

        public void setInitialFrequency (float frequency) {
            this.initialFrequency = frequency;
        }

        @Override
        public void noteOn (float v) {
//        l.activate();
            adsr.noteOn();
//        adsr2.noteOn();
            // patch to the output
            adsr.patch(out);
        }

        @Override
        public void noteOff () {
            adsr.unpatchAfterRelease(out);
            // call the noteOff
            adsr.noteOff();

        }

    }

    public static class Template implements InstrumentGenerator.Template {

        List<Float>   oscillatorFrequencyFactor = new ArrayList<>();
        List<Integer> oscillatorWave            = new ArrayList<>();

        int modulatorWaveTable;

        int numOfOScillators;

        Template () {
            Random r = new Random();

            this.numOfOScillators = r.nextInt(3) + 1;
//        this.baseFrequency = frequency;
//        this.initialFrequency = baseFrequency;
//        this.amplitude = amplitude;

            this.modulatorWaveTable = r.nextInt(4);

            for (int i = 1; i <= this.numOfOScillators; i++) {
                oscillatorFrequencyFactor.add((r.nextInt(i + 1) + i) * .25f);
                oscillatorWave.add(r.nextInt(4));
            }


        }
    }


}
