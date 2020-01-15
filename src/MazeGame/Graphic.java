package MazeGame;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Graphic extends JPanel {

    private int xPos;
    private int yPos;
    private Cell[][] totalMap;
    private int mapSize;
    private CopyOnWriteArrayList<Bullet> bullets;
    private Player playerInfo;
    private CopyOnWriteArrayList<Enemy> enemies;
    private Font itemFont = new Font("Cosmic",Font.BOLD,12);

    Graphic(Cell[][] totalMap, Player player,CopyOnWriteArrayList<Bullet> bullets, CopyOnWriteArrayList<Enemy> enemies){
        this.totalMap = totalMap;
        mapSize = totalMap.length;
        this.playerInfo = player;
        this.enemies = enemies;
        this.bullets = bullets;
    }

    public void draw(int x, int y){
        xPos = x;
        yPos = y;
        repaint();
    }

    public void drawElements(){
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
                if(totalMap[startX+i][startY+j].boarder){
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
                    graphics.setColor(Color.GREEN);
                    graphics.drawString("| " + totalMap[startX+i][startY+j].getFallenWeapon().getName() + " |",j*15-5,i*15+15);
                }
            }
        }

        for (Bullet temp: bullets
             ) {
            graphics.setColor(temp.getColor());
            graphics.fillArc((int)Math.round(temp.getX()-(startY*15)),(int)Math.round(temp.getY()-(startX*15)),10,10,0,360);
        }

        graphics.setColor(Color.MAGENTA);
        for (Enemy temp: enemies
             ) {
            graphics.fillRect((int)(temp.getjPos()-startY)*15, (int)(temp.getiPos()-startX)*15,15,15);
        }

        graphics.setColor(Color.lightGray);
        graphics.fillRect(0,(endX+1)*15,1215,825-(endX+1)*15);
        graphics.fillRect((endY+1)*15,0,1215-(endY+1)*15,825);

        graphics.setColor(Color.BLACK);
        graphics.drawString(playerInfo.getCurrentHealth() + " / " + playerInfo.getMaxHealth(),(yPos-startY)*15-15,(xPos-startX)*15-5);
        graphics.setColor(Color.cyan);
        graphics.fillRect((yPos-startY)*15,(xPos-startX)*15,15,15);
    }

}
