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

    public void startTimer(){
        affect(target);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                endBuff();
            }
        },lastTimeInMs);
    }

    public void cancelBuff(){
        timer.cancel();
        endAffect(target);
    }

    private void endBuff(){
        endAffect(target);
        target.removeBuff(this);
    }

    abstract void affect(Creature target);

    abstract void endAffect(Creature target);

    public String getName() {
        return name;
    }
}
