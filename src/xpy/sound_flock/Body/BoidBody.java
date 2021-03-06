package xpy.sound_flock.Body;

import Boids.Boid.Boid;
import Boids.Flock.Flock;
import processing.core.PApplet;
import xpy.sound_flock.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BoidBody
 * Created by xpy on 20-Oct-15.
 */
public class BoidBody extends Flock implements Body {
    public PApplet pa;

    private Phrase phrase;

    private List<BoidMember> members = new ArrayList<>();

    public float x;
    public float y;

    public BoidBody(PApplet pa) {
        super(pa);
        this.pa = pa;
//        this.size = 5f;
        Random r = new Random();
        this.x = r.nextFloat() * (this.pa.width - 100) + 50;
        this.y = r.nextFloat() * (this.pa.height - 100) + 50;
        red = r.nextInt(100) + 50;
        green = r.nextInt(100) + 50;
        blue = r.nextInt(100) + 50;
        neighborDist = 100;
        separationBurstReduce = 4;
        cohesionFactor = .5f;
    }


    public void update() {

        for (BoidMember b : members) {
            b._update(this);  // Passing the entire list of boids to each boid individually
        }
        normalize();

    }

    @Override
    public List<Boid> getBoids() {
        List<Boid> boids = new ArrayList<>();
        for (Member m : members) {
            for (Boid b : ((BoidMember) m).getBoids())
                boids.add(b);
        }
        return boids;
    }

    public void attachMember(Member member) {
        this.members.add((BoidMember) member);
    }

    @Override
    public void attachPhrase(Phrase phrase) {

        this.phrase = phrase;
        Random r     = new Random();
        int    color = pa.color(r.nextInt(256), r.nextInt(256), r.nextInt(256), 100);
        for (int i = 0; i < this.phrase.notes.size(); i++) {
            attachMember(new BoidMember(pa, this.phrase.notes.get(i)));
        }

        for (int i = 0; i < members.size(); i++) {
            members.get(i).setX(x);
            members.get(i).setY(y);
        }

    }

    @Override
    public Phrase getPhrase() {
        return phrase;
    }

    @Override
    public List<Member> getMembers() {
        return (List<Member>) (List<?>) members;
    }
}
