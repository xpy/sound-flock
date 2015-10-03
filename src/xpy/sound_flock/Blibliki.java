package xpy.sound_flock;

import ddf.minim.AudioOutput;
import processing.core.*;
import xpy.sound_flock.Body.Body;
import xpy.sound_flock.Body.Member;
import xpy.sound_flock.Distortions.Distortion;
import xpy.sound_flock.Instruments.BaseInstrument;
import xpy.sound_flock.Instruments.BaseInstrumentGenerator;
import xpy.sound_flock.Instruments.InstrumentGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Blibliki
 * Created by xpy on 05-Sep-15.
 */
public class Blibliki extends PApplet/* implements BitListener*/ {

    private AudioOutput             out;
    public BaseInstrumentGenerator instrumentGenerator;
    private Phrase                  phrase;
    private Body                    body;

    private List<Distortion> distortions = new ArrayList<>();

    private List<LoopEvent> loopEvents = new ArrayList<>();

    public int loops = 0;
    private long nextCheck;
    private long beatTime = (long) (60000f / 120f);

    private boolean hasBody = false;

    public Blibliki (Phrase phrase, BaseInstrumentGenerator instrumentGenerator, Body body, AudioOutput out) {

        this.addPhrase(phrase);
        this.addBody(body);
        this.out = out;
        this.instrumentGenerator = instrumentGenerator;
    }

/*
    public static Blibliki createRandomBlibliki (AudioOutput out) {

        Blibliki ret = new Blibliki(out);
        Phrase   p   = new Phrase();
        p.randomizePhrase();
        ret.addPhrase(p);
        return ret;
    }
*/

    public void addPhrase (Phrase phraseToAdd) {

        phrase = phraseToAdd;
    }

    public void addBody (Body bodyToAdd) {

        body = bodyToAdd;
        body.attachPhrase(this.phrase);

        hasBody = true;
    }

    public void setNotes () {
        out.pauseNotes();
        float i = 0;

        for (LoopEvent loopEvent : loopEvents) {
            loopEvent.fire(loops);
        }
//            offset += offsetFlag;
//            offsetFlag *= -1;
        if (hasBody)
            for (Member member : body.getMembers()) {

                BaseInstrument instrument = instrumentGenerator.createInstrument(member.getNote().pitch, instrumentGenerator.getAmplitude(), out);
                member.attachInstrument(instrument);
                out.playNoteAtBeat(phrase.getPhraseMeters(), i, Math.min(member.getNote().duration, instrumentGenerator.getMaxDuration()), instrument);
                i += member.getNote().duration;
            }
        else
            for (Note note : phrase.notes) {
                InstrumentGenerator.Instrument instrument = instrumentGenerator.createInstrument(note.pitch, instrumentGenerator.getAmplitude(), out);
                out.playNoteAtBeat(phrase.getPhraseMeters(), i, Math.min(note.duration, instrumentGenerator.getMaxDuration()), instrument);
                i += note.duration;
            }
        out.resumeNotes();
    }


    public void update () {

        if (System.currentTimeMillis() - nextCheck >= 0 && loops < 100) {
            println("loops:" + loops);
            setNotes();
            loops++;
            nextCheck = System.currentTimeMillis() + out.nextMeterStart(phrase.getPhraseMeters()) + 100;
        }
        if (hasBody)
            body.update();
    }

    public float millisToBeats (long millis) {
        return ((float) millis / (float) beatTime);
    }

    public void start () {
        nextCheck = System.currentTimeMillis() + out.nextMeterStart(phrase.meterLength) + 100;
    }

    public void resetPhrase () {
        phrase.reset();
    }

    public void addDistortion (Distortion distortion) {
        distortions.add(distortion);
    }

    public void applyDistortion (int index) {
        distortions.get(index).apply();
    }

    public void revertDistortion (int index) {
        distortions.get(index).revert();
    }

    public void addLoopEvent (LoopEvent loopEvent) {
        loopEvents.add(loopEvent);
    }

    public Distortion getDistortion (int index) {
        return distortions.get(index);
    }

    public static class LoopEvent {
        public void fire (int loopNum) {

        }
    }
}
