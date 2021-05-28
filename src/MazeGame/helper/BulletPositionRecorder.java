package MazeGame.helper;

import MazeGame.bullets.Bullet;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class BulletPositionRecorder {

    private int iPos;
    private int jPos;
    private Color color;
    private Bullet bulletReference;

    public BulletPositionRecorder(){
        iPos = -1;
        jPos = -1;
        bulletReference = null;
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

    public Bullet getBulletReference() {
        return bulletReference;
    }

    public void setBulletReference(Bullet bulletReference) {
        this.bulletReference = bulletReference;
    }
}
