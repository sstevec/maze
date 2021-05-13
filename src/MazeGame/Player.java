package MazeGame;

import MazeGame.effect.Effect;
import MazeGame.equipment.Equipment;
import MazeGame.helper.creaturePositionRecorder;
import MazeGame.weapons.*;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.*;

public class Player extends Creature {

    // position
    private int x; // cell level row number
    private int y; // cell level column number
    private int roomI = 0;
    private int roomJ = 0;

    private String name = "Player 1";

    private int mapSize;
    private int totalMapSize;
    private Room[][] rooms;
    private CopyOnWriteArrayList<Effect> effects;
    private Random random = new Random();
    private ConcurrentHashMap<String,Integer> movePriorityList = new ConcurrentHashMap<>();
    private GameInitializer gameInitializer;


    Player(int x, int y, GameResourceController gameResourceController) {
        super(100, 100, 1, gameResourceController);
        this.x = x;
        this.y = y;
        iPos = x;
        jPos = y;
        this.totalMapSize = gameResourceController.getTotalMapSize();
        this.color = Color.CYAN;
        this.effects = gameResourceController.getEffects();
        weapon = new MindControlGun(color, getTeamNumber(), effects, this);
        this.rooms = gameResourceController.getRooms();
        this.mapSize = rooms.length;
        this.gameInitializer = gameResourceController.getGameInitializer();
    }

    @Override
    protected void customInit() {
    }

    /***
     *
     * @param xDest
     * @param yDest
     *
     * Note here xDest and yDest are relative position of mouse to the current game board
     * Thus, to calculate the definite position, we need to plus the board distance
     */

    public void fire(int xDest, int yDest) {
        int yBorder = Math.max(x - 27, 0);
        int xBorder = Math.max(y - 40, 0);
        yBorder = Math.min(yBorder, roomSize*mapSize - 55);
        xBorder = Math.min(xBorder, roomSize*mapSize - 81);
        weapon.CheckFireStatus(y * Info.cellWidth + Info.cellWidth/2, x * Info.cellWidth + Info.cellWidth/2, xDest + xBorder * Info.cellWidth - 10, yDest + yBorder * Info.cellWidth - 35);
    }

    public void castAbility(int xDest, int yDest) {
        int yBorder = Math.max(x - 27, 0);
        int xBorder = Math.max(y - 40, 0);
        yBorder = Math.min(yBorder, roomSize*mapSize - 55);
        xBorder = Math.min(xBorder, roomSize*mapSize - 81);
        weapon.CheckAbilityStatus(y * Info.cellWidth + Info.cellWidth/2, x * Info.cellWidth + Info.cellWidth/2, xDest + xBorder * Info.cellWidth - 10, yDest + yBorder * Info.cellWidth - 35);
    }

    public void move(String dir) {
        int newX = iPos;
        int newY = jPos;
        if (dir.equals("up")) {
            newX = iPos - 1;
        } else if (dir.equals("down")) {
            newX = iPos + 1;
        } else if (dir.equals("left")) {
            newY = jPos - 1;
        } else if (dir.equals("right")) {
            newY = jPos + 1;
        }
        if (newX < 1) {
            newX = 1;
        }
        if (newY < 1) {
            newY = 1;
        }
        if (newX >= totalMapSize) {
            newX = totalMapSize - 1;
        }
        if (newY >= totalMapSize) {
            newY = totalMapSize - 1;
        }

        // check if new position is wall
        if(cellInfo[newX][newY].isBoarder()){
            return;
        }

        iPos = newX;
        jPos = newY;
        x =  newX;
        y =  newY;
        creatures[0].setiPos(iPos);
        creatures[0].setjPos(jPos);
        roomI = x / roomSize;
        roomJ = y / roomSize;


    }


    @Override
    public void dieClear() {
        gameResourceController.getGameController().gameOver();
    }

    @Override
    public void dieEffect() {

    }

    public void pick() {
        for(int i = -3; i<4; i++){
            for(int j = -3; j<4; j++){
                if(x+i < cellInfo.length && x+i >=0 && y+j < cellInfo.length && y+j >=0){
                    Item fallenItem = cellInfo[x+i][y+j].getFallenItem();
                    if ( fallenItem != null) {
                        // there is an item
                        if(fallenItem.getKind() == WEAPON_KIND){
                            // it is a weapon
                            Weapon tempWeapon = null;
                            if (weapon.getDamage() != 0) {
                                // means player equipped with a weapon
                                tempWeapon = weapon;
                            }
                            weapon = (Weapon) fallenItem;
                            weapon.setColor(color);
                            weapon.setBelongTeam(getTeamNumber());
                            weapon.setUser(this);
                            weapon.setEffects(effects);
                            weapon.upgradeAttackSpeed(extraAttackSpeed);
                            weapon.upgradeDamage(extraDamage);
                            weapon.upgradeExtraCD(extraCDReduce);
                            cellInfo[x+i][y+j].setFallenItem(tempWeapon);
                            return;
                        } else if(fallenItem.getKind() == EQUIPMENT_KIND){
                            // it is a equipment
                            Equipment fallenEquipment = (Equipment)fallenItem;
                            fallenEquipment.equip(this);
                            cellInfo[x+i][y+j].setFallenItem(null);
                            return;
                        }

                    }
                }

            }
        }

    }

    public void drop() {
        if (weapon.getDamage() != 0) {
            // means player equipped with a weapon
            if (cellInfo[x][y].getFallenItem() == null) {
                cellInfo[x][y].setFallenItem(weapon);
                weapon = new NoWeapon(Color.CYAN, getTeamNumber(), effects, this);
            }
        }
    }

    public void interact(){
        for(int i = -3; i<4; i++){
            for(int j = -3; j<4; j++){
                if(x+i < cellInfo.length && x+i >=0 && y+j < cellInfo.length && y+j >=0){
                    if (cellInfo[x+i][y+j].getIntractable() != null) {
                        cellInfo[x+i][y+j].getIntractable().interact(gameResourceController);
                        return;
                    }
                }

            }
        }
    }

    public void teleport(int x, int y) {
        this.x = x;
        this.y = y;
        this.iPos = x;
        this.jPos = y;
        roomI = x / roomSize;
        roomJ = y / roomSize;
    }

    public double getWeaponCD() {
        return weapon.getCD();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getRoomI() {
        return roomI;
    }

    public int getRoomJ() {
        return roomJ;
    }
}
