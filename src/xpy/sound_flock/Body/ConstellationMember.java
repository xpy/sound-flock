package xpy.sound_flock.Body;

import Constellation.ConstellationNode;
import ddf.minim.ugens.EnvelopeFollower;
import processing.core.PApplet;
import xpy.sound_flock.Instruments.BaseInstrumentGenerator;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ConstellationMember
 * Created by xpy on 24-Oct-15.
 */
public class ConstellationMember implements Member {

    PApplet pa;
    Note    note;
    List<BaseInstrumentGenerator.BaseInstrument> instruments = new ArrayList<>();
    public ConstellationNode node;
    public boolean hasStarted = false;

    public ConstellationMember(PApplet pa, Note note) {
        this.pa = pa;
        this.note = note;
        this.node = new ConstellationNode(pa);
    }

    public void update(Body body) {

        for (BaseInstrumentGenerator.BaseInstrument instrument : instruments) {
            if (hasStarted || instrument.isPlaying) {
                hasStarted = true;
                EnvelopeFollower envf = instrument.getEnvFollower();

                if (envf.getLastValues().length > 0) {
                    float enfValue = envf.getLastValues()[0] * 10;
                    if (enfValue > 0){
                        node.bounce();
                        node.drawBurst(50);
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
    }

    @Override
    public void setY(float value) {
    }

    @Override
    public void setColor(int color) {
    }

    @Override
    public Note getNote() {
        return note;
    }


}