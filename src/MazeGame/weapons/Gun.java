package MazeGame.weapons;

import MazeGame.creature.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;
import MazeGame.helper.FireData;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Gun extends Weapon {

    public Gun() {
        super("Gun", 5.0, 10, null, 20, 0, 5, null, null);
    }

    public Gun(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user) {
        super("Gun", 5.0, 10, color, 20, belongTeam, 5, effects, user);
    }

    @Override
    protected void fire(FireData fireData) {
        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new NormalBullet(fireData.getUserX(), fireData.getUserY(), fireData.getxDir(),
                fireData.getyDir(), bulletSpeed * fireData.getBulletSpeedFactor(), color,
                (int) Math.ceil(damage * fireData.getBulletDamageFactor()),
                belongTeam * fireData.getBelongTeamFactor(), effects));
        fireData.setBullets(bullets);
    }

    @Override
    protected void cast(double x, double y, double xDest, double yDest) {
        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        double xDir = (xDest - x) / dist;
        double yDir = (yDest - y) / dist;

        ArrayList<Bullet> bullets = new ArrayList<>();

        bullets.add(new NormalBullet(x, y, xDir, yDir, bulletSpeed * 2, color, damage * 3, belongTeam, effects));

        user.addBullets(bullets);
    }

    class GunFactory implements WeaponInstance<Gun> {
        @Override
        public Gun create() {
            return new Gun();
        }
    }
}
