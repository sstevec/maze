package MazeGame.bullets;

import MazeGame.creature.Creature;
import MazeGame.buffs.FireBuff;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class FireBullet extends Bullet {

    public FireBullet(double x, double y, double xDir, double yDir, double speed, Color color, int damage, int belongTeam, CopyOnWriteArrayList<Effect> effects) {
        super(x, y, xDir, yDir, speed, color, damage, belongTeam, effects);
    }

    @Override
    public void hurt(Creature creature) {
        creature.takeDamage(getDamage());
        FireBuff fireBuff = new FireBuff(creature, 3 * 1000, 1);
        creature.addBuff(fireBuff);
    }

    @Override
    public void dieEffect() {

    }

    @Override
    public Bullet copy(double x, double y, double xDir, double yDir, double speed, int damage) {
        return new FireBullet(x, y, xDir, yDir, speed, color, damage, belongTeam, effects);
    }
}
