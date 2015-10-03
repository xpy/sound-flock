package xpy.sound_flock.Instruments;

import ddf.minim.UGen;
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
}
