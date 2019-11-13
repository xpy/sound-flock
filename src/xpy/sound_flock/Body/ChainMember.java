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
    public int delay      = 4;
    public int delayIndex = 0;

    public ChainMember(PApplet pa, Note note, ChainBody chainBody) {
        this.pa = pa;
        this.note = note;
        this.chainBody = chainBody;
    }


    public synchronized void update(Body body) {/*

        idleIsDrawn = false;
        for (BaseInstrumentGenerator.BaseInstrument instrument : instruments) {
            if (instrument.getClass().getSimpleName().equals("KickInstrument")) {
                PApplet.println("KickInstrument");
                PApplet.println("instrument.isPlaying: " + instrument.isPlaying);
                PApplet.println("instrument.hasStarted: " + instrument.hasStarted);
                PApplet.println("instrument.isComplete: " + instrument.isComplete);

            }

            if (instrument.isPlaying || instrument.hasStarted) {
                hasStarted = true;
                EnvelopeFollower envf = instrument.getEnvFollower();
                ADSR adsr = instrument.finalADSR;
                float value = -1;
                if (instrument.hasStarted) {
                    instrument.hasStarted = false;
                    if (instrument.getClass().getSimpleName().equals("KickInstrument"))
                        value = 1f;
                }
                if (adsr.getLastValues().length > 0) {

                    value = envf.getLastValues()[0];
                }
                if (value <= 0 && adsr.getLastValues().length > 0) {

                    value = adsr.getLastValues()[0];
                }
                if (value >= 0) {
                    value = Math.abs(value);

//                    PApplet.println("abs(envf.getLastValues()[0]): "+Math.abs(envf.getLastValues()[0]));
*//*
                    if (instrument.getClass().getSimpleName().equals("KickInstrument")) {
                        PApplet.println("adsr.getLastValues().length: " + adsr.getLastValues().length);
                        for (float f : envf.getLastValues()) {
                            PApplet.println("KICK VALUES: " + f);
                        }
                        for (float f : adsr.getLastValues()) {
                            PApplet.println("KICK ADSR VALUES: " + f);
                        }
                    }
                    *//*

                    float enfValue = Math.abs(value) * 100;
                    enfValue = Math.min(10, enfValue);
                    if (enfValue >= 0f) {
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
        }*/

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
        instrument.addStartEvent(new BaseInstrumentGenerator.StartEvent() {
            @Override
            public synchronized void fire(BaseInstrumentGenerator.BaseInstrument instr) {
                Thread t = new Thread(new RenderLoop(instrument, chainBody));
                t.start();
            }
        });
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

    private static class RenderLoop
            implements Runnable {
        public BaseInstrumentGenerator.BaseInstrument instrument;
        public ChainBody                              chainBody;
        public int delay      = 1;
        public int delayIndex = 0;

        public RenderLoop(BaseInstrumentGenerator.BaseInstrument instrument, ChainBody chainBody) {
            this.instrument = instrument;
            this.chainBody = chainBody;
        }

        public synchronized void run() {
            while (instrument.isPlaying) {

                EnvelopeFollower envf = instrument.getEnvFollower();
                ADSR adsr = instrument.finalADSR;
                float value = -1;
/*                if (instrument.hasStarted) {
                    instrument.hasStarted = false;
                    if (instrument.getClass().getSimpleName().equals("Kickinstrument"))
                        value = 1f;
                }*/
                if (adsr.getLastValues().length > 0) {

                    value = envf.getLastValues()[0];
                }
                if (value <= 0 && adsr.getLastValues().length > 0) {

                    value = adsr.getLastValues()[0];
                }
                if (value >= 0) {
                    value = Math.abs(value);

                    float enfValue = Math.abs(value) * 100;
                    enfValue = Math.min(10, enfValue);
                    if (enfValue >= 0f) {
                        // Draw Here
                        if (delayIndex == 0) {
                            ChainParticle p = chainBody.chain.addParticle();
                            p.size *= enfValue + 1;
                        }
                        delayIndex = delayIndex == delay ? 0 : delayIndex + 1;
                    }
                }
                try {
                    Thread.sleep((int)((1000*instrument.instanceDuration)/10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
