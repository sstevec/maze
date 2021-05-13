package MazeGame.effect;

import MazeGame.Cell;
import MazeGame.Creature;
import MazeGame.helper.creaturePositionRecorder;

import java.awt.*;

import static MazeGame.Info.cellWidth;

public class Explosion extends Effect{

    private int currentRadius;
    private int effectRadius;
    private int maxRadius;
    private int damage;
    private boolean breakable = false;


    public Explosion(int x, int y, int maxRadius, int effectRadius, int damage, boolean breakable,
                     creaturePositionRecorder[] creatures){
        super(x,y, Color.RED, creatures);
        this.maxRadius = maxRadius;
        this.effectRadius = effectRadius;
        this.damage = damage;
        this.breakable = breakable;
    }

    public void doDamage(Cell[][] cells){
        dealDamage = true;
        int i = y/cellWidth;
        int j = x/cellWidth;

        for(creaturePositionRecorder enemy: creatures){
            Creature creature = enemy.getCreatureReference();
            if(creature != null){
                int iDis = creature.getiPos() - i;
                int jDis = creature.getjPos() - j;
                if(Math.sqrt(iDis * iDis + jDis * jDis) < effectRadius){
                    creature.takeDamage(damage);
                }
            }
        }

        if(breakable) {
            for (int startI = i - effectRadius + 1; startI < i + effectRadius; startI++) {
                for (int startJ = j - effectRadius + 1; startJ < j + effectRadius; startJ++) {
                    if (startI > 0 && startI < cells.length-1 && startJ > 0 && startJ < cells.length-1) {
                        if(cells[startI][startJ].isBoarder()){
                            cells[startI][startJ].setBoarder(false);
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
