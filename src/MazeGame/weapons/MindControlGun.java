package MazeGame.weapons;

import MazeGame.Creature;
import MazeGame.bullets.Bullet;
import MazeGame.bullets.ControlBullet;
import MazeGame.effect.Effect;
import MazeGame.effect.Explosion;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.cellWidth;

public class MindControlGun extends Weapon {

    private CopyOnWriteArrayList<Creature> controlList;
    private int effectRadius = 1;
    private boolean breakable = false;

    public MindControlGun() {
        super("MindController", 1.0, 7, null, 1, 0, 1, null, null);
        controlList = new CopyOnWriteArrayList<>();
    }

    public MindControlGun(Color color, int belongTeam, CopyOnWriteArrayList<Effect> effects, Creature user) {
        super("MindController", 1.0, 7, color, 1, belongTeam, 1, effects, user);
        controlList = new CopyOnWriteArrayList<>();
    }

    @Override
    protected void fire(double x, double y, double xDest, double yDest) {
        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        double xDir = (xDest - x) / dist;
        double yDir = (yDest - y) / dist;

        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.add(new ControlBullet(x, y, xDir, yDir, bulletSpeed, color, damage, belongTeam, effects, controlList));
        user.addBullets(bullets);
    }

    @Override
    protected void cast(int x, int y, int xDest, int yDest) {
        int size = controlList.size();
        for (int i = 0; i < size; i++) {
            if (controlList.get(i).getCurrentHealth() <= 0) {
                controlList.remove(i);
                i--;
                size--;
            }
        }
        for (Creature temp : controlList
        ) {
            effects.add(new Explosion((int) (temp.getjPos() * cellWidth), (int) (temp.getiPos() * cellWidth), 25 + effectRadius * cellWidth, effectRadius, 100, breakable, user.getCreatures()));
        }
    }


    class MindControlGunFactory implements WeaponInstance<MindControlGun> {
        @Override
        public MindControlGun create() {
            return new MindControlGun();
        }
    }
}
