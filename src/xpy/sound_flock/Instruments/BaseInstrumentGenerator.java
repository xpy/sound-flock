package xpy.sound_flock.Instruments;

import com.sun.org.apache.xpath.internal.operations.Mult;
import ddf.minim.AudioOutput;
import ddf.minim.UGen;
import ddf.minim.ugens.*;
import processing.core.PApplet;
import xpy.sound_flock.Note;

/**
 * BaseInstrumentGenerator
 * Created by xpy on 02-Oct-15.
 */
public abstract class BaseInstrumentGenerator implements InstrumentGenerator {

    public    float amplitude   = .75f;
    public    float minDuration = .25f;
    public    float maxDuration = 2;
    protected float maxPitch    = Note.getPitchOfIndex(-24);
    protected float minPitch    = Note.getPitchOfIndex(24);

    // protected InstrumentGenerator.Template template;

    public static Wavetable getWaveTable(int k) {
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

    public float getAmplitude() {
        return amplitude;
    }

    @Override
    public float normalizeDuration(float duration) {

        return Math.max(Math.min(duration, maxDuration), minDuration);
    }

    public abstract static class BaseTemplate implements Template {

        protected float moogFactor       = 1;
        protected float targetMoogFactor = 1;
        public    float moogFrequency    = 440;
        protected float modulatorFrequencyAmp;

        boolean hasMoog = false;

        public float fAdsrAttack  = .01f;
        public float fAdsrDelay   = .05f;
        public float fAdsrRelease = .5f;

        float fullAmpTime    = 0;
        float currentLineAmp = 0;

        public void increaseMoogFactor(float value) {
            targetMoogFactor *= value;
        }

        @Override
        public float getMoogFactor() {
            return moogFactor;
        }

        @Override
        public float getTargetMoog() {
            return Math.min(moogFrequency * targetMoogFactor, 2000);
        }

        public void decreaseMoogFactor(float value) {
            targetMoogFactor /= value;
        }

        public void setFullAmpDelay(float duration) {
            fullAmpTime = duration;
        }

        public ADSR getFinalADSR(float amplitude) {

            return new ADSR(amplitude, fAdsrAttack, fAdsrDelay, amplitude, fAdsrRelease);
        }

        @Override
        public void increaseModulatorFactor(int value) {
            int modPower = (int) (Math.log(modulatorFrequencyAmp) / Math.log(2));
            modPower = Math.min(modPower + value, 8);
            modulatorFrequencyAmp = (float) Math.pow(2f, modPower);
        }

        @Override
        public void decreaseModulatorFactor(int value) {
            int modPower = (int) (Math.log(modulatorFrequencyAmp) / Math.log(2));
            modulatorFrequencyAmp = (float) Math.pow(2f, modPower - value);

        }

        @Override
        public float fAdsrRelease() {
            return fAdsrRelease;
        }

        @Override
        public boolean hasMoog() {
            return hasMoog;
        }

        @Override
        public void setHasMoog(boolean moog) {
            this.hasMoog = true;
        }

        @Override
        public float getMoogFrequency() {
            return moogFrequency;
        }

        @Override
        public void reverseADSR() {
            float tmp = fAdsrAttack;
            fAdsrAttack = fAdsrRelease;
            fAdsrRelease = tmp;

        }

        @Override
        public void activateAmpLine(float dur, Multiplier ml) {
            if (currentLineAmp < 1 && fullAmpTime > 0) {
                Line ampLine = new Line();

                ampLine.activate(dur, currentLineAmp, currentLineAmp + dur / fullAmpTime);
                ampLine.patch(ml.amplitude);
                currentLineAmp = currentLineAmp + dur / fullAmpTime;
            }

        }


        @Override
        public String toString() {
            return "BaseTemplate{" +
                   "moogFactor=" + moogFactor +
                   ", targetMoogFactor=" + targetMoogFactor +
                   ", moogFrequency=" + moogFrequency +
                   ", hasMoog=" + hasMoog +
                   ", fAdsrAttack=" + fAdsrAttack +
                   ", fAdsrDelay=" + fAdsrDelay +
                   ", fAdsrRelease=" + fAdsrRelease +
                   '}';
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

        public AudioOutput out;
        public Sink             sink             = xpy.sound_flock.Maestro.sink;
        public EnvelopeFollower envelopeFollower = new EnvelopeFollower(0, .2f, 64);
        public Multiplier       finalMultiplier  = new Multiplier(1);
        public MoogFilter moogFilter;
        public ADSR       finalADSR;
        public ADSR       finalADSR2;
        public UGen       preFinalUgen;

        @Override
        public Sink getSink() {
            return sink;
        }

        @Override
        public EnvelopeFollower getEnvFollower() {
            return envelopeFollower;
        }

        @Override
        public void setMoog(MoogFilter moog) {
            this.moogFilter = moog;
            getTemplate().setHasMoog(true);
        }

        @Override
        public boolean isComplete() {
            return isComplete && System.currentTimeMillis() > completesAt;
        }

        @Override
        public void noteOn(float dur) {
            patch(preFinalUgen, dur);
        }

        public float normalizePitch(float pitch) {
            return Math.max(minPitch, Math.min(pitch, maxPitch));
        }

        @Override
        public void noteOff() {
            setComplete();
            finalADSR.unpatchAfterRelease(out);
            // finalADSR2.unpatchAfterRelease(envelopeFollower);
            finalADSR.noteOff();
            // finalADSR2.noteOff();


        }


        public void setComplete() {
            isComplete = true;
            isPlaying = false;
            completesAt = System.currentTimeMillis() + ((long) (getTemplate().fAdsrRelease() * 1000));
        }

        public void patch(UGen uGen, float duration) {

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
            getTemplate().activateAmpLine(duration + getTemplate().fAdsrRelease(), finalMultiplier);
            finalADSR = getTemplate().getFinalADSR(this.amplitude);
            // finalADSR2 = getTemplate().getFinalADSR(this.amplitude);

            finalADSR.patch(envelopeFollower).patch(sink);
            lastUgen.patch(finalMultiplier).patch(finalADSR).patch(out);
            // finalADSR2.patch(envelopeFollower).patch(sink);

            finalADSR.noteOn();
            // finalADSR2.noteOn();
            isPlaying = true;

        }

        public void unpatch() {
//            sink.unpatch(out);
            envelopeFollower.unpatch(sink);
        }

        public float amplitudeByFrequency(float amplitude, float frequency) {

   /*         if (frequency > 0 && frequency < 500) {
                amplitude = amplitude *(.6f + .4f* (frequency / 500));
            } else if (frequency >= 500) {
                amplitude = amplitude * (.6f + .4f* (1 - (frequency-500) /5000));

            }
 */           return amplitude;
        }
    }
}
