package xpy.sound_flock;

import java.util.*;
import processing.core.*;

/**
 * Tempo
 * Created by xpy on 05-Sep-15.
 */

interface BitListener {
    void tick();
}

public class Tempo extends PApplet {
    int bpm;
    int bitms;
    long lastEvent;
//    long period;

    private List<BitListener> listeners = new ArrayList<>();
    Tempo(int bpm) {

        this.bpm = bpm;
        this.bitms = (60 * 1000) / bpm;
        lastEvent = System.currentTimeMillis();
    }

    public void addListener(BitListener toAdd) {
        listeners.add(toAdd);
    }

    public void update(){

        long now=System.currentTimeMillis();
      //  println((now-lastEvent) - this.bitms);

        if (now -lastEvent>=this.bitms) {
            println((now-lastEvent) - this.bitms);

            lastEvent=now;
            for(BitListener l : listeners) {
                l.tick();
//                println("TICKKKK!!!");
            }

        }
    }

    public void run(){
        update();
    }


    public void start(){

        new Timer().schedule(new TimerTask() {
            public void run()  {
                // do stuff
                update();
            }
        }, 0, 1);
    }

    public boolean bit() {

        println(millis() % this.bitms);
        return millis() % this.bitms == 0;
    }
}
