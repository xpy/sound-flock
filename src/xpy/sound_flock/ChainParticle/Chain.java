package xpy.sound_flock.ChainParticle;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Chain
 * Created by xpy on 11-Nov-15.
 */
public class Chain {

    public List<ChainParticle> particles     = new ArrayList<>();
    public List<ChainParticle> deadParticles = new ArrayList<>();
    PApplet pa;
    public int color;

    public float x;
    public float y;
    public float speed;
    public float yBounce = 0;
    public float maxY    = 100;

    public static final int P_ELLIPSE  = 0;
    public static final int P_TRIANGLE = 1;
    public static final int P_SQUARE   = 2;

    public int shape;
    private int removeTime      = 60;
    private int removeTimeIndex = 0;

    public Chain(PApplet pa, float x, float y) {
        Random r = new Random();
        this.pa = pa;
        this.x = x;
        this.y = y;
        this.speed = (r.nextFloat() * -5) - 5;
        this.shape = r.nextInt(3);

        color = pa.color(r.nextInt(256), r.nextInt(256), r.nextInt(256));

    }

    public synchronized ChainParticle addParticle() {
        ChainParticle particle = new ChainParticle(pa, 0, (float) (maxY * Math.sin(yBounce)));
        addParticle(particle);
        return particle;
    }

    public void addParticle(ChainParticle particle) {
        this.particles.add(particle);
    }

    public void removeParticle(ChainParticle particle) {
        this.particles.remove(particle);
    }

    public synchronized void run() {
        this.update();
        for (ChainParticle particle : particles) {
            particle.run(this);
            if (particle.size <= 0 || particle.getX(this) < -100) {
                deadParticles.add(particle);
            }
        }
        if (removeTime == removeTimeIndex++) {
            removeTimeIndex = 0;
            deadParticles.forEach(this::removeParticle);
            deadParticles.clear();
        }
    }

    public void update() {

        yBounce += .025f;
    }

    public float getX() {
        return this.pa.width * .875f;

    }

    public float getY() {
        return this.y = this.pa.height / 2;


    }

}
