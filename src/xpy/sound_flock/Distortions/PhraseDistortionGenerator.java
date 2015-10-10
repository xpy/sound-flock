package xpy.sound_flock.Distortions;

import xpy.sound_flock.Blibliki;
import xpy.sound_flock.Instruments.BaseInstrumentGenerator;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Phrase;

import java.util.Random;

/**
 * PhraseDistortionGenerator
 * Created by xpy on 01-Oct-15.
 */
public class PhraseDistortionGenerator {

    public static final int DISTORTION_TONE_FULL    = 0;
    public static final int DISTORTION_TONE_PARTIAL = 1;
    public static final int DISTORTION_MOOG         = 2;
    public static final int DISTORTION_TONE_PARTIAL_RANDOM = 3;



    public static void full_tone_distortion() {

    }

    public static Distortion createDistortion(int distortion, Blibliki blibliki) {

        switch (distortion) {
            case (DISTORTION_TONE_FULL):
                return new FullToneDistortion(blibliki.getPhrase());
            case (DISTORTION_TONE_PARTIAL):
                return new PartialToneDistortion(blibliki.getPhrase());
            case (DISTORTION_TONE_PARTIAL_RANDOM):
                Random r = new Random();
                return new PartialToneDistortion(blibliki.getPhrase(),r.nextInt(3),r.nextInt(3),r.nextInt(blibliki.getPhrase().numOfNotes));
            case (DISTORTION_MOOG):
                return new MoogFactorDistortion(blibliki.instrumentGenerator);
            default:
                return new FullToneDistortion(blibliki.getPhrase());
        }
    }

    public static Distortion createRandomDistortion(Blibliki blibliki) {
        return createDistortion((new Random()).nextInt(3), blibliki);
    }

    public static Distortion createRandomPartialToneDistortion(Phrase phrase) {
        Random r = new Random();
        return new PartialToneDistortion(phrase, r.nextInt(3), r.nextInt(2)+1, r.nextInt(phrase.numOfNotes - 1) + 1);
    }

    public static Distortion createMoogDistortion(BaseInstrumentGenerator instrumentGenerator) {
        return new MoogFactorDistortion(instrumentGenerator);
    }

    public static Distortion createADSRReverseDistortion(BaseInstrumentGenerator instrumentGenerator) {
        return new ADSRReverseDistortion(instrumentGenerator);
    }

  /*  public static Distortion bliblikiDistortion(Blibliki blibliki) {


    }*/

}
