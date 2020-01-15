package MazeGame.weapons;

import MazeGame.Bullet;
import MazeGame.Weapon;

import java.awt.*;
import java.util.ArrayList;

public class Gun extends Weapon {

    public Gun(Color color, int belongTeam){
        super("Gun",5.0, 5, color,20, belongTeam);
    }

    @Override
    protected ArrayList<Bullet> fire(int x, int y, int xDest, int yDest){
        double dist = Math.sqrt((xDest-x)*(xDest-x) + (yDest-y)*(yDest-y));
        double xDir = (xDest-x)/dist;
        double yDir = (yDest-y)/dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new Bullet(x,y,xDir,yDir,bulletSpeed, color, damage, belongTeam));
        return bullets;
    }
}
