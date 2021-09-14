package MazeGame.weapons;

import MazeGame.creature.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.FireBullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;
import MazeGame.helper.FireData;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class BrokenGun extends Weapon {

    public BrokenGun() {
        super("BrokenGun", 2.0, 3, null, 5, 0, 3, null, null);

    }

    public BrokenGun(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user) {
        super("BrokenGun", 2.0, 3, color, 5, belongTeam, 3, effects, user);
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

    // ability is fire two fire bullet with greater damage and speed
    @Override
    protected void cast(double x, double y, double xDest, double yDest) {
        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        double xDir = (xDest - x) / dist;
        double yDir = (yDest - y) / dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new FireBullet(x, y, xDir, yDir, bulletSpeed * 3, color, damage * 3, belongTeam, effects));
        user.addBullets(bullets);
        try {
            Thread.sleep(50);
        } catch (Exception e) {
            System.out.println("Broken Gun sleep error");
        }
        bullets.clear();
        bullets.add(new FireBullet(x, y, xDir, yDir, bulletSpeed * 3, color, damage * 3, belongTeam, effects));
        user.addBullets(bullets);
    }

    class BrokenGunFactory implements WeaponInstance<BrokenGun> {
        @Override
        public BrokenGun create() {
            return new BrokenGun();
        }
    }
}
