package MazeGame.weapons;

import MazeGame.bullets.Bullet;
import MazeGame.effect.Effect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Weapon {
    protected String name = "weapon";
    private double fireRate = 1;
    private double abilityCD = 5;
    protected int bulletSpeed = 1;
    private int currentTime;
    private boolean allowFire = true;
    private boolean allowCast = true;
    protected Color color;
    protected int damage;
    protected int belongTeam;
    protected CopyOnWriteArrayList<Effect> effects;

    public Weapon(String name, double fireRate, int bulletSpeed, Color color, int damage, int belongTeam, double abilityCD, CopyOnWriteArrayList<Effect> effects){
        this.name = name;
        this.fireRate = fireRate;
        this.bulletSpeed = bulletSpeed;
        this.color = color;
        this.damage = damage;
        this.belongTeam = belongTeam;
        this.abilityCD = abilityCD;
        this.effects = effects;
    }

    public ArrayList<Bullet> CheckFireStatus(double xPos, double yPos, double xDest, double yDest){
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
            currentTime = 0;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(currentTime >= abilityCD*1000) {
                        allowCast = true;
                        this.cancel();
                    }
                    currentTime +=100;
                }
            }, 0,1000/10);
            return cast(xPos,yPos, xDest, yDest);
        }
        return null;
    }

    protected abstract ArrayList<Bullet> fire(double x, double y, double xDest, double yDest);

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

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBelongTeam(int belongTeam) {
        this.belongTeam = belongTeam;
    }

    public double getCD(){
        if(allowCast){
            return 0.0;
        }else{
            return (abilityCD*1000 - currentTime)/(abilityCD*1000);
        }
    }
}
