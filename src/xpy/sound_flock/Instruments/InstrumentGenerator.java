package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

/**
 * InstrumentGenerator
 * Created by xpy on 20-Sep-15.
 */
public interface InstrumentGenerator {

    interface Instrument extends ddf.minim.ugens.Instrument {
        Sink getSink();

        EnvelopeFollower getEnvFollower();

        boolean isComplete();

        void unpatch();

        void setMoog(MoogFilter moog);

    }

    public Template getTemplate();

    interface Template {

        void increaseMoogFactor(float value);

        void decreaseMoogFactor(float value);

        void increaseModulatorFactor(int value);

        void decreaseModulatorFactor(int value);

        float fAdsrRelease();

        boolean hasMoog();

        void setHasMoog(boolean moog);

        float getMoogFrequency();

        float getMoogFactor();

        float getTargetMoog();

        void reverseADSR();

        ADSR getFinalADSR(float amplitude);

        void activateAmpLine(float dur, Multiplier ml);
    }


    static Template createTemplate() {
        return null;
    }

    BaseInstrumentGenerator.BaseInstrument createInstrument(float frequency, float amplitude, AudioOutput out);

//    float getAmplitude ();

    float getMaxDuration();


}
