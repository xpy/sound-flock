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

    interface Template {
    }

    static Template createTemplate () {
        return null;
    }

    Instrument createInstrument (float frequency, float amplitude, AudioOutput out);

    float getAmplitude ();

    float getMaxDuration ();



}
