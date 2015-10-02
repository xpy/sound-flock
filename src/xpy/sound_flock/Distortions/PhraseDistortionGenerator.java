package xpy.sound_flock.Distortions;

import xpy.sound_flock.Phrase;

/**
 * PhraseDistortionGenerator
 * Created by xpy on 01-Oct-15.
 */
public class PhraseDistortionGenerator {

    public static final int DISTORTION_TONE_FULL    = 0;
    public static final int DISTORTION_TONE_PARTIAL = 1;


    public static void full_tone_distortion () {

    }

    public static Distortion createDistortion (int distortion, Phrase phrase) {

        switch (distortion) {
            case (DISTORTION_TONE_FULL):
                return new FullToneDistortion(phrase);
            case (DISTORTION_TONE_PARTIAL):
                return new PartialToneDistortion(phrase);
            default:
                return new FullToneDistortion(phrase);
        }
    }
}
