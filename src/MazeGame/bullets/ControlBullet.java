package MazeGame.bullets;

import MazeGame.creature.Creature;
import MazeGame.creature.Enemy;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ControlBullet extends Bullet {

    private CopyOnWriteArrayList<Creature> controlList;

    public ControlBullet(double x, double y, double xDir, double yDir, double speed, Color color, int damage, int belongTeam, CopyOnWriteArrayList<Effect> effects, CopyOnWriteArrayList<Creature> controlList) {
        super(x, y, xDir, yDir, speed, color, damage, belongTeam, effects);
        this.controlList = controlList;
    }

    @Override
    public void hurt(Creature creature) {
        creature.takeDamage(getDamage());
        if (creature instanceof Enemy) {
            ((Enemy) creature).betray(getBelongTeam());
            controlList.add(creature);
        }
    }

    @Override
    public void dieEffect() {

    }

    @Override
    public Bullet copy(double x, double y, double xDir, double yDir, double speed, int damage) {
        return new ControlBullet(x, y, xDir, yDir, speed, color, damage, belongTeam, effects, controlList);
    }
}
