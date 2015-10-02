package xpy.sound_flock.Distortions;

import xpy.sound_flock.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PartialToneDistortion
 * Created by xpy on 01-Oct-15.
 */
public class PartialToneDistortion implements Distortion {


    private List<Integer> noteIndexes = new ArrayList<>();
    private List<Integer> tuneAmounts = new ArrayList<>();

    private int    numOfDistortions;
    private Phrase phrase;

    public PartialToneDistortion (Phrase phrase) {

        Random r = new Random();

        this.phrase = phrase;

        numOfDistortions = r.nextInt(this.phrase.numOfNotes) + 1;
        for (int i = 0; i < numOfDistortions; i++) {
            int noteIndex;
            do {
                noteIndex = r.nextInt(this.phrase.numOfNotes);
            } while (noteIndexes.contains(noteIndex));
            noteIndexes.add(noteIndex);
            tuneAmounts.add(r.nextInt(9) - 4);
        }
    }

    @Override
    public void apply () {
        phrase.tune(noteIndexes.toArray(new Integer[noteIndexes.size()]), tuneAmounts.toArray(new Integer[tuneAmounts.size()]));
    }

    @Override
    public void revert () {
        Integer[] deTuneAmounts = new Integer[tuneAmounts.size()];
        for (int i = 0; i < tuneAmounts.size(); i++) {
            deTuneAmounts[i] = tuneAmounts.get(i) * -1;
        }

        phrase.tune(noteIndexes.toArray(new Integer[noteIndexes.size()]), deTuneAmounts);

    }
}
