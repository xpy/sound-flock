package xpy.sound_flock.Body;

import ddf.minim.ugens.Instrument;
import processing.core.*;

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
    private Instrument instrument;


    public CircleBody (PApplet pa) {
        this.pa = pa;
        radius = 30;
        maxRadius = 15;
        bodyColor = color(204, 153, 0);
        expandColor = color(100, 200, 0, .5f);
        x = 10;
        y = 10;
    }

    public void update () {
        int     prevFill   = pa.g.fillColor;
        boolean prevStroke = pa.g.stroke;
        pa.noStroke();
        pa.fill(bodyColor);
        pa.ellipse(x, y, radius, radius);
        pa.ellipse(x, y, radius, radius);
        pa.fill(prevFill);
        pa.g.stroke = prevStroke;
    }

    @Override
    public void attachInstrument (Instrument instrument) {
        this.instrument = instrument;
    }
}
