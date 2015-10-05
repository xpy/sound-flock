package xpy.sound_flock.Body;

import processing.core.*;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * CircleBody
 * Created by xpy on 29-Sep-15.
 */
public class CircleBody extends PApplet implements Body {

    PApplet pa;

    private Phrase                         phrase;

    private List<Member> members = new ArrayList<>();

    public float x;
    public float y;

    public CircleBody (PApplet pa) {
        this.pa = pa;
        Random r = new Random();
        this.x = r.nextFloat() * (this.pa.width - 100) + 50;
        this.y = r.nextFloat() * (this.pa.height - 100) + 50;

//        println(x);
//        println(y);

    }


    public void update () {

        for (int i = 0; i < members.size(); i++) {
            members.get(i).update();
        }

    }


    public void attachMember (Member member) {
        this.members.add(member);
    }

    @Override
    public void attachPhrase (Phrase phrase) {

        this.phrase = phrase;
        Random r     = new Random();
        int    color = pa.color(r.nextInt(256), r.nextInt(256), r.nextInt(256),100);
        for (int i = 0; i < this.phrase.notes.size(); i++) {
            attachMember(new CircleMember(pa, this.phrase.notes.get(i)));
        }

        for (int i = 0; i < members.size(); i++) {
            members.get(i).setX(this.x + r.nextFloat() * 100);
            members.get(i).setY(this.y + r.nextFloat() * 100);
            members.get(i).setColor(color);
        }

    }

    @Override
    public Phrase getPhrase () {
        return phrase;
    }

    @Override
    public List<Member> getMembers () {
        return members;
    }


}
