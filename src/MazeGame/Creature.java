package MazeGame;

import MazeGame.buffs.Buff;
import MazeGame.bullets.Bullet;
import MazeGame.helper.bulletPositionRecorder;
import MazeGame.helper.creaturePositionRecorder;
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

    protected bulletPositionRecorder[] bullets;
    protected ArrayList<Integer> avaSlot;
    protected ArrayList<Integer> readySlot;
    protected Timer bulletDriver = new Timer();
    protected creaturePositionRecorder[] creatures;
    protected GameResourceController gameResourceController;

    private boolean died = false;
    private Timer aliveChecker = new Timer();



    public Creature(int currentHealth, int maxHealth, int teamNumber, GameResourceController gameResourceController){
        this.gameResourceController = gameResourceController;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.teamNumber = teamNumber;
        this.cellInfo = gameResourceController.getTotalMap();
        this.bullets = new bulletPositionRecorder[500];
        this.avaSlot = new ArrayList<>();
        this.readySlot = new ArrayList<>();
        this.creatures = gameResourceController.getCreatures();
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
            bullets[i] = new bulletPositionRecorder();
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
                        bullet.initBulletDriver(assignNumber, creatures, this.bullets, readySlot, cellInfo);
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
        for (bulletPositionRecorder bullet : bullets) {
            Bullet temp = bullet.getBulletReference();
            if (temp != null) {
                temp.die();
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

    public creaturePositionRecorder[] getCreatures() {
        return creatures;
    }

    public bulletPositionRecorder[] getBullets() {
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
}
