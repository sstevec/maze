package MazeGame.bullets;

import MazeGame.helper.Event;
import MazeGame.helper.EventInfo;
import MazeGame.map.Cell;
import MazeGame.creature.Creature;
import MazeGame.effect.Effect;
import MazeGame.helper.BulletPositionRecorder;
import MazeGame.helper.CreaturePositionRecorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.helper.Info.cellWidth;

public abstract class Bullet {

    protected double x;         // pos of the bullet
    protected double y;
    protected double xDir;        // directions of the bullet
    protected double yDir;
    protected double speed;
    protected int damage;
    protected Color color;
    protected int belongTeam;
    private int assignNumber;
    private boolean died = false;

    protected CopyOnWriteArrayList<Effect> effects;
    private final Timer bulletDriver;
    protected CreaturePositionRecorder[] creatures;
    private ArrayList<Integer> readyPool;
    private BulletPositionRecorder[] bullets;
    private Cell[][] cells;
    private final ArrayList<Event> dieEvents;
    private final ArrayList<Event> createEvents;
    private Creature source;


    public Bullet(double x, double y, double xDir, double yDir, double speed, Color color, int damage, int belongTeam, CopyOnWriteArrayList<Effect> effects) {
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
        this.dieEvents = new ArrayList<>();
        this.createEvents = new ArrayList<>();
    }


    public void fly() {
        x = x + xDir * speed;
        y = y + yDir * speed;

        bullets[assignNumber].setiPos((int) y);
        bullets[assignNumber].setjPos((int) x);
    }

    public void initBulletDriver(int assignNumber, CreaturePositionRecorder[] creatureList, BulletPositionRecorder[] bullets,
                                 ArrayList<Integer> readyPool, Cell[][] cells, Creature source) {
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
        this.source = source;

        bullets[assignNumber].setiPos((int) y);
        bullets[assignNumber].setjPos((int) x);
        bullets[assignNumber].setBulletReference(this);

        for (Event e : createEvents) {
            e.invoke(null);
        }

        bulletDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                fly();

                // after flying, check if it reach the bound
                if (cells[(int) (y / cellWidth)][(int) (x / cellWidth)].isBoarder()) {
                    EventInfo info = new EventInfo(source);
                    if (cells[(int) ((y - (yDir * speed)) / cellWidth)][(int) (x / cellWidth)].isBoarder()) {
                        info.getDataMap().put("reflectDir", "x");
                    } else {
                        info.getDataMap().put("reflectDir", "y");
                    }
                    dieWithEvent(info);
                }


                // then check if the bullet hit any enemy
                for (CreaturePositionRecorder c : creatures) {
                    Creature creature = c.getCreatureReference();
                    if (creature == null) {
                        continue;
                    }
                    double iPos = creature.getiDPos();
                    double jPos = creature.getjDPos();

                    double jDis = jPos * cellWidth + 7.5 - x;
                    double iDis = iPos * cellWidth + 7.5 - y;
                    double dis = Math.sqrt(jDis * jDis + iDis * iDis);

                    if (belongTeam != creature.getTeamNumber() && dis <= 12.5) {
                        hurt(creature);
                        EventInfo info = new EventInfo(source);
                        dieWithEvent(info);
                        return;
                    }
                }


            }
        }, 0, 1000 / 40);
    }

    public void dieWithEvent(EventInfo info) {
        if (died) {
            System.out.println("WARNING from bullet: try to kill one bullet twice");
            return;
        } else {
            died = true;
        }

        dieEffect();

        for (Event e : dieEvents) {
            e.invoke(info);
        }

        dieClear();
    }

    public void dieWithoutEvent() {
        if (died) {
            System.out.println("WARNING from bullet: try to kill one bullet twice");
            return;
        } else {
            died = true;
        }
        dieClear();
    }

    private void dieClear() {
        bulletDriver.cancel();
        bullets[assignNumber].setiPos(-1);
        bullets[assignNumber].setjPos(-1);
        bullets[assignNumber].setBulletReference(null);

        // give the assigned number back so the graph list slot can be reused
        synchronized (readyPool) {
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

    public abstract Bullet copy(double x, double y, double xDir, double yDir, double speed, int damage);

    public double getxDir() {
        return xDir;
    }

    public double getyDir() {
        return yDir;
    }

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

    public double getSpeed() {
        return speed;
    }

    public ArrayList<Event> getDieEvents() {
        return dieEvents;
    }

    public ArrayList<Event> getCreateEvents() {
        return createEvents;
    }

    public Creature getSource() {
        return source;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
