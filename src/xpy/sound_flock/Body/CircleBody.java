package xpy.sound_flock.Body;

import ddf.minim.ugens.EnvelopeFollower;
import ddf.minim.ugens.Instrument;
import ddf.minim.ugens.Sink;
import processing.core.*;
import xpy.sound_flock.Instruments.InstrumentGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xpy on 29-Sep-15.
 */
public class CircleBody extends PApplet implements Body {

    PApplet pa;
    float   radius;
    float   maxRadius;
    int     bodyColor;
    int     expandColor;
    float   x;
    float   y;
    private List<InstrumentGenerator.Instrument> instruments = new ArrayList<>();


    public CircleBody (PApplet pa) {
        this.pa = pa;
        radius = 30;
        maxRadius = 50;
        bodyColor = color(204, 153, 0);
        expandColor = color(100, 200, 0, 50);
        x = 10;
        y = 10;
    }

    public void update () {
        int     prevFill   = pa.g.fillColor;
        boolean prevStroke = pa.g.stroke;
        pa.noStroke();
        pa.fill(expandColor);

        for (int i = 0; i < instruments.size(); i++) {
            EnvelopeFollower envf = instruments.get(i).getEnvFollower();
            for (int j = 0; j < envf.getLastValues().length; j++) {
//                println("envf: "+envf.getLastValues()[j]);
                float enfValue= envf.getLastValues()[j] * 10;
                println("maxRadius*enfValue: " +(maxRadius*enfValue));
                pa.ellipse(x+(i*50), y, maxRadius*enfValue, maxRadius*enfValue);

            }
        }

        pa.fill(bodyColor);
        pa.ellipse(x, y, radius, radius);
        pa.fill(prevFill);
        pa.g.stroke = prevStroke;
    }


    public void attachInstrument (InstrumentGenerator.Instrument instrument) {
        this.instruments.add(instrument);
    }
}
