package xpy.sound_flock.Body;

import processing.core.*;
import xpy.sound_flock.Instruments.InstrumentGenerator;
import xpy.sound_flock.Phrase;

import java.util.ArrayList;
import java.util.List;

/**
 * CircleBody
 * Created by xpy on 29-Sep-15.
 */
public class CircleBody extends PApplet implements Body {

    PApplet pa;

    private Phrase                         phrase;
    private InstrumentGenerator.Instrument instrument;
    private List<Member> members = new ArrayList<>();

    public CircleBody (PApplet pa) {
        this.pa = pa;
    }

    public CircleBody (PApplet pa, Phrase phrase, InstrumentGenerator.Instrument instrument) {
        this.phrase = phrase;
        this.pa = pa;
        this.instrument = instrument;


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
        for (int i = 0; i < this.phrase.notes.size(); i++) {
            attachMember(new CircleMember(pa, this.phrase.notes.get(i), this.instrument));
        }

        for (int i = 0; i < members.size(); i++) {
            members.get(i).setX(50 + i * 50);
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
