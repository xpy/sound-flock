package xpy.sound_flock.Body;

import Boids.FlockWorld.*;
import ddf.minim.ugens.EnvelopeFollower;
import processing.core.PApplet;
import tests.Maestro;
import xpy.sound_flock.Instruments.BaseInstrumentGenerator;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * BoidMember
 * Created by xpy on 20-Oct-15.
 */
public class BoidMember implements Member {


    private Note note;
    private List<Boid> boids = new ArrayList<>();

    public boolean hasStarted = false;

    List<BaseInstrumentGenerator.BaseInstrument> instruments = new ArrayList<>();


    public BoidMember(PApplet pa, Note note) {
        this.note = note;
        for (int i = 0; i < 20; i++) {
            boids.add(new Boid(pa, pa.width / 2, pa.height / 2));
        }
    }


    @Override
    public void update(Body body) {

    }

    public void _update(Flock flock) {

        for (Boid b : boids) {
            for (BaseInstrumentGenerator.BaseInstrument instrument : instruments) {
                if (hasStarted || instrument.isPlaying) {
                    hasStarted = true;
                    EnvelopeFollower envf = instrument.getEnvFollower();

                    if (envf.getLastValues().length > 0) {
                        float enfValue = envf.getLastValues()[0] * 10;
                        if (b.colorBurst < 10)
                            b.colorBurst += 20;
        /*            if (sizeBurst < 5)
                        sizeBurst += 20 * enfValue;*/
//                        if (flock.speedBurst < 2)
//                            flock.speedBurst += .05f;
                        flock.separationBurst += .5f;
                    }

                }
            }

        }

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
        for (Boid b : boids)
            b.location.x = value;
    }

    @Override
    public void setY(float value) {
        for (Boid b : boids)
            b.location.y = value;
    }

    @Override
    public void setColor(int color) {

    }

    @Override
    public Note getNote() {
        return note;

    }

    public List<Boid> getBoids() {
        return boids;
    }
}
