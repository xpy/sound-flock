package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.UGen;
import ddf.minim.ugens.EnvelopeFollower;
import ddf.minim.ugens.Sink;
import processing.core.PApplet;

/**
 * BaseInstrumentGenerator
 * Created by xpy on 02-Oct-15.
 */
public abstract class BaseInstrumentGenerator implements InstrumentGenerator {

    public float amplitude = .65f;

    public UGen finalUgen;

    public float getAmplitude () {
        return amplitude;
    }

    public abstract static class BaseTemplate implements Template {

        protected float moogFactor;
        protected float targetMoogFactor = 1;

        public void increaseMoogFactor (float value) {
            targetMoogFactor *= value;
            PApplet.println(440 * targetMoogFactor);
        }

        public void decreaseMoogFactor (float value) {
            targetMoogFactor /= value;
            PApplet.println(100 * targetMoogFactor);
        }

    }

    /**
     * BaseInstrument
     * Created by xpy on 30-Sep-15.
     */
    public abstract static class BaseInstrument implements Instrument {

        public float frequency;
        public float amplitude;

        protected boolean isComplete                 = false;
        public    boolean isPlaying                  = false;
        protected long    completesAt                = 0;
        protected float   releaseTime                = .5f;
        protected int     envelopeFollowerBufferSize = 2048;

        public UGen lastUgen;

        public AudioOutput out;
        public Sink             sink             = new Sink();
        public EnvelopeFollower envelopeFollower = new EnvelopeFollower(0, .2f, 64);

        @Override
        public Sink getSink () {
            return sink;
        }

        @Override
        public EnvelopeFollower getEnvFollower () {
            return envelopeFollower;
        }

        @Override
        public boolean isComplete () {
            return isComplete && System.currentTimeMillis() > completesAt;
        }

        public void setComplete () {
            isComplete = true;
            isPlaying = false;
            completesAt = System.currentTimeMillis() + ((long) (releaseTime * 1000));
        }

        public void patch (UGen uGen, float duration) {
            envelopeFollower = new EnvelopeFollower(0, duration + releaseTime, envelopeFollowerBufferSize);
            lastUgen = uGen;
            uGen.patch(envelopeFollower).patch(sink).patch(out);
            uGen.patch(out);
        }

        public void unpatch () {
            sink.unpatch(out);
            envelopeFollower.unpatch(sink);

        }
    }
}
