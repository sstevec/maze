package MazeGame.creature;

import MazeGame.map.Cell;
import MazeGame.GameResourceController;
import MazeGame.buffs.Buff;
import MazeGame.bullets.Bullet;
import MazeGame.equipment.weaponComponent.WeaponComponent;
import MazeGame.helper.BulletPositionRecorder;
import MazeGame.helper.CreaturePositionRecorder;
import MazeGame.weapons.Weapon;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public abstract class Creature {
    protected Integer currentHealth = 100;
    private int maxHealth = 100;

    protected int extraHealth = 0;
    protected int extraDamage = 0;
    protected int extraCDReduce = 0;
    protected int extraAttackSpeed = 0;

    private int teamNumber;

    protected Color color;
    protected int iPos;
    protected int jPos;
    protected Cell[][] cellInfo; // a reference of cell info
    protected HashMap<String, Buff> buffs = new HashMap<>();
    protected Weapon weapon;

    protected BulletPositionRecorder[] bullets;
    protected final ArrayList<Integer> avaSlot;
    protected ArrayList<Integer> readySlot;
    protected Timer bulletDriver = new Timer();
    protected CreaturePositionRecorder[] creatures;
    protected GameResourceController gameResourceController;

    protected final ArrayList<WeaponComponent> preProcessComponent;
    protected final WeaponComponent[] postProcessComponent;
    protected final WeaponComponent[] beg;

    private boolean died = false;
    private final Timer aliveChecker = new Timer();



    public Creature(int currentHealth, int maxHealth, int teamNumber, GameResourceController gameResourceController){
        this.gameResourceController = gameResourceController;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.teamNumber = teamNumber;
        this.cellInfo = gameResourceController.getTotalMap();
        this.bullets = new BulletPositionRecorder[500];
        this.avaSlot = new ArrayList<>();
        this.readySlot = new ArrayList<>();
        this.creatures = gameResourceController.getCreatures();

        this.preProcessComponent = new ArrayList<>();
        this.postProcessComponent = new WeaponComponent[6];
        this.beg = new WeaponComponent[12];
        customInit();
        initDriver();
    }

    protected abstract void customInit();


    public void takeDamage(int damage) {
        synchronized (currentHealth) {
            currentHealth = currentHealth - damage;
        }
    }

    private void initDriver() {
        for (int i = 0; i < 500; i++) {
            bullets[i] = new BulletPositionRecorder();
            avaSlot.add(i);
        }

        bulletDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (avaSlot) {
                    synchronized (readySlot) {
                        avaSlot.addAll(readySlot);
                        readySlot.clear();
                    }
                }
            }
        }, 0, 3000);

        aliveChecker.schedule(new TimerTask() {
            @Override
            public void run() {
                if(currentHealth <= 0){
                    // this creature died
                    aliveChecker.cancel();
                    die();
                }
            }
        },0,1000/10);
    }

    public void addBullets(ArrayList<Bullet> bullets) {
        while (true) {
            if (bullets.isEmpty()) {
                return;
            }

            synchronized (avaSlot) {
                if (avaSlot.size() >= bullets.size()) {
                    // can fit the bullet array in
                    while(!bullets.isEmpty()) {
                        int assignNumber = avaSlot.remove(0);
                        Bullet bullet = bullets.remove(0);
                        bullet.initBulletDriver(assignNumber, creatures, this.bullets, readySlot, cellInfo, this);
                    }
                    return;
                }
            }
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("ERROR from creature: " + e.getMessage());
            }
        }

    }

    public void die() {
        if (died) {
            System.out.println("WARNING from creature: try to kill one creature twice");
            return;
        } else {
            died = true;
        }
        dieClear();
        bulletDriver.cancel();
        for (BulletPositionRecorder bullet : bullets) {
            Bullet temp = bullet.getBulletReference();
            if (temp != null) {
                temp.dieWithoutEvent();
            }
        }
    }

    public abstract void dieClear();

    public abstract void dieEffect();

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Color getColor() {
        return color;
    }

    public int getiPos() {
        return iPos;
    }

    public int getjPos() {
        return jPos;
    }

    public CreaturePositionRecorder[] getCreatures() {
        return creatures;
    }

    public BulletPositionRecorder[] getBullets() {
        return bullets;
    }

    /***
     *
     *  Buff with same name will cover the old one
     *
     */
    public void addBuff(Buff buff) {
        synchronized (buffs) {
            if (buffs.get(buff.getName()) != null) {
                buffs.get(buff.getName()).cancelBuff();
            }
            buffs.put(buff.getName(), buff);
        }
    }

    public void removeBuff(Buff buff) {
        buffs.remove(buff.getName());
    }


    public void updateAttribute(int extraHealth, int heal, int extraDamage, int extraCDReduce, int extraAttackSpeed){
        this.extraHealth += extraHealth;
        this.extraDamage += extraDamage;
        this.extraCDReduce += extraCDReduce;
        this.extraAttackSpeed += extraAttackSpeed;
        weapon.upgradeAttackSpeed(extraAttackSpeed);
        weapon.upgradeDamage(extraDamage);
        weapon.upgradeExtraCD(extraCDReduce);
        synchronized (this) {
            maxHealth += extraHealth;
            currentHealth = currentHealth + (int)(maxHealth * (heal/100.0));
            currentHealth = Math.min(currentHealth, maxHealth);
        }
    }

    public ArrayList<WeaponComponent> getPreProcessComponent() {
        return preProcessComponent;
    }

    public WeaponComponent[] getPostProcessComponent() {
        return postProcessComponent;
    }

    public WeaponComponent[] getBeg() {
        return beg;
    }

    public boolean putIntoBeg(WeaponComponent weaponComponent){
        synchronized (beg){
            for(int i = 0; i<beg.length; i++){
                if(beg[i] == null){
                    beg[i] = weaponComponent;
                    return true;
                }
            }
            return false;
        }
    }

    public void putIntoPreProcess(WeaponComponent weaponComponent){
        synchronized (preProcessComponent){
            preProcessComponent.add(weaponComponent);
        }
    }

    public boolean putIntoPostProcess(WeaponComponent weaponComponent){
        synchronized (postProcessComponent){
            for(int i = 0; i<postProcessComponent.length; i++){
                if(postProcessComponent[i] == null){
                    postProcessComponent[i] = weaponComponent;
                    return true;
                }
            }
            return false;
        }
    }
}
