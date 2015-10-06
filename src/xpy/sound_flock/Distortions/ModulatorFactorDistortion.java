package xpy.sound_flock.Distortions;

import processing.core.PApplet;
import xpy.sound_flock.Instruments.BaseInstrumentGenerator;

import java.util.Random;

/**
 * ModulatorFactorDistortion
 * Created by xpy on 02-Oct-15.
 */
public class ModulatorFactorDistortion implements Distortion {


    private int                   tuneAmount;
    private BaseInstrumentGenerator instrumentGenerator;

    public ModulatorFactorDistortion (BaseInstrumentGenerator instrumentGenerator) {
        this.instrumentGenerator = instrumentGenerator;
        tuneAmount = 1;
    }

    @Override
    public void apply () {
        instrumentGenerator.getTemplate().increaseModulatorFactor(tuneAmount);
    }

    @Override
    public void revert () {
        instrumentGenerator.getTemplate().decreaseModulatorFactor(tuneAmount);

    }
}
