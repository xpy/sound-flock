package xpy.sound_flock.Distortions;

import xpy.sound_flock.Instruments.InstrumentGenerator;

/**
 * Created by xpy on 04-Oct-15.
 */
public class ADSRReverseDistortion implements Distortion {

    private InstrumentGenerator.Template template;

    public ADSRReverseDistortion (InstrumentGenerator instrument) {

        template = instrument.getTemplate();

    }

    @Override
    public void apply () {
        template.reverseADSR();
    }

    @Override
    public void revert () {
        template.reverseADSR();

    }
}
