package MazeGame.weapons;

import MazeGame.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Gun extends Weapon {

    public Gun(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user){
        super("Gun",5.0, 5, color,20, belongTeam,5, effects, user);
    }

    @Override
    protected ArrayList<Bullet> fire(double x, double y, double xDest, double yDest){
        double dist = Math.sqrt((xDest-x)*(xDest-x) + (yDest-y)*(yDest-y));
        double xDir = (xDest-x)/dist;
        double yDir = (yDest-y)/dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new NormalBullet(x,y,xDir,yDir,bulletSpeed, color, damage, belongTeam, effects));
        return bullets;
    }

    @Override
    protected ArrayList<Bullet> cast(int x, int y, int xDest, int yDest) {
        double dist = Math.sqrt((xDest-x)*(xDest-x) + (yDest-y)*(yDest-y));
        double xDir = (xDest-x)/dist;
        double yDir = (yDest-y)/dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        for(int i = -5; i<5; i++) {
            bullets.add(new NormalBullet(x, y, xDir+i/10.0, yDir+i/10.0, bulletSpeed, color, damage, belongTeam, effects));
        }
        return bullets;
    }
}
