package xpy.sound_flock.Body;

import processing.core.PApplet;
import xpy.sound_flock.Maestro;
import xpy.sound_flock.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TriangleBody
 * Created by xpy on 10-Nov-15.
 */
public class TriangleBody implements Body {
    public PApplet pa;

    private Phrase phrase;

    private List<Member> members = new ArrayList<>();

    public static float nextX;
    public static float nextY;
    public static int maxSteps   = 1;
    public static int numOfSteps = 0;
    public static int dir        = 0;
    public static int dirChange  = 0;
    public float x;
    public float y;
    public float red;
    public float green;
    public float blue;

    public float size = 50f;

    public TriangleBody(PApplet pa) {
        this.pa = pa;
//        this.size = 5f;
        Random r = new Random();
        red = r.nextInt(255);
        green = r.nextInt(255);
        blue = r.nextInt(255);

    }

    public void update() {

        for (Member member : members) {
            member.update(this);
        }

    }

    public void attachMember(Member member) {
        if (member.getNote().pitch > 0)
            setMember(member);
        Random r = new Random();
        if (members.size() > 0)
            members.add(r.nextInt(members.size()), member);
        else
            members.add(member);
    }

    public void setMember(Member member) {
        member.setX(nextX );
        member.setY(nextY );
        member.setColor(pa.color(red, green, blue));

        switch (dir) {
            case 0:
                nextX += size;
                break;
            case 1:
                nextY -= size;
                break;
            case 2:
                nextX -= size;
                break;
            case 3:
                nextY += size;
                break;
        }
        numOfSteps++;
        if (numOfSteps == maxSteps) {
            dir = dir == 3 ? 0 : dir + 1;
            numOfSteps = 0;
            dirChange++;
        }
        if (dirChange == 2) {
            dirChange = 0;
            maxSteps++;
        }

    }

    @Override
    public void attachPhrase(Phrase phrase) {

        this.phrase = phrase;
        Random r = new Random();
        for (int i = 0; i < this.phrase.notes.size(); i++) {
            attachMember(new TriangleMember(pa, this.phrase.notes.get(i)));
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
