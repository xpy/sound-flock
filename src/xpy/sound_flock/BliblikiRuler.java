package xpy.sound_flock;

/**
 * BliblikiRuler
 * Created by xpy on 07-Oct-15.
 */
public class BliblikiRuler {

    public int instrument;
    public int numOfInstances;
    public int maxInstances;

    public BliblikiRuler (int instrument, int maxInstances) {
        this.instrument = instrument;
        this.maxInstances = maxInstances;
    }

    public boolean hasSpace(){
        return numOfInstances < maxInstances;
    }
}
