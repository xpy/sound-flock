package xpy.sound_flock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SynthInstrumentTemplate
 * Created by xpy on 23-Sep-15.
 */
public class SynthInstrumentTemplate {

    List<Float>   oscillatorFrequencyFactor = new ArrayList<>();
    List<Integer> oscillatorWave            = new ArrayList<>();

    int modulatorWaveTable;

    int numOfOScillators;

    SynthInstrumentTemplate () {
        Random r = new Random();

        this.numOfOScillators = r.nextInt(3) + 1;
//        this.baseFrequency = frequency;
//        this.initialFrequency = baseFrequency;
//        this.amplitude = amplitude;

        this.modulatorWaveTable = r.nextInt(4);

        for (int i = 1; i <= this.numOfOScillators; i++) {
            oscillatorFrequencyFactor.add((r.nextInt(i + 1) + i) * .25f);
            oscillatorWave.add(r.nextInt(4));
        }


    }
}
