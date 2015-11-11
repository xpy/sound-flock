package xpy.sound_flock.Body;

import ddf.minim.ugens.ADSR;
import ddf.minim.ugens.EnvelopeFollower;
import processing.core.PApplet;
import xpy.sound_flock.ChainParticle.ChainParticle;
import xpy.sound_flock.Instruments.BaseInstrumentGenerator;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Instruments.KickInstrumentGenerator;
import xpy.sound_flock.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ChainMember
 * Created by xpy on 11-Nov-15.
 */
public class ChainMember implements Member {

    PApplet   pa;
    Note      note;
    ChainBody chainBody;
    List<BaseInstrumentGenerator.BaseInstrument> instruments = new ArrayList<>();

    public boolean hasStarted = false;
    public boolean idleIsDrawn;
    public int delay      = 2;
    public int delayIndex = 0;

    public ChainMember(PApplet pa, Note note, ChainBody chainBody) {
        this.pa = pa;
        this.note = note;
        this.chainBody = chainBody;
    }


    public void update(Body body) {

        pa.noStroke();
        idleIsDrawn = false;
        for (BaseInstrumentGenerator.BaseInstrument instrument : instruments) {
            if (instrument.isPlaying) {
                hasStarted = true;
                EnvelopeFollower envf = instrument.getEnvFollower();
                ADSR adsr = instrument.finalADSR;

                if (adsr.getLastValues().length > 0) {
//                    PApplet.println("abs(envf.getLastValues()[0]): "+Math.abs(envf.getLastValues()[0]));
                    if (instrument.getClass().getSimpleName().equals("KickInstrument")) {
                        PApplet.println("adsr.getLastValues().length: "+adsr.getLastValues().length);
                        for (float f : envf.getLastValues()) {
                            PApplet.println("KICK VALUES: " + f);
                        }
                        for (float f : adsr.getLastValues()) {
                            PApplet.println("KICK ADSR VALUES: " + f);
                        }
                    }
                    float enfValue = Math.abs(adsr.getLastValues()[0]) * 100;
                    enfValue = Math.min(50, enfValue);
                    if (enfValue > 0f) {
                        // Draw Here
                        if (delayIndex == 0) {
                            ChainParticle p = chainBody.chain.addParticle();
                            p.size *= enfValue + 1;
                            idleIsDrawn = true;
                        }
                        delayIndex = delayIndex == delay ? 0 : delayIndex + 1;
                    }
                }
            }
        }

        if (hasStarted && !idleIsDrawn) {

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
