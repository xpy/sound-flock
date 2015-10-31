package xpy.sound_flock.Instruments;

import ddf.minim.ugens.ADSR;
import ddf.minim.ugens.Waves;

/**
 * TsikInstrumentGenerator
 * Created by xpy on 12-Oct-15.
 */
public class TsikInstrumentGenerator extends SparkInstrumentGenerator {


    public TsikInstrumentGenerator() {
        template = createTemplate();
        maxDuration = .25f;
        minDuration = .125f;
    }

    public Template createTemplate() {
        return new Template();
    }


    public static class Template extends SparkInstrumentGenerator.Template {
        @Override
        public ADSR getFinalADSR(float amplitude) {
            return new ADSR(amplitude * 1.5f, fAdsrAttack, fAdsrDelay, 0, fAdsrRelease);
        }

        public Template() {
            super();
            moogFrequency = 1000;

        }
    }

}
