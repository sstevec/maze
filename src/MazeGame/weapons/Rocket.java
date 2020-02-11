package MazeGame.weapons;

import MazeGame.bullets.Bullet;
import MazeGame.bullets.ExplosiveBullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Rocket extends Weapon {

    public Rocket(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects){
        super("Rocket",0.5, 15, color,100, belongTeam,5, effects);
    }

    @Override
    protected ArrayList<Bullet> fire(double x, double y, double xDest, double yDest){
        double dist = Math.sqrt((xDest-x)*(xDest-x) + (yDest-y)*(yDest-y));
        double xDir = (xDest-x)/dist;
        double yDir = (yDest-y)/dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new ExplosiveBullet(x,y,xDir,yDir,bulletSpeed, color, damage,2,true, belongTeam, effects));
        return bullets;
    }

    @Override
    protected ArrayList<Bullet> cast(int x, int y, int xDest, int yDest) {
        double dist = Math.sqrt((xDest-x)*(xDest-x) + (yDest-y)*(yDest-y));
        double xDir = (xDest-x)/dist;
        double yDir = (yDest-y)/dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        for(int i = -3; i<3; i++) {
            bullets.add(new ExplosiveBullet(x,y,xDir,yDir,bulletSpeed, color, damage,2,true, belongTeam, effects));
        }
        return bullets;
    }
}
