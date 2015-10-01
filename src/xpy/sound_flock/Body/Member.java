package xpy.sound_flock.Body;

import xpy.sound_flock.Instruments.BaseInstrument;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Note;

/**
 * Member
 * Created by xpy on 29-Sep-15.
 */
public interface Member {

    void update ();

    void attachInstrument (BaseInstrument instrument);

    void setX (float value);

    void setY (float value);

    void setColor (int color);

    Note getNote();
}
