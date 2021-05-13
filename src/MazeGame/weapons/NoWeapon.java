package MazeGame.weapons;

import MazeGame.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.NormalBullet;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class NoWeapon extends Weapon {


    public NoWeapon(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user) {
        super("NoWeapon", 0.001, 0, color, 0, belongTeam, 10, effects, user);
    }

    @Override
    protected void fire(double x, double y, double xDest, double yDest) {
    }

    @Override
    protected void cast(int x, int y, int xDest, int yDest) {
    }
}
