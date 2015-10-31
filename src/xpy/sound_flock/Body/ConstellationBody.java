package xpy.sound_flock.Body;

import Constellation.Constellation;
import processing.core.PApplet;
import xpy.sound_flock.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ConstellationBody
 * Created by xpy on 24-Oct-15.
 */
public class ConstellationBody implements Body {

    public Constellation constellation;

    private Phrase phrase;

    private List<Member> members = new ArrayList<>();

    public float   x;
    public float   y;
    public PApplet pa;


    public ConstellationBody(PApplet pa) {
        constellation = new Constellation(pa);
        this.pa = pa;
    }

    @Override
    public void update() {
        constellation.x = pa.width / 2;
        constellation.y = pa.height / 2;
        for (int i = 0; i < members.size(); i++) {
            members.get(i).update(this);
        }
        constellation.run();
    }

    public void attachMember(Member member) {

        Random r = new Random();
        if (members.size() > 0)
            members.add(r.nextInt(members.size()), member);
        else
            members.add(member);

    }


    @Override
    public void attachPhrase(Phrase phrase) {

        this.phrase = phrase;
        for (int i = 0; i < this.phrase.notes.size(); i++) {
            ConstellationMember cm = new ConstellationMember(pa, this.phrase.notes.get(i));
            attachMember(cm);
            constellation.addNode(cm.node);
        }
    }

    @Override
    public Phrase getPhrase() {
        return phrase;
    }


    public List<Member> getMembers() {
        return members;
    }

}
