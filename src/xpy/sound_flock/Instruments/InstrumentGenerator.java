package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

/**
 * InstrumentGenerator
 * Created by xpy on 20-Sep-15.
 */
public interface InstrumentGenerator {

    interface Instrument extends ddf.minim.ugens.Instrument {
        Sink getSink ();

        EnvelopeFollower getEnvFollower ();

        boolean isComplete ();

        void unpatch ();

    }

    public Template getTemplate ();

    interface Template {

        public void increaseMoogFactor (float value);

        public void decreaseMoogFactor (float value);

    }


    static Template createTemplate () {
        return null;
    }

    BaseInstrumentGenerator.BaseInstrument createInstrument (float frequency, float amplitude, AudioOutput out);

//    float getAmplitude ();

    float getMaxDuration ();


}
