package xpy.sound_flock;

/**
 * BliblikiRuler
 * Created by xpy on 07-Oct-15.
 */
public class BliblikiRuler {

    public int blibliki;
    public int numOfInstances = 0;
    public int maxInstances;
    public int numOfPreludeInstances = 0;
    public int maxPreludeInstances;
    public int preluder;
    public int leaver;
    public int returner;

    public BliblikiRuler(int blibkiki, int maxInstances, int maxPreludeInstances, int preluder, int leaver, int returner) {
        this.blibliki = blibkiki;
        this.maxInstances = maxInstances;
        this.maxPreludeInstances = maxPreludeInstances;
        this.preluder = preluder;
        this.leaver = leaver;
        this.returner = returner;
    }

    public boolean canPrelude() {
        return numOfInstances < maxInstances && numOfPreludeInstances < maxPreludeInstances;
    }

    public boolean hasSpace() {
        return numOfInstances < maxInstances;
    }
}
