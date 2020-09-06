package MazeGame.helper;

import MazeGame.Creature;
import MazeGame.Enemy;
import MazeGame.bullets.Bullet;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class enemyPositionRecorder {
    private int iPos;
    private int jPos;
    private Color color;
    private bulletPositionRecorder[] bullets;
    private Creature enemyReference;

    public enemyPositionRecorder(){
        iPos = -1;
        jPos = -1;
        enemyReference = null;
    }

    public int getiPos() {
        return iPos;
    }

    public void setiPos(int iPos) {
        this.iPos = iPos;
    }

    public int getjPos() {
        return jPos;
    }

    public void setjPos(int jPos) {
        this.jPos = jPos;
    }

    public void clear(){
        iPos = -1;
        jPos = -1;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public bulletPositionRecorder[]  getBullets() {
        return bullets;
    }

    public void setBullets(bulletPositionRecorder[] bullets) {
        this.bullets = bullets;
    }

    public Creature getEnemyReference() {
        return enemyReference;
    }

    public void setEnemyReference(Creature enemyReference) {
        this.enemyReference = enemyReference;
    }
}
