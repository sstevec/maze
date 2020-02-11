package MazeGame.bullets;

import MazeGame.Creature;
import MazeGame.Enemy;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ControlBullet extends Bullet {

    private CopyOnWriteArrayList<Creature> controlList;

    public ControlBullet(double x, double y, double xDir, double yDir, int speed, Color color, int damage, int belongTeam, CopyOnWriteArrayList<Effect> effects, CopyOnWriteArrayList<Creature> controlList){
        super(x,y,xDir,yDir,speed,color,damage,belongTeam,effects);
        this.controlList = controlList;
    }
    @Override
    public void hurt(Creature creature) {
        creature.takeDamage(1);
        if(creature instanceof Enemy){
            ((Enemy) creature).betray(getBelongTeam());
            controlList.add(creature);
        }
    }

    @Override
    public void dieEffect() {

    }
}
