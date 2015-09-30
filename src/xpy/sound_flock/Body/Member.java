package xpy.sound_flock.Body;

import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Note;

/**
 * Member
 * Created by xpy on 29-Sep-15.
 */
public interface Member {

    void update ();

    void attachInstrument (InstrumentGenerator.Instrument instrument);

    void setX (float value);

    void setY (float value);

    Note getNote();
}
