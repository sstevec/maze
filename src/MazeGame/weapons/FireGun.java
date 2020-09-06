package MazeGame.weapons;

import MazeGame.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.FireBullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class FireGun extends Weapon {

    public FireGun(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user){
        super("FireGun",3.0, 5, color,15, belongTeam,3, effects, user);
    }

    @Override
    protected ArrayList<Bullet> fire(double x, double y, double xDest, double yDest){
        double dist = Math.sqrt((xDest-x)*(xDest-x) + (yDest-y)*(yDest-y));
        double xDir = (xDest-x)/dist;
        double yDir = (yDest-y)/dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new FireBullet(x,y,xDir,yDir,bulletSpeed, color, damage, belongTeam, effects));
        return bullets;
    }

    @Override
    protected ArrayList<Bullet> cast(int x, int y, int xDest, int yDest) {
        double dist = Math.sqrt((xDest-x)*(xDest-x) + (yDest-y)*(yDest-y));
        double xDir = (xDest-x)/dist;
        double yDir = (yDest-y)/dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        for(int i = 0; i<3; i++) {
            bullets.add(new FireBullet(x, y, xDir, yDir, bulletSpeed * 3, color, damage * 3, belongTeam, effects));
        }
        return bullets;
    }
}
