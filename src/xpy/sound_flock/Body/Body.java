package xpy.sound_flock.Body;

import ddf.minim.ugens.Instrument;
import xpy.sound_flock.Instruments.InstrumentGenerator;

/**
 * Body
 * Created by xpy on 29-Sep-15.
 */
public interface Body {


    void update ();

    void attachInstrument(InstrumentGenerator.Instrument instrument);

}
