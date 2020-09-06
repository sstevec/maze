package MazeGame.bullets;

import MazeGame.Cell;
import MazeGame.Creature;
import MazeGame.effect.Effect;
import MazeGame.helper.bulletPositionRecorder;
import MazeGame.helper.enemyPositionRecorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.cellWidth;

public abstract class Bullet {

    protected double x;
    protected double y;
    private double xDir;
    private double yDir;
    private int speed;
    private int damage;
    private Color color;
    private int belongTeam;
    protected CopyOnWriteArrayList<Effect> effects;
    private Timer bulletDriver;
    protected enemyPositionRecorder[] enemies;
    private int assignNumber;
    private ArrayList<Integer> readyPool;
    private bulletPositionRecorder[] bullets;
    private Cell[][] cells;
    private boolean died = false;

    public Bullet(double x, double y, double xDir, double yDir, int speed, Color color, int damage, int belongTeam, CopyOnWriteArrayList<Effect> effects) {
        this.x = x;
        this.y = y;
        this.xDir = xDir;
        this.yDir = yDir;
        this.speed = speed;
        this.color = color;
        this.damage = damage;
        this.belongTeam = belongTeam;
        this.effects = effects;
        this.bulletDriver = new Timer();
    }


    public void fly() {
        x = x + xDir * speed;
        y = y + yDir * speed;

        bullets[assignNumber].setiPos((int) y);
        bullets[assignNumber].setjPos((int) x);
    }

    public void initBulletDriver(int assignNumber, enemyPositionRecorder[] enemyList, bulletPositionRecorder[] bullets,
                                 ArrayList<Integer> readyPool, Cell[][] cells) {
        this.enemies = enemyList;
        this.assignNumber = assignNumber;
        this.readyPool = readyPool;
        this.bullets = bullets;
        this.cells = cells;

        bullets[assignNumber].setiPos((int) y);
        bullets[assignNumber].setjPos((int) x);
        bullets[assignNumber].setBulletReference(this);

        bulletDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                fly();

                // after flying, check if it reach the bound
                if (cells[(int) (y / cellWidth)][(int) (x / cellWidth)].isBoarder()) {
                    die();
                }


                // then check if the bullet hit any enemy
                for (enemyPositionRecorder enemy : enemies) {
                    Creature creature = enemy.getEnemyReference();
                    if (creature == null) {
                        continue;
                    }
                    int iPos = creature.getiPos();
                    int jPos = creature.getjPos();

                    double jDis = jPos * cellWidth + 7.5 - x;
                    double iDis = iPos * cellWidth + 7.5 - y;
                    double dis = Math.sqrt(jDis * jDis + iDis * iDis);

                    if (dis <= 12.5) {
                        hurt(creature);
                        die();
                    }
                }


            }
        }, 0, 1000 / 40);
    }

    public void die(){
        if(died){
            System.out.println("WARNING from bullet: try to kill one bullet twice");
            return;
        }else{
            died = true;
        }
        dieEffect();
        bulletDriver.cancel();
        bullets[assignNumber].setiPos(-1);
        bullets[assignNumber].setjPos(-1);
        bullets[assignNumber].setBulletReference(null);
        synchronized (readyPool){
            readyPool.add(assignNumber);
        }
    }

    public abstract void hurt(Creature creature);

    public abstract void dieEffect();

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public int getDamage() {
        return damage;
    }

    public int getBelongTeam() {
        return belongTeam;
    }
}
