package xpy.sound_flock.Distortions;

import processing.core.PApplet;
import xpy.sound_flock.Instruments.BaseInstrumentGenerator;

import java.util.Random;

/**
 * MoogFactorDistortion
 * Created by xpy on 02-Oct-15.
 */
public class MoogFactorDistortion implements Distortion {


    private float                     tuneAmount;
    private BaseInstrumentGenerator instrumentGenerator;

    public MoogFactorDistortion (BaseInstrumentGenerator instrumentGenerator) {
        this.instrumentGenerator = instrumentGenerator;
        tuneAmount = 1.5f;
    }

    @Override
    public void apply () {
        instrumentGenerator.getTemplate().increaseMoogFactor(tuneAmount);
    }

    @Override
    public void revert () {
        instrumentGenerator.getTemplate().decreaseMoogFactor(tuneAmount);

    }
}
