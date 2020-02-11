package MazeGame.weapons;

import MazeGame.bullets.Bullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class NoWeapon extends Weapon {

    Random random = new Random();
    public NoWeapon(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects){
        super("NoWeapon",0.001, 0, color,0, belongTeam,10, effects);
    }

    @Override
    protected ArrayList<Bullet> fire(double x, double y, double xDest, double yDest){
        return null;
    }

    @Override
    protected ArrayList<Bullet> cast(int x, int y, int xDest, int yDest) {
        double dist = Math.sqrt((xDest-x)*(xDest-x) + (yDest-y)*(yDest-y));
        double xDir = (xDest-x)/dist;
        double yDir = (yDest-y)/dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        for(int i = 0; i<30; i++) {
            int tempX = random.nextInt(21)-10;
            int tempY = random.nextInt(21)-10;
            bullets.add(new NormalBullet(x, y, xDir+tempX/10.0, yDir+tempY/10.0, 3, color, 1, belongTeam, effects));
        }
        return bullets;
    }
}
