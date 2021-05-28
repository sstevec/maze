package MazeGame.bullets;

import MazeGame.creature.Creature;
import MazeGame.effect.Effect;
import MazeGame.effect.Explosion;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.helper.Info.cellWidth;

public class ExplosiveBullet extends Bullet {

    private int effectRadius;
    private boolean breakable;

    public ExplosiveBullet(double x, double y, double xDir, double yDir, double speed, Color color, int damage,int effectRadius, boolean breakable, int belongTeam, CopyOnWriteArrayList<Effect> effects){
        super(x,y,xDir,yDir,speed,color,damage,belongTeam, effects);
        this.effectRadius = effectRadius;
        this.breakable = breakable;
    }
    @Override
    public void hurt(Creature creature) {
        effects.add(new Explosion((int)x, (int)y,effectRadius*cellWidth,effectRadius,getDamage(),breakable,creatures));
    }

    @Override
    public void dieEffect() {
        effects.add(new Explosion((int)x, (int)y,25+effectRadius*cellWidth,effectRadius,getDamage(),breakable,creatures));
    }

    @Override
    public Bullet copy(double x, double y, double xDir, double yDir, double speed, int damage){
        return new ExplosiveBullet(x, y, xDir, yDir, speed, color, damage, effectRadius, breakable, belongTeam, effects);
    }
}
