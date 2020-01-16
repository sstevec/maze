package MazeGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Weapon {
    protected String name = "weapon";
    private double fireRate = 1;
    private double abilityCD = 5;
    protected int bulletSpeed = 1;
    private boolean allowFire = true;
    private boolean allowCast = true;
    protected Color color;
    protected int damage;
    protected int belongTeam;

    public Weapon(String name, double fireRate, int bulletSpeed, Color color, int damage, int belongTeam, double abilityCD){
        this.name = name;
        this.fireRate = fireRate;
        this.bulletSpeed = bulletSpeed;
        this.color = color;
        this.damage = damage;
        this.belongTeam = belongTeam;
        this.abilityCD = abilityCD;
    }

    public ArrayList<Bullet> CheckFireStatus(int xPos, int yPos, int xDest, int yDest){
        if(allowFire){
            allowFire = false;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    allowFire = true;
                }
            }, (long) (1000.0/fireRate));
            return fire(xPos,yPos, xDest, yDest);
        }
        return null;
    }

    public ArrayList<Bullet> CheckAbilityStatus(int xPos, int yPos, int xDest, int yDest){
        if(allowCast){
            allowCast = false;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    allowCast = true;
                }
            }, (long) (1000.0*abilityCD));
            return cast(xPos,yPos, xDest, yDest);
        }
        return null;
    }

    protected abstract ArrayList<Bullet> fire(int x, int y, int xDest, int yDest);

    protected abstract ArrayList<Bullet> cast(int x, int y, int xDest, int yDest);

    public String getName() {
        return name;
    }

    public double getFireRate() {
        return fireRate;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public int getDamage() {
        return damage;
    }
}
