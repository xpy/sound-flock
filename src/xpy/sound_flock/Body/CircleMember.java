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
 * CircleMember
 * Created by xpy on 29-Sep-15.
 */
public class CircleMember implements Member {


    PApplet pa;
    Note    note;
    List<BaseInstrumentGenerator.BaseInstrument> instruments = new ArrayList<>();

    float radius;
    float offsetRadius;
    public int bodyColor;
    int expandColor;
    public float x;
    public float y;

    public boolean hasStarted = false;
    public boolean idleIsDrawn;

    public float maxasdfasdfValue = 0;

    public CircleMember (PApplet pa, Note note) {
        this.pa = pa;
        this.note = note;

        radius = 30;
        offsetRadius = 20;
        bodyColor = pa.color(204, 153, 0);
        expandColor = pa.color(100, 200, 0, 50);
        expandColor = bodyColor;
        x = 10;
        y = 10;

    }

    public void update () {

        int     prevFill   = pa.g.fillColor;
        boolean prevStroke = pa.g.stroke;
        pa.noStroke();

        idleIsDrawn = false;
        for (BaseInstrumentGenerator.BaseInstrument instrument : instruments) {
            if (hasStarted || instrument.isPlaying) {
                hasStarted = true;
                EnvelopeFollower envf = instrument.getEnvFollower();

                if (envf.getLastValues().length > 0) {
                    float enfValue = envf.getLastValues()[0] * 10;
                    float prev = maxasdfasdfValue;
//                    maxasdfasdfValue = Math.max(envf.getLastValues()[0], maxasdfasdfValue);
//                    if (maxasdfasdfValue != prev)
//                        PApplet.println("envf: " + (maxasdfasdfValue));
                    pa.fill(expandColor);

                    pa.ellipse(x, y, radius + offsetRadius * enfValue, radius + offsetRadius * enfValue);
                    idleIsDrawn = true;
                }

            }
        }

        if (!idleIsDrawn) {
            pa.fill(expandColor);
            idleIsDrawn = true;
            pa.ellipse(x, y, 5, 5);

        }

        pa.fill(prevFill);
        pa.g.stroke = prevStroke;

        for (Iterator<BaseInstrumentGenerator.BaseInstrument> iterator = instruments.iterator(); iterator.hasNext(); ) {
            InstrumentGenerator.Instrument instrument = iterator.next();

            if (instrument.isComplete()) {
                instrument.unpatch();
                iterator.remove();

            }

        }

    }

    @Override
    public void attachInstrument (BaseInstrumentGenerator.BaseInstrument instrument) {
        this.instruments.add(instrument);
    }

    @Override
    public void setX (float value) {
        this.x = value;
    }

    @Override
    public void setY (float value) {
        this.y = value;
    }

    @Override
    public void setColor (int color) {
        this.bodyColor = color;
        this.expandColor = color;
    }

    @Override
    public Note getNote () {
        return note;
    }
}
