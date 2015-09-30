package xpy.sound_flock.Body;

import xpy.sound_flock.Phrase;

import java.util.List;

/**
 * Body
 * Created by xpy on 29-Sep-15.
 */
public interface Body {


    void update ();

    void attachMember (Member member);

    void attachPhrase (Phrase phrase);

    Phrase getPhrase ();

    List<Member> getMembers ();
}
