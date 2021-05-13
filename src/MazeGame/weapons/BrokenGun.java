package MazeGame.weapons;

import MazeGame.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.FireBullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;

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
    protected void fire(double x, double y, double xDest, double yDest) {
        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        double xDir = (xDest - x) / dist;
        double yDir = (yDest - y) / dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new NormalBullet(x, y, xDir, yDir, bulletSpeed, color, damage, belongTeam, effects));
        user.addBullets(bullets);
    }

    // ability is fire two fire bullet with greater damage and speed
    @Override
    protected void cast(int x, int y, int xDest, int yDest) {
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
