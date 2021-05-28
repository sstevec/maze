package MazeGame.weapons;

import MazeGame.creature.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.FireBullet;
import MazeGame.effect.Effect;
import MazeGame.helper.FireData;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class FireGun extends Weapon {

    public FireGun() {
        super("FireGun", 3.0, 5, null, 15, 0, 3, null, null);
    }

    public FireGun(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user) {
        super("FireGun", 3.0, 5, color, 15, belongTeam, 3, effects, user);
    }

    @Override
    protected void fire(FireData fireData) {

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new FireBullet(fireData.getUserX(), fireData.getUserY(), fireData.getxDir(),
                fireData.getyDir(), bulletSpeed * fireData.getBulletSpeedFactor(), color,
                (int) Math.ceil(damage * fireData.getBulletDamageFactor()),
                belongTeam * fireData.getBelongTeamFactor(), effects));
        fireData.setBullets(bullets);
    }

    @Override
    protected void cast(int x, int y, int xDest, int yDest) {
        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        double xDir = (xDest - x) / dist;
        double yDir = (yDest - y) / dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        double degree = Math.toDegrees(Math.atan(yDir / xDir));
        if (xDir < 0) {
            degree = degree - 180;
        }
        for (int i = 0; i < 12; i++) {
            double newR = Math.toRadians(degree + i * 30);
            bullets.add(new FireBullet(x, y, Math.cos(newR), Math.sin(newR), bulletSpeed, color, damage, belongTeam, effects));
        }
        user.addBullets(bullets);
    }

    class FireGunFactory implements WeaponInstance<FireGun> {
        @Override
        public FireGun create() {
            return new FireGun();
        }
    }
}
