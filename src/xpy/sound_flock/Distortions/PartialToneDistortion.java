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

    private static final int POSITION_RANDOM = 0;
    private static final int POSITION_START  = 1;
    private static final int POSITION_END    = 2;

    private static final int PATTERN_RANDOM = 0;
    private static final int PATTERN_UP     = 1;
    private static final int PATTERN_DOWN   = 2;

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

    public PartialToneDistortion (Phrase phrase, int position, int pattern, int num) {

        Random r = new Random();
        this.phrase = phrase;
        numOfDistortions = Math.min(this.phrase.numOfNotes, num);
        int start, end;
        switch (position) {
            default:
                start = 0;
                end = numOfDistortions;
                int noteIndex;
                for (int i = start; i < end; i++) {

                    do {
                        noteIndex = r.nextInt(this.phrase.numOfNotes);
                    } while (noteIndexes.contains(noteIndex));
                    noteIndexes.add(noteIndex);
                }
                break;
            case (POSITION_START):
                start = 0;
                end = numOfDistortions;
                for (int i = start; i < end; i++) noteIndexes.add(i);
                break;
            case (POSITION_END):
                start = numOfDistortions;
                end = this.phrase.numOfNotes;
                for (int i = start; i < end; i++) noteIndexes.add(i);
                break;
        }
        switch (pattern) {
            default:
                for (int i = start; i < end; i++) {
                    tuneAmounts.add(r.nextInt(9) - 4);
                }
                break;
            case (PATTERN_UP):
                for (int i = start; i < end; i++) {
                    tuneAmounts.add((r.nextInt(5) + 1) * -1);
                }

                break;
            case (PATTERN_DOWN):
                for (int i = start; i < end; i++) {
                    tuneAmounts.add((r.nextInt(5) + 1) * -1);
                }
                break;
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
