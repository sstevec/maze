package MazeGame;

import MazeGame.bullets.Bullet;
import MazeGame.effect.Effect;
import MazeGame.effect.Explosion;
import MazeGame.helper.bulletPositionRecorder;
import MazeGame.helper.creaturePositionRecorder;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Graphic extends JPanel {

    private int xPos;
    private int yPos;
    private Cell[][] totalMap;
    private int totalMapSize;
    private Player playerInfo;
    private creaturePositionRecorder[] creatures;
    private CopyOnWriteArrayList<Effect> effects;
    private Font itemFont = new Font("Cosmic", Font.BOLD, 12);

    Graphic(GameResourceController gameResourceController) {
        this.setBounds(0, 0, 1215, 825);
        this.totalMap = gameResourceController.getTotalMap();
        totalMapSize = gameResourceController.getTotalMapSize();
        this.playerInfo = gameResourceController.getPlayer();
        this.creatures = gameResourceController.getCreatures();
        this.effects = gameResourceController.getEffects();
    }


    public void drawElements() {
        xPos = playerInfo.getX();
        yPos = playerInfo.getY();
        repaint();
    }


    public void paint(Graphics graphics) {
        // use father class
        super.paint(graphics);
        // calculate start point
        int startX = xPos - 27;
        int endX = xPos + 27;
        if (startX < 0) {
            startX = 0;
            endX = 54;
        }
        if (endX >= totalMapSize) {
            endX = totalMapSize - 1;
            startX = totalMapSize - 55;
        }

        int startY = yPos - 40;
        int endY = yPos + 40;
        if (startY < 0) {
            startY = 0;
            endY = 80;
        }
        if (endY >= totalMapSize) {
            endY = totalMapSize - 1;
            startY = totalMapSize - 81;
        }

        int xLength = endX - startX;
        int yLength = endY - startY;

        graphics.setFont(itemFont);

        for (int i = 0; i <= xLength; i++) {
            for (int j = 0; j <= yLength; j++) {
                if (totalMap[startX + i][startY + j].isBoarder()) {
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(j * 15, i * 15, 15, 15);
                } else if (totalMap[startX + i][startY + j].getIntractable() != null) {
                    graphics.setColor(totalMap[startX + i][startY + j].getIntractable().getColor());
                    graphics.fillRect(j * 15, i * 15, 15, 15);
                } else {
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(j * 15, i * 15, 15, 15);
                }
            }
        }

        for (int i = 0; i <= xLength; i++) {
            for (int j = 0; j <= yLength; j++) {
                if (totalMap[startX + i][startY + j].getFallenItem() != null) {
                    graphics.setColor(Color.BLUE);
                    graphics.drawString("| " + totalMap[startX + i][startY + j].getFallenItem().getName() + " |", j * 15 - 5, i * 15 + 15);
                }
            }
        }

        for (creaturePositionRecorder temp : creatures
        ) {
            Creature creature = temp.getCreatureReference();
            if (creature == null) {
                continue;
            }
            int iPos = temp.getiPos();
            int jPos = temp.getjPos();

            graphics.setColor(temp.getColor());
            graphics.drawString(creature.getCurrentHealth() + " / " + creature.getMaxHealth(), (jPos - startY) * 15 - 15, (iPos - startX) * 15 - 5);

            graphics.fillRect((jPos - startY) * 15, (iPos - startX) * 15, 15, 15);

            bulletPositionRecorder[] bullets = creature.getBullets();
            for (bulletPositionRecorder tempC : bullets
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
                graphics.drawOval(temp.getX() - (startY * 15) - radius / 2, temp.getY() - (startX * 15) - radius / 2, radius, radius);
            }
        }


//        graphics.setColor(Color.BLACK);
//        graphics.drawString(playerInfo.getCurrentHealth() + " / " + playerInfo.getMaxHealth(), (yPos - startY) * 15 - 15, (xPos - startX) * 15 - 5);
//        graphics.setColor(Color.cyan);
//        graphics.fillRect((yPos - startY) * 15, (xPos - startX) * 15, 15, 15);
//
//        bulletPositionRecorder[] bullets = playerInfo.getBullets();
//        for (bulletPositionRecorder tempC : bullets
//        ) {
//            Bullet tempB = tempC.getBulletReference();
//            if (tempB == null) {
//                continue;
//            }
//            graphics.fillArc((int) Math.round(tempB.getX() - (startY * 15)), (int) Math.round(tempB.getY() - (startX * 15)), 10, 10, 0, 360);
//        }
    }

}
