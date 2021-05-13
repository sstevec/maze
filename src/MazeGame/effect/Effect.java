package MazeGame.effect;

import MazeGame.Cell;
import MazeGame.helper.creaturePositionRecorder;

import java.awt.*;

public abstract class Effect {
    protected int x;
    protected int y;
    protected boolean dealDamage = false;
    private Color color;
    protected creaturePositionRecorder[] creatures;

    public Effect(int x, int y, Color color, creaturePositionRecorder[] creatures){
        this.y = y;
        this.x = x;
        this.color = color;
        this.creatures = creatures;
    }

    public abstract void doDamage(Cell[][] cells);

    public abstract void animate();

    public abstract boolean disappear();

    public boolean isDealDamage() {
        return dealDamage;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
