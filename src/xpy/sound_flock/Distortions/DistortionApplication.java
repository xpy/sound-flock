package xpy.sound_flock.Distortions;

import processing.core.PApplet;

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

    public DistortionApplication(Distortion distortion) {
        Random r = new Random();

        this.distortion = distortion;
        this.period = (int) Math.pow(2, r.nextInt(3));
        this.maxApplications  = r.nextInt(10)+1;
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

        if (applications < maxApplications && !peaked) {
            applyDistortion();
            peaked = applications == maxApplications;

        } else if (applications > 0) {
            revertDistortion();
            peaked = applications != 0;

        }
    }
}
