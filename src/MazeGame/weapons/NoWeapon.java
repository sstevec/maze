package MazeGame.weapons;

import MazeGame.Bullet;
import MazeGame.Weapon;

import java.awt.*;

public class NoWeapon extends Weapon {

    public NoWeapon(Color color, int belongTeam){
        super("NoWeapon",0.001, 0, color,0, belongTeam);
    }

    @Override
    protected Bullet fire(int x, int y, int xDest, int yDest){
        return null;
    }
}
