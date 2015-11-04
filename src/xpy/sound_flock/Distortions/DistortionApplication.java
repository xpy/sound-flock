package xpy.sound_flock.Distortions;

import java.util.Random;

/**
 * DistortionApplication
 * Created by xpy on 14-Oct-15.
 */
public class DistortionApplication {

    public Distortion distortion;
    public int applications    = 0;
    public int maxApplications = 1;
    public int period;
    public boolean peaked = false;
    public int applicationMode;

    public static final int AM_cycle = 0;
    public static final int AM_reset = 1;

    public DistortionApplication(Distortion distortion) {
        Random r = new Random();

        this.distortion = distortion;
        this.period = (int) Math.pow(2, r.nextInt(2));
        this.maxApplications = (int) Math.pow(2, r.nextInt(3));
        this.applicationMode = r.nextInt(2);
    }

    public void applyDistortion() {
        distortion.apply();
        applications++;
    }

    public void revertDistortion() {
        distortion.revert();
        applications--;
    }

    public void handleDistortion() {

        if (applicationMode == AM_cycle) {
            if (applications < maxApplications && !peaked) {
                applyDistortion();
                peaked = applications == maxApplications;

            } else if (applications > 0) {
                revertDistortion();
                peaked = applications != 0;
            }
        } else if (applicationMode == AM_reset) {
            if (applications < maxApplications) {
                applyDistortion();
            } else if (applications == maxApplications) {
                for (int i = 0; i <= maxApplications; i++) {
                    revertDistortion();
                }
            }
        }
    }
}
