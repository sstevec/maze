package MazeGame.helper;

import MazeGame.creature.Creature;

import java.awt.*;

public class CreaturePositionRecorder {
    private int iPos;
    private int jPos;
    protected double iDPos;
    protected double jDPos;
    private Color color;
    private BulletPositionRecorder[] bullets;
    private Creature creatureReference;

    public CreaturePositionRecorder(){
        iPos = -1;
        jPos = -1;
        creatureReference = null;
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

    public double getiDPos() {
        return iDPos;
    }

    public void setiDPos(double iDPos) {
        this.iDPos = iDPos;
    }

    public double getjDPos() {
        return jDPos;
    }

    public void setjDPos(double jDPos) {
        this.jDPos = jDPos;
    }

    public void clear(){
        iPos = -1;
        jPos = -1;
        iDPos = iPos;
        jDPos = jPos;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public BulletPositionRecorder[]  getBullets() {
        return bullets;
    }

    public void setBullets(BulletPositionRecorder[] bullets) {
        this.bullets = bullets;
    }

    public Creature getCreatureReference() {
        return creatureReference;
    }

    public void setCreatureReference(Creature creatureReference) {
        this.creatureReference = creatureReference;
    }
}
