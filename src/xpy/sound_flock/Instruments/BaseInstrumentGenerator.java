package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.UGen;
import ddf.minim.ugens.*;
import processing.core.PApplet;
import tests.Maestro;

/**
 * BaseInstrumentGenerator
 * Created by xpy on 02-Oct-15.
 */
public abstract class BaseInstrumentGenerator implements InstrumentGenerator {

    public float amplitude = .65f;

    // protected InstrumentGenerator.Template template;

    public static Wavetable getWaveTable (int k) {
        switch (k) {
            default:
                return Waves.SINE;
            case 1:
                return Waves.SAW;
            case 2:
                return Waves.SQUARE;
            case 3:
                return Waves.TRIANGLE;
            case 4:
                return Waves.PHASOR;
            case 5:
                return Waves.QUARTERPULSE;
        }

    }
    public float getAmplitude () {
        return amplitude;
    }

    public abstract static class BaseTemplate implements Template {

        protected float moogFactor       = 1;
        protected float targetMoogFactor = 1;
        public    float moogFrequency    = 440;

        boolean hasMoog = false;

        public float fAdsrAttack  = .01f;
        public float fAdsrDelay   = .05f;
        public float fAdsrRelease = .5f;

        public void increaseMoogFactor (float value) {
            targetMoogFactor *= value;
            PApplet.println(getTargetMoog());
        }

        @Override
        public float getMoogFactor () {
            return moogFactor;
        }

        @Override
        public float getTargetMoog () {
            return Math.min(moogFrequency * targetMoogFactor, 20000);
        }

        public void decreaseMoogFactor (float value) {
            targetMoogFactor /= value;
            PApplet.println(getTargetMoog());
        }

        public ADSR getFinalADSR (float amplitude) {
            return new ADSR(amplitude*1.5f, fAdsrAttack, fAdsrDelay, amplitude, fAdsrRelease);
        }

        @Override
        public float fAdsrRelease () {
            return fAdsrRelease;
        }

        @Override
        public boolean hasMoog () {
            return hasMoog;
        }

        @Override
        public void setHasMoog (boolean moog) {
            this.hasMoog = true;
        }

        @Override
        public float getMoogFrequency () {
            return moogFrequency;
        }

        @Override
        public void reverseADSR () {
            float tmp = fAdsrAttack;
            fAdsrAttack = fAdsrRelease;
            fAdsrRelease = tmp;

        }
    }

    /**
     * BaseInstrument
     * Created by xpy on 30-Sep-15.
     */
    public abstract class BaseInstrument implements Instrument {

        public float frequency;
        public float amplitude;

        protected boolean isComplete                 = false;
        public    boolean isPlaying                  = false;
        protected long    completesAt                = 0;
        protected int     envelopeFollowerBufferSize = 2048;
        public    float   releaseTime                = .5f;

        public AudioOutput out;
        public Sink             sink             = xpy.sound_flock.Maestro.sink;
        public EnvelopeFollower envelopeFollower = new EnvelopeFollower(0, .2f, 64);
        public MoogFilter moogFilter;
        public ADSR       finalADSR;
        public UGen       preFinalUgen;

        @Override
        public Sink getSink () {
            return sink;
        }

        @Override
        public EnvelopeFollower getEnvFollower () {
            return envelopeFollower;
        }

        @Override
        public void setMoog (MoogFilter moog) {
            this.moogFilter = moog;
            getTemplate().setHasMoog(true);
        }

        public ADSR getFinalAdsr () {
            return finalADSR;
        }

        @Override
        public boolean isComplete () {
            return isComplete && System.currentTimeMillis() > completesAt;
        }

        @Override
        public void noteOn (float dur) {
            patch(preFinalUgen, dur);
        }

        @Override
        public void noteOff () {
            finalADSR.unpatchAfterRelease(out);
            finalADSR.unpatchAfterRelease(envelopeFollower);
            finalADSR.noteOff();
            setComplete();

        }


        public void setComplete () {
            isComplete = true;
            isPlaying = false;
            completesAt = System.currentTimeMillis() + ((long) (getTemplate().fAdsrRelease() * 1000));
        }

        public void patch (UGen uGen, float duration) {
            envelopeFollower = new EnvelopeFollower(0, duration + getTemplate().fAdsrRelease(), envelopeFollowerBufferSize);
            UGen lastUgen = uGen;
            if (getTemplate().hasMoog()) {
                uGen.patch(moogFilter);
                lastUgen = moogFilter;
//                Line l = new Line(duration + getTemplate().fAdsrRelease(), getTemplate().getMoogFrequency() * getTemplate().getMoogFactor(), getTemplate().getMoogFrequency() * getTemplate().getTargetMoogFactor());
//                l.activate();
//                l.patch(moogFilter.frequency);
//                template.moogFactor = template.targetMoogFactor;
            }
            finalADSR = getTemplate().getFinalADSR(this.amplitude);

            lastUgen.patch(finalADSR);
            finalADSR.patch(envelopeFollower).patch(sink);
            finalADSR.patch(out);
            finalADSR.noteOn();
            isPlaying = true;

        }

        public void unpatch () {
//            sink.unpatch(out);
            envelopeFollower.unpatch(sink);
        }
    }
}
