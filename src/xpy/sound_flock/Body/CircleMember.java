package xpy.sound_flock.Body;

import ddf.minim.ugens.EnvelopeFollower;
import processing.core.PApplet;
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
    List<InstrumentGenerator.Instrument> instruments = new ArrayList<>();

    float radius;
    float offsetRadius;
    int   bodyColor;
    int   expandColor;
    public float x;
    public float y;

    public CircleMember (PApplet pa, Note note, InstrumentGenerator.Instrument instrument) {
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

        for (InstrumentGenerator.Instrument instrument : instruments) {

            EnvelopeFollower envf = instrument.getEnvFollower();
            for (int j = 0; j < envf.getLastValues().length; j++) {
//                println("envf: "+envf.getLastValues()[j]);
                float enfValue = envf.getLastValues()[j] * 10;
//                PApplet.println("offsetRadius*enfValue: " + (enfValue));
                pa.fill(expandColor);

                pa.ellipse(x, y, radius + offsetRadius * enfValue, radius + offsetRadius * enfValue);

            }

        }
        PApplet.println("instruments.size(): " + instruments.size());

        pa.fill(bodyColor);
        pa.ellipse(x, y, radius, radius);
        pa.fill(prevFill);
        pa.g.stroke = prevStroke;

       for (Iterator<InstrumentGenerator.Instrument> iterator = instruments.iterator(); iterator.hasNext(); ) {
            InstrumentGenerator.Instrument instrument = iterator.next();
           PApplet.println("instrument.isComplete(): " + instrument.isComplete());

           if (instrument.isComplete())
                iterator.remove();

        }

    }

    @Override
    public void attachInstrument (InstrumentGenerator.Instrument instrument) {
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
    public Note getNote () {
        return note;
    }
}
