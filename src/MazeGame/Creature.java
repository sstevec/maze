package MazeGame;

import MazeGame.buffs.Buff;
import MazeGame.bullets.Bullet;
import MazeGame.helper.bulletPositionRecorder;
import MazeGame.helper.enemyPositionRecorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class Creature {
    protected int currentHealth = 100;
    private int maxHealth = 100;
    private int teamNumber;
    protected Color color;
    protected int iPos;
    protected int jPos;
    protected Cell[][] cellInfo; // a reference of cell info
    protected HashMap<String, Buff> buffs = new HashMap<>();

    protected bulletPositionRecorder[] bullets;
    protected ArrayList<Integer> avaSlot;
    protected ArrayList<Integer> readySlot;
    protected Timer bulletDriver = new Timer();
    protected enemyPositionRecorder[] enemies;
    private boolean died = false;

    public Creature(int currentHealth, int maxHealth, int teamNumber, Cell[][] cellInfo, enemyPositionRecorder[] enemies){
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.teamNumber = teamNumber;
        this.cellInfo = cellInfo;
        this.bullets = new bulletPositionRecorder[500];
        this.enemies = enemies;
        this.avaSlot = new ArrayList<>();
        this.readySlot = new ArrayList<>();

        initBulletDriver();
    }

    public Creature(int currentHealth, int maxHealth, int teamNumber, Cell[][] cellInfo, Creature creature){
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.teamNumber = teamNumber;
        this.cellInfo = cellInfo;
        this.bullets = new bulletPositionRecorder[500];
        this.enemies = new enemyPositionRecorder[1];
        enemies[0] = new enemyPositionRecorder();
        enemies[0].setEnemyReference(creature);
        this.avaSlot = new ArrayList<>();
        this.readySlot = new ArrayList<>();

        initBulletDriver();
    }

    public void takeDamage(int damage){
        currentHealth = currentHealth - damage;
    }

    private void initBulletDriver(){
        for(int i = 0; i< 500; i++){
            bullets[i] = new bulletPositionRecorder();
            avaSlot.add(i);
        }

        bulletDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (avaSlot){
                    synchronized (readySlot){
                        avaSlot.addAll(readySlot);
                        readySlot.clear();
                    }
                }
            }
        },0,3000);
    }

    public void addBullets(ArrayList<Bullet> bullets){
        while(true){
            if(bullets.isEmpty()){
                break;
            }

            synchronized (avaSlot){
                if(!avaSlot.isEmpty()){
                    int assignNumber = avaSlot.remove(0);
                    Bullet bullet = bullets.remove(0);
                    bullet.initBulletDriver(assignNumber,enemies,this.bullets,readySlot,cellInfo);
                }
            }
            try {
                Thread.sleep(500);
            }catch (Exception e){
                System.out.println("ERROR from creature: " + e.getMessage());
            }
        }

    }

    public void die(){
        if(died){
            System.out.println("WARNING from creature: try to kill one creature twice");
            return;
        }else{
            died = true;
        }
        dieEffect();
        bulletDriver.cancel();
        for(bulletPositionRecorder bullet: bullets){
            Bullet temp = bullet.getBulletReference();
            if(temp != null){
                temp.die();
            }
        }
    }

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

    public bulletPositionRecorder[] getBullets() {
        return bullets;
    }

    public void addBuff(Buff buff){
        synchronized (buffs) {
            if (buffs.get(buff.getName()) != null) {
                buffs.get(buff.getName()).cancelBuff();
            }
            buffs.put(buff.getName(), buff);
        }
    }

    public void removeBuff(Buff buff){
        buffs.remove(buff.getName());
    }

    public enemyPositionRecorder[] getEnemies() {
        return enemies;
    }

    public void setEnemies(enemyPositionRecorder[] enemies) {
        this.enemies = enemies;
    }
}
