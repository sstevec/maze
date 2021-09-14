package MazeGame.graphic;

import MazeGame.map.Cell;
import MazeGame.creature.Creature;
import MazeGame.GameResourceController;
import MazeGame.creature.Player;
import MazeGame.bullets.Bullet;
import MazeGame.effect.Effect;
import MazeGame.effect.Explosion;
import MazeGame.helper.BulletPositionRecorder;
import MazeGame.helper.CreaturePositionRecorder;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Graphic extends JPanel {

    private double playerIPos;
    private double playerJPos;
    private Cell[][] totalMap;
    private int totalMapSize;
    private Player playerInfo;
    private CreaturePositionRecorder[] creatures;
    private CopyOnWriteArrayList<Effect> effects;
    private Font itemFont = new Font("Cosmic", Font.BOLD, 12);
    private GameResourceController gameResourceController;

    public Graphic(GameResourceController gameResourceController) {
        this.gameResourceController = gameResourceController;
        this.setBounds(0, 0, 1215, 825);
        this.totalMap = gameResourceController.getTotalMap();
        totalMapSize = gameResourceController.getTotalMapSize();
        this.playerInfo = gameResourceController.getPlayer();
        this.creatures = gameResourceController.getCreatures();
        this.effects = gameResourceController.getEffects();
    }


    public void drawElements() {
        repaint();
    }

    public void updateViewLocation(){
        playerIPos = gameResourceController.getLastIPos();
        playerJPos = gameResourceController.getLastJPos();
    }

    public void paint(Graphics graphics) {
        // use father class
        super.paint(graphics);
        // calculate start point
        double startX = playerIPos - 27;
        double endX = playerIPos + 27;
        if (startX < 0) {
            startX = 0;
            endX = 54;
        }
        if (endX >= totalMapSize) {
            endX = totalMapSize - 1;
            startX = totalMapSize - 55;
        }

        double startY = playerJPos - 40;
        double endY = playerJPos + 40;
        if (startY < 0) {
            startY = 0;
            endY = 80;
        }
        if (endY >= totalMapSize) {
            endY = totalMapSize - 1;
            startY = totalMapSize - 81;
        }

        double xLength = endX - startX;
        double yLength = endY - startY;

        graphics.setFont(itemFont);

        for (int i = 0; i <= xLength; i++) {
            for (int j = 0; j <= yLength; j++) {
                if (totalMap[(int)startX + i][(int)startY + j].isBoarder()) {
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(j * 15, i * 15, 15, 15);
                } else if (totalMap[(int)startX + i][(int)startY + j].getIntractable() != null) {
                    graphics.setColor(totalMap[(int)startX + i][(int)startY + j].getIntractable().getColor());
                    graphics.fillRect(j * 15, i * 15, 15, 15);
                } else {
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(j * 15, i * 15, 15, 15);
                }
            }
        }

        for (int i = 0; i <= xLength; i++) {
            for (int j = 0; j <= yLength; j++) {
                if (totalMap[(int)startX + i][(int)startY + j].getFallenItem() != null) {
                    graphics.setColor(Color.BLUE);
                    graphics.drawString("| " + totalMap[(int)startX + i][(int)startY + j].getFallenItem().getName() + " |", j * 15 - 5, i * 15 + 15);
                }
            }
        }

        for (CreaturePositionRecorder temp : creatures
        ) {
            Creature creature = temp.getCreatureReference();
            if (creature == null) {
                continue;
            }
            double iPos = temp.getiDPos();
            double jPos = temp.getjDPos();

            graphics.setColor(temp.getColor());
            graphics.drawString(creature.getCurrentHealth() + " / " + creature.getMaxHealth(), (int)((jPos - startY) * 15 - 15), (int)((iPos - startX) * 15 - 5));

            graphics.fillRect((int)((jPos - startY) * 15), (int)((iPos - startX) * 15), 15, 15);

            BulletPositionRecorder[] bullets = creature.getBullets();
            for (BulletPositionRecorder tempC : bullets
            ) {
                Bullet tempB = tempC.getBulletReference();
                if (tempB == null) {
                    continue;
                }
                graphics.fillArc((int) Math.round(tempB.getX() - (startY * 15)), (int) Math.round(tempB.getY() - (startX * 15)), 10, 10, 0, 360);
            }
        }

        for (Effect temp : effects) {
            graphics.setColor(temp.getColor());
            if (temp instanceof Explosion) {
                int radius = ((Explosion) temp).getCurrentRadius();
                graphics.drawOval((int)(temp.getX() - (startY * 15) - radius / 2), (int)(temp.getY() - (startX * 15) - radius / 2), radius, radius);
            }
        }
    }

}
