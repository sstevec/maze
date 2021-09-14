package MazeGame.weapons;

import MazeGame.creature.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.ExplosiveBullet;
import MazeGame.effect.Effect;
import MazeGame.helper.FireData;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Rocket extends Weapon {

    public Rocket() {
        super("Rocket", 0.5, 15, null, 100, 0, 5, null, null);
    }

    public Rocket(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user) {
        super("Rocket", 0.5, 15, color, 100, belongTeam, 5, effects, user);
    }

    @Override
    protected void fire(FireData fireData) {
        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new ExplosiveBullet(fireData.getUserX(), fireData.getUserY(), fireData.getxDir(),
                fireData.getyDir(), bulletSpeed * fireData.getBulletSpeedFactor(), color,
                (int) Math.ceil(damage * fireData.getBulletDamageFactor()),
                 2, true, belongTeam * fireData.getBelongTeamFactor(), effects));
        fireData.setBullets(bullets);
    }

    @Override
    protected void cast(double x, double y, double xDest, double yDest) {
        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        double xDir = (xDest - x) / dist;
        double yDir = (yDest - y) / dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        double degree = Math.toDegrees(Math.atan(yDir / xDir));
        if (xDir < 0) {
            degree = degree - 180;
        }
        for (int i = -3; i < 4; i++) {
            double newR = Math.toRadians(degree + i * 10);
            bullets.add(new ExplosiveBullet(x, y, Math.cos(newR), Math.sin(newR), bulletSpeed, color, damage, 2, false, belongTeam, effects));
        }
        user.addBullets(bullets);
    }


    class RocketFactory implements WeaponInstance<Rocket> {
        @Override
        public Rocket create() {
            return new Rocket();
        }
    }
}
