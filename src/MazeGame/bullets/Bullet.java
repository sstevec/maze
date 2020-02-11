package MazeGame.bullets;

import MazeGame.Creature;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

    public Bullet(double x, double y, double xDir, double yDir, int speed, Color color, int damage, int belongTeam, CopyOnWriteArrayList<Effect> effects){
        this.x = x;
        this.y = y;
        this.xDir = xDir;
        this.yDir = yDir;
        this.speed = speed;
        this.color = color;
        this.damage = damage;
        this.belongTeam = belongTeam;
        this.effects = effects;
    }

    public void fly(){
        x = x + xDir*speed;
        y = y + yDir*speed;
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
