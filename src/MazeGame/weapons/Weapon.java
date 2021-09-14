package MazeGame.weapons;

import MazeGame.creature.Creature;
import MazeGame.effect.Effect;
import MazeGame.Item;
import MazeGame.equipment.weaponComponent.WeaponComponent;
import MazeGame.helper.FireData;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.helper.Info.WEAPON_KIND;

public abstract class Weapon extends Item {

    private double fireRate = 1;
    private int attackSpeed = 0;
    private double abilityCD = 5;
    private int extraCoolDown = 0;
    protected int bulletSpeed = 1;
    private int currentTime;
    private boolean allowFire = true;
    private boolean allowCast = true;

    protected Color color;
    protected int originalDamage;
    protected int damage;
    protected int belongTeam;
    protected CopyOnWriteArrayList<Effect> effects;
    protected Creature user;

    public Weapon(String name, double fireRate, int bulletSpeed, Color color, int damage,
                  int belongTeam, double abilityCD, CopyOnWriteArrayList<Effect> effects, Creature user) {
        super(WEAPON_KIND);
        this.name = name;
        this.fireRate = fireRate;
        this.bulletSpeed = bulletSpeed;
        this.color = color;
        this.originalDamage = damage;
        this.damage = damage;
        this.belongTeam = belongTeam;
        this.abilityCD = abilityCD;
        this.effects = effects;
        this.user = user;
    }

    public void CheckFireStatus(double xPos, double yPos, double xDest, double yDest) {
        if (allowFire) {
            allowFire = false;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    allowFire = true;
                }
            }, (long) (1000.0 / (fireRate + attackSpeed / 100.0 * fireRate)));

            FireData fireData = new FireData(xPos, yPos, xDest, yDest);

            for(WeaponComponent wc: user.getPreProcessComponent()){
                if(wc != null) {
                    wc.processFireData(fireData);
                }
            }
            fire(fireData);
            for(WeaponComponent wc: user.getPostProcessComponent()){
                if(wc != null) {
                    wc.processFireData(fireData);
                }
            }

            user.addBullets(fireData.getBullets());
        }
    }

    public void CheckAbilityStatus(double xPos, double yPos, double xDest, double yDest) {
        if (allowCast) {
            allowCast = false;
            currentTime = 0;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (currentTime >= abilityCD * 1000 * (100.0 / (100 + extraCoolDown))) {
                        allowCast = true;
                        this.cancel();
                    }
                    currentTime += 100;
                }
            }, 0, 1000 / 10);
            cast(xPos, yPos, xDest, yDest);
        }
    }

    public void pickUp(Creature creature){

    }

    abstract void fire(FireData fireData);

    abstract void cast(double x, double y, double xDest, double yDest);


    public double getFireRate() {
        return fireRate + attackSpeed / 100.0 * fireRate;
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

    public void setEffects(CopyOnWriteArrayList<Effect> effects) {
        this.effects = effects;
    }

    public void setUser(Creature user) {
        this.user = user;
    }

    public double getCD() {
        if (allowCast) {
            return 0.0;
        } else {
            double cd = abilityCD * 1000 * (100.0 / (100 + extraCoolDown));
            return Math.max((cd - currentTime) / cd, 0);
        }
    }

    public void upgradeDamage(int amount) {
        damage += amount;
    }

    public void upgradeAttackSpeed(int amount) {
        attackSpeed += amount;
    }

    public void upgradeExtraCD(int amount) {
        extraCoolDown += amount;
    }
}
