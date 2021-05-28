package MazeGame.bullets;

import MazeGame.creature.Creature;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class NormalBullet extends Bullet {

    public NormalBullet(double x, double y, double xDir, double yDir, double speed, Color color, int damage, int belongTeam, CopyOnWriteArrayList<Effect> effects){
        super(x,y,xDir,yDir,speed,color,damage,belongTeam, effects);
    }
    @Override
    public void hurt(Creature creature) {
        creature.takeDamage(getDamage());
    }

    @Override
    public void dieEffect() {

    }

    @Override
    public Bullet copy(double x, double y, double xDir, double yDir, double speed, int damage) {
        return new NormalBullet(x, y, xDir, yDir, speed, color, damage, belongTeam, effects);
    }
}
