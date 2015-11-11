package xpy.sound_flock.Body;

import processing.core.PApplet;
import xpy.sound_flock.ChainParticle.Chain;
import xpy.sound_flock.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ChainBody
 * Created by xpy on 11-Nov-15.
 */
public class ChainBody implements Body {

    PApplet pa;
    private Phrase phrase;
    private List<Member> members = new ArrayList<>();
    public Chain chain;
    public float x;
    public float y;

    public ChainBody(PApplet pa) {
        this.pa = pa;
        Random r = new Random();
        this.x = this.pa.width - 100;
        this.y = this.pa.height /2;
        chain = new Chain(pa, x, y);
    }

    public void update() {

        chain.run();
        for (int i = 0; i < members.size(); i++) {
            members.get(i).update(this);
        }

    }

    public void attachMember(Member member) {
        this.members.add(member);
    }

    @Override
    public void attachPhrase(Phrase phrase) {

        this.phrase = phrase;
        Random r = new Random();
        for (int i = 0; i < this.phrase.notes.size(); i++) {
            attachMember(new ChainMember(pa, this.phrase.notes.get(i), this));
        }


    }

    @Override
    public Phrase getPhrase() {
        return phrase;
    }

    @Override
    public List<Member> getMembers() {
        return members;
    }

}
