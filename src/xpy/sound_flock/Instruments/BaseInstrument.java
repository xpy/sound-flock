package xpy.sound_flock.Instruments;

import ddf.minim.AudioOutput;
import ddf.minim.UGen;
import ddf.minim.ugens.EnvelopeFollower;
import ddf.minim.ugens.Sink;

/**
 * BaseInstrument
 * Created by xpy on 30-Sep-15.
 */
abstract class BaseInstrument implements InstrumentGenerator.Instrument {

    public float frequency;
    public float amplitude;

    protected boolean isComplete                 = false;
    protected long    completesAt                = 0;
    protected float   releaseTime                = .5f;
    protected int     envelopeFollowerBufferSize = 256;
    public AudioOutput out;
    public Sink             sink             = new Sink();
    public EnvelopeFollower envelopeFollower = new EnvelopeFollower(0, .2f, 256);

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
        completesAt = System.currentTimeMillis() + ((long) (releaseTime * 1000));
    }

    public void patch (UGen uGen, float duration) {
        envelopeFollower = new EnvelopeFollower(0, duration, envelopeFollowerBufferSize);
        uGen.patch(envelopeFollower).patch(sink).patch(out);
        uGen.patch(out);
    }

    public void unpatch(){
        sink.unpatch(out);
        envelopeFollower.unpatch(sink);

    }
}
