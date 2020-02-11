package MazeGame.effect;

import MazeGame.Cell;

import java.awt.*;

import static MazeGame.Info.cellWidth;
import static MazeGame.Info.roomSize;

public class Explosion extends Effect{

    private int currentRadius;
    private int effectRadius;
    private int maxRadius;
    private int damage;
    private boolean breakable = false;

    public Explosion(int x, int y, int maxRadius, int effectRadius, int damage, boolean breakable){
        super(x,y, Color.RED);
        this.maxRadius = maxRadius;
        this.effectRadius = effectRadius;
        this.damage = damage;
        this.breakable = breakable;
    }

    public void doDamage(Cell[][] cells){
        dealDamage = true;
        int i = y/cellWidth;
        int j = x/cellWidth;
        synchronized (cells) {
            for (int x = i - effectRadius; x <= i + effectRadius; x++) {
                for (int y = j - effectRadius; y <= j + effectRadius; y++) {
                    if (x > 0 && y > 0) {
                        if (x < roomSize * 10 - 1 && y < roomSize * 10 - 1) {
                            if (cells[x][y].getOccupiedCreature() != null) {
                                cells[x][y].getOccupiedCreature().takeDamage(damage);
                            }
                            if (breakable) {
                                if (cells[x][y].isBoarder()) {
                                    cells[x][y].setBoarder(false);
                                }
                            }
                        }
                    }
                }
            }
        }
        animate();
    }

    public void animate(){
        currentRadius+=10;
    }

    public boolean disappear() {
        return currentRadius >= maxRadius;
    }

    public int getCurrentRadius() {
        return currentRadius;
    }
}
