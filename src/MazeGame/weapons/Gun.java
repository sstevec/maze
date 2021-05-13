package MazeGame.weapons;

import MazeGame.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;

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
    protected void fire(double x, double y, double xDest, double yDest) {
        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        double xDir = (xDest - x) / dist;
        double yDir = (yDest - y) / dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new NormalBullet(x, y, xDir, yDir, bulletSpeed, color, damage, belongTeam, effects));
        user.addBullets(bullets);
    }

    @Override
    protected void cast(int x, int y, int xDest, int yDest) {
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
