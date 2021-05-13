package MazeGame.buffs;

import MazeGame.Creature;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Buff {
    private Creature target;
    private int lastTimeInMs;
    private String name;
    private Timer timer = new Timer();

    public Buff(Creature creature, int lastTimeInMs, String name){
        this.target = creature;
        this.lastTimeInMs = lastTimeInMs;
        this.name = name;
    }

    // child call this to start the buff
    public void startTimer(){
        affect(target);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                endBuff();
            }
        },lastTimeInMs);
    }

    // child call this to cancel the buff
    public void cancelBuff(){
        timer.cancel();
        endAffect(target);
    }

    // child call this when buff ends
    private void endBuff(){
        endAffect(target);
        target.removeBuff(this);
    }


    /***
     *
     * Child should implement these two functions to get the effect they want
     * However, child should not call these two functions to start or end the buff
     *
     */
    // child implement this to get whatever the buff effect they want
    abstract void affect(Creature target);

    // child implement this to clean whatever they create when buff ends
    abstract void endAffect(Creature target);

    public String getName() {
        return name;
    }
}
