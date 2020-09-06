package MazeGame.buffs;

import MazeGame.Creature;

import java.util.Timer;
import java.util.TimerTask;

public class FireBuff extends Buff {

    private int damage;
    private Timer timer = new Timer();

    public FireBuff(Creature creature, int lastTimeInMs, int damage){
        super(creature, lastTimeInMs, "fire");
        this.damage = damage;
        startTimer();
    }

    @Override
    void affect(Creature target) {
       timer.schedule(new TimerTask() {
           @Override
           public void run() {
               target.takeDamage(damage);
           }
       },0,100);
    }

    @Override
    void endAffect(Creature target) {
       timer.cancel();
    }
}