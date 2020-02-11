package MazeGame;

import MazeGame.bullets.Bullet;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Creature {
    private int currentHealth = 100;
    private int maxHealth = 100;
    private int teamNumber;
    protected Color color;
    protected double iPos;
    protected double jPos;
    protected Cell[][] cellInfo; // a reference of cell info

    protected CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    protected Timer bulletDriver = new Timer();

    public Creature(int currentHealth, int maxHealth, int teamNumber, Cell[][] cellInfo){
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.teamNumber = teamNumber;
        this.cellInfo = cellInfo;

        initBulletDriver();
    }

    public void takeDamage(int damage){
        currentHealth = currentHealth - damage;
    }

    private void initBulletDriver(){
        bulletDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                // let bullet fly
                for (Bullet temp : bullets
                ) {
                    temp.fly();
                }

                // after flying, check if it reach the bound
                int bulletSize = bullets.size();
                for (int i = 0; i < bulletSize; i++) {
                    Bullet temp = bullets.get(i);
                    int j1 = (int) (temp.getX() + 5) / 15;
                    int i1 = (int) (temp.getY() + 5) / 15;
                    int j2 = (int) temp.getX() / 15;
                    int i2 = (int) temp.getY() / 15;
                    if (cellInfo[i1][j1].isBoarder() || cellInfo[i2][j2].isBoarder()) {
                        temp.dieEffect();
                        bullets.remove(i);
                        i--;
                        bulletSize--;
                    }
                    synchronized (cellInfo) {
                        if (cellInfo[i1][j1].getOccupiedCreature() != null) {
                            if (cellInfo[i1][j1].getOccupiedCreature().getTeamNumber() != temp.getBelongTeam()) {
                                temp.hurt(cellInfo[i1][j1].getOccupiedCreature());
                                bullets.remove(i);
                                i--;
                                bulletSize--;
                                continue;
                            }
                        }
                        if (cellInfo[i2][j2].getOccupiedCreature() != null) {
                            if (cellInfo[i2][j2].getOccupiedCreature().getTeamNumber() != temp.getBelongTeam()) {
                                temp.hurt(cellInfo[i2][j2].getOccupiedCreature());
                                bullets.remove(i);
                                i--;
                                bulletSize--;
                            }
                        }
                    }
                }

            }
        }, 0, 1000 / 40);
    }

    public abstract void die();

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

    public double getiPos() {
        return iPos;
    }

    public double getjPos() {
        return jPos;
    }

    public CopyOnWriteArrayList<Bullet> getBullets() {
        return bullets;
    }
}
