package xpy.sound_flock.ChainParticle;

import processing.core.PApplet;

import java.util.Random;

/**
 * ChainParticle
 * Created by xpy on 11-Nov-15.
 */
public class ChainParticle {

    PApplet pa;
    public float x;
    public float y;
    public float size;
    public float rotationSpeed;
    public float rotation   = 0;
    public float sizeChange = .5f;
    public float reachY;
    private int yRestoration = 2;
    private float bounceVolume = .25f;


    public ChainParticle(PApplet pa, float x, float y) {
        Random r = new Random();
        this.pa = pa;
        this.x = x;//+r.nextInt(pa.width/4)*-1;
        this.y = y;
        reachY = (r.nextFloat() * 150 + 10) * (r.nextBoolean() ? 1 : -1);
        rotationSpeed = r.nextFloat() * .05f - .025f;
        size = r.nextFloat() * 30f + 5f;
    }

    public void run(Chain chain) {
        this.render(chain);
        this.update(chain);
    }

    public void update(Chain chain) {

        x += chain.speed;
        rotation += rotationSpeed;
        size -= sizeChange;
        if (y != reachY) {
            float dif =  Math.abs(y - reachY)/10f;
            y += dif * (y < reachY ? 1 : -1);
        }/* else {
            reachY = (float) Math.floor(reachY * -bounceVolume);
        }*/
    }

    public void render(Chain chain) {
        pa.blendMode(PApplet.ADD);
        pa.fill(chain.color);
        pa.noStroke();
        pa.pushMatrix();
        pa.translate(getX(chain), getY(chain));
        pa.rotateZ(rotation);
//        pa.rect(size / -2, size / -2, size, size);
//        pa.ellipse(size / -2, size / -2, size, size);
        pa.triangle(size / -2, size / 2, 0, size/-2,size/2,size/2);
        pa.popMatrix();
    }

    public float getX(Chain chain) {
        return chain.getX() + x;

    }

    public float getY(Chain chain) {
        return chain.getY() + y;

    }
}
