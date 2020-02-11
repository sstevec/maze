package MazeGame;

import MazeGame.bullets.Bullet;
import MazeGame.effect.Effect;
import MazeGame.effect.Explosion;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Graphic extends JPanel {

    private int xPos;
    private int yPos;
    private Cell[][] totalMap;
    private int mapSize;
    private Player playerInfo;
    private CopyOnWriteArrayList<Enemy> enemies;
    private CopyOnWriteArrayList<Effect> effects;
    private Font itemFont = new Font("Cosmic",Font.BOLD,12);

    Graphic(Cell[][] totalMap, Player player, CopyOnWriteArrayList<Enemy> enemies, CopyOnWriteArrayList<Effect> effects){
        this.setBounds(0,0,1215,825);
        this.totalMap = totalMap;
        mapSize = totalMap.length;
        this.playerInfo = player;
        this.enemies = enemies;
        this.effects = effects;
    }


    public void drawElements(){
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
        if(startX < 0){
            startX = 0;
        }
        if(endX >=  mapSize){
            endX = mapSize-1;
        }

        int startY = yPos - 40;
        int endY = yPos + 40;
        if(startY < 0){
            startY = 0;
        }
        if(endY >=  mapSize){
            endY = mapSize-1;
        }

        int xLength = endX - startX;
        int yLength = endY - startY;

        graphics.setFont(itemFont);

        for(int i = 0; i<= xLength; i++){
            for(int j = 0; j<= yLength; j++){
                if(totalMap[startX+i][startY+j].isBoarder()){
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(j*15,i*15,15,15);
                }else if(totalMap[startX+i][startY+j].getIntractable() != null){
                    graphics.setColor(totalMap[startX+i][startY+j].getIntractable().getColor());
                    graphics.fillRect(j*15,i*15,15,15);
                }else{
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(j*15,i*15,15,15);
                }
            }
        }

        for(int i = 0; i<= xLength; i++){
            for(int j = 0; j<= yLength; j++){
                if(totalMap[startX+i][startY+j].getFallenWeapon() != null){
                    graphics.setColor(Color.BLUE);
                    graphics.drawString("| " + totalMap[startX+i][startY+j].getFallenWeapon().getName() + " |",j*15-5,i*15+15);
                }
            }
        }

        for (Enemy temp: enemies
             ) {
            graphics.setColor(temp.getColor());
            graphics.fillRect((int)(temp.getjPos()-startY)*15, (int)(temp.getiPos()-startX)*15,15,15);

            CopyOnWriteArrayList<Bullet> bullets = temp.getBullets();
            for (Bullet tempB: bullets
            ) {
                graphics.fillArc((int)Math.round(tempB.getX()-(startY*15)),(int)Math.round(tempB.getY()-(startX*15)),10,10,0,360);
            }
        }

        for(Effect temp : effects){
            graphics.setColor(temp.getColor());
            if(temp instanceof Explosion){
                int radius = ((Explosion) temp).getCurrentRadius();
                graphics.drawOval(temp.getX()-(startY*15)- radius/2,temp.getY()-(startX*15) - radius/2,radius,radius);
            }
        }

        graphics.setColor(Color.lightGray);
        graphics.fillRect(0,(endX+1)*15,1215,825-(endX+1)*15);
        graphics.fillRect((endY+1)*15,0,1215-(endY+1)*15,825);

        graphics.setColor(Color.BLACK);
        graphics.drawString(playerInfo.getCurrentHealth() + " / " + playerInfo.getMaxHealth(),(yPos-startY)*15-15,(xPos-startX)*15-5);
        graphics.setColor(Color.cyan);
        graphics.fillRect((yPos-startY)*15,(xPos-startX)*15,15,15);

        CopyOnWriteArrayList<Bullet> bullets = playerInfo.getBullets();
        for (Bullet tempB: bullets
        ) {
            graphics.fillArc((int)Math.round(tempB.getX()-(startY*15)),(int)Math.round(tempB.getY()-(startX*15)),10,10,0,360);
        }
    }

}
