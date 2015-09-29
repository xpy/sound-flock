package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.*;

/**
 * InstrumentGenerator
 * Created by xpy on 20-Sep-15.
 */
public interface InstrumentGenerator {

    interface Instrument extends ddf.minim.ugens.Instrument {
    }

    interface Template {
    }

    static Template createTemplate (){
        return null;
    }

    ddf.minim.ugens.Instrument createInstrument (float frequency, float amplitude, AudioOutput out);

    float getAmplitude();
    float getMaxDuration();

}