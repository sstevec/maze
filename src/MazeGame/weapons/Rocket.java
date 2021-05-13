package MazeGame.weapons;

import MazeGame.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.ExplosiveBullet;
import MazeGame.effect.Effect;

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
    protected void fire(double x, double y, double xDest, double yDest) {
        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        double xDir = (xDest - x) / dist;
        double yDir = (yDest - y) / dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new ExplosiveBullet(x, y, xDir, yDir, bulletSpeed, color, damage, 2, true, belongTeam, effects));
        user.addBullets(bullets);
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
