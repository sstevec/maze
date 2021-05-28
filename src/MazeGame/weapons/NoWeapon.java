package MazeGame.weapons;

import MazeGame.creature.Creature;
import MazeGame.effect.Effect;
import MazeGame.helper.FireData;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class NoWeapon extends Weapon {


    public NoWeapon(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user) {
        super("NoWeapon", 0.001, 0, color, 0, belongTeam, 10, effects, user);
    }

    @Override
    protected void fire(FireData fireData) {
    }

    @Override
    protected void cast(int x, int y, int xDest, int yDest) {
    }
}
