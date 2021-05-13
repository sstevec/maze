package MazeGame.bullets;

import MazeGame.Cell;
import MazeGame.Creature;
import MazeGame.effect.Effect;
import MazeGame.helper.bulletPositionRecorder;
import MazeGame.helper.creaturePositionRecorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.cellWidth;

public abstract class Bullet {

    protected double x;         // pos of the bullet
    protected double y;
    private double xDir;        // directions of the bullet
    private double yDir;
    private int speed;
    private int damage;
    private Color color;
    private int belongTeam;
    protected CopyOnWriteArrayList<Effect> effects;
    private Timer bulletDriver;
    protected creaturePositionRecorder[] creatures;
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

    public void initBulletDriver(int assignNumber, creaturePositionRecorder[] creatureList, bulletPositionRecorder[] bullets,
                                 ArrayList<Integer> readyPool, Cell[][] cells) {
        // creatures is a list of creatures, it may contain null spot in between
        this.creatures = creatureList;

        // bullets is a list of bullets that will be graphed, so we need to
        //   add this bullet to this list to show it on the screen
        this.bullets = bullets;

        // bullets is not RW safe, so what we do here is assign it a spot that is
        //   guarantee to be empty so we don't have to lock it
        this.assignNumber = assignNumber;
        this.readyPool = readyPool;

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
                for (creaturePositionRecorder c : creatures) {
                    Creature creature = c.getCreatureReference();
                    if (creature == null) {
                        continue;
                    }
                    int iPos = creature.getiPos();
                    int jPos = creature.getjPos();

                    double jDis = jPos * cellWidth + 7.5 - x;
                    double iDis = iPos * cellWidth + 7.5 - y;
                    double dis = Math.sqrt(jDis * jDis + iDis * iDis);

                    if (belongTeam != creature.getTeamNumber() && dis <= 12.5) {
                        hurt(creature);
                        die();
                        return;
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

        // give the assigned number back so the graph list slot can be reused
        synchronized (readyPool){
            readyPool.add(assignNumber);
        }
    }


    /***
     *
     * When a bullet hit a target, it hurts the target and disappear
     * Thus, child class should implement hurt so that it can apply buff
     *  to the target or do whatever they want
     * Child class should also implement dieEffect, so that bullet can do something
     *  like explode when it disappear
     *
     */

    abstract void hurt(Creature creature);

    abstract void dieEffect();




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
