package xpy.sound_flock;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static processing.core.PApplet.println;


/**
 * SynthInstrument
 * Created by xpy on 20-Sep-15.
 */


public class SynthInstrument extends GenericInstrument {

    SynthInstrumentTemplate template;
    List<Oscil> oscillators = new ArrayList<>();

    Oscil      modulator;
    Oscil      moogModulator;
    Wavetable  moogModulatorWavetable;
    MoogFilter moogFilter;

    Summer s = new Summer();

    Multiplier ml;



    SynthInstrument (float frequency, float amplitude, AudioOutput out) {

        this.out = out;
        this.baseFrequency = frequency;
        this.initialFrequency = baseFrequency;
        this.amplitude = amplitude;

        moogModulatorWavetable = WavetableGenerator.gen9(4086, new float[]{1}, new float[]{1}, new float[]{1});
        moogModulatorWavetable.offset(1f);
        moogModulatorWavetable.normalize();
        moogModulatorWavetable.offset(.5f);


    }

    SynthInstrument (float frequency, float amplitude, AudioOutput out, SynthInstrumentTemplate template) {
        this(frequency, amplitude, out);
        this.template = template;
        createOscillators();
    }

    public void createOscillators () {
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

        moogFilter = new MoogFilter(10 * initialFrequency, .7f, MoogFilter.Type.LP);
        moogModulator = new Oscil(initialFrequency, amplitude, moogModulatorWavetable);
        moogModulator.patch(ml).patch(moogFilter.frequency);
//        moogFilter = new MoogFilter((r.nextInt(7) + 2) * initialFrequency, .7f, MoogFilter.Type.LP);

        adsr = new ADSR(.8f, 0.3f, .3f, 0.7f, 0.3f);
        s.patch(moogFilter).patch(adsr);

    }

    public void setInitialFrequency (float frequency) {
        this.initialFrequency = frequency;
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
