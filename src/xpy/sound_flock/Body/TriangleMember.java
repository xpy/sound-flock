package xpy.sound_flock.Body;

import ddf.minim.ugens.EnvelopeFollower;
import processing.core.PApplet;
import xpy.sound_flock.Instruments.BaseInstrumentGenerator;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TriangleMember
 * Created by xpy on 10-Nov-15.
 */
public class TriangleMember implements Member {

    private Note note;

    public PApplet pa;
    public boolean hasStarted = false;
    public float   x;
    public float   y;
    public int     color;
    public boolean idleIsDrawn;
    public float alpha = 0;

    List<BaseInstrumentGenerator.BaseInstrument> instruments = new ArrayList<>();

    public TriangleMember(PApplet pa, Note note) {
        this.note = note;
        this.pa = pa;
    }

    @Override
    public void update(Body body) {
        TriangleBody myBody = (TriangleBody) body;
        for (BaseInstrumentGenerator.BaseInstrument instrument : instruments) {
            if (hasStarted || instrument.isPlaying) {
                hasStarted = true;
                EnvelopeFollower envf = instrument.getEnvFollower();

                if (envf.getLastValues().length > 0) {
                    alpha = 255;
                    float enfValue = envf.getLastValues()[0] * 10;
                    idleIsDrawn = true;
                }

            }
        }
        int newColor = pa.color(pa.red(color), pa.green(color), pa.blue(color), alpha);
        pa.fill(newColor);
        pa.rect(x + pa.width / 2, y + pa.height / 2, myBody.size, myBody.size);
        alpha -= 2;
        for (Iterator<BaseInstrumentGenerator.BaseInstrument> iterator = instruments.iterator(); iterator.hasNext(); ) {
            InstrumentGenerator.Instrument instrument = iterator.next();

            if (instrument.isComplete()) {
                instrument.unpatch();
                iterator.remove();

            }

        }

    }

    @Override
    public void attachInstrument(BaseInstrumentGenerator.BaseInstrument instrument) {
        this.instruments.add(instrument);

    }

    @Override
    public void setX(float value) {
        x = value;
    }

    @Override
    public void setY(float value) {
        y = value;

    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public Note getNote() {
        return note;
    }

}
