package MazeGame;

import MazeGame.bullets.Bullet;
import MazeGame.effect.Effect;
import MazeGame.helper.enemyPositionRecorder;
import MazeGame.weapons.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.roomSize;
import static MazeGame.Info.traceRange;

public class Player extends Creature {

    // position
    private int x; // row number
    private int y; // column number
    private int roomI = 0;
    private int roomJ = 0;

    private String name = "Player 1";

    private int mapSize;
    private int totalMapSize;
    private Room[][] rooms;
    private CopyOnWriteArrayList<Effect> effects;
    private Weapon weapon;
    private enemyPositionRecorder[] enemies;
    private Random random = new Random();
    private ConcurrentHashMap<String,Integer> movePriorityList = new ConcurrentHashMap<>();


    Player(int totalMapSize, int x, int y, Cell[][] totalMap, Room[][] rooms, CopyOnWriteArrayList<Effect> effects, enemyPositionRecorder[] enemies) {
        super(100, 100, 1, totalMap, enemies);
        this.x = x;
        this.y = y;
        iPos = x;
        jPos = y;
        this.totalMapSize = totalMapSize;
        this.color = Color.CYAN;
        this.effects = effects;
        weapon = new Rocket(color, getTeamNumber(), effects, this);
        this.enemies = enemies;
        this.rooms = rooms;
        this.mapSize = rooms.length;
    }

    public void fire(int xDest, int yDest) {
        int yBorder = x - 27 > 0 ? x - 27 : 0;
        int xBorder = y - 40 > 0 ? y - 40 : 0;
        ArrayList<Bullet> tempList = weapon.CheckFireStatus(y * 15 + 1, x * 15 + 1, xDest + xBorder * 15 - 10, yDest + yBorder * 15 - 35);
        if(tempList != null){
            addBullets(tempList);
        }
    }

    public void castAbility(int xDest, int yDest) {
        int yBorder = x - 27 > 0 ? x - 27 : 0;
        int xBorder = y - 40 > 0 ? y - 40 : 0;
        ArrayList<Bullet> tempList = weapon.CheckAbilityStatus(y * 15 + 1, x * 15 + 1, xDest + xBorder * 15 - 10, yDest + yBorder * 15 - 35);
        if(tempList != null){
            addBullets(tempList);
        }
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
        roomI = x / roomSize;
        roomJ = y / roomSize;


    }


    @Override
    public void dieEffect() {

    }

    public void pick() {
        if (cellInfo[x][y].getFallenWeapon() != null) {
            Weapon tempWeapon = null;
            if (weapon.getDamage() != 0) {
                // means player equipped with a weapon
                tempWeapon = weapon;
            }
            weapon = cellInfo[x][y].getFallenWeapon();
            weapon.setColor(color);
            weapon.setBelongTeam(getTeamNumber());
            cellInfo[x][y].setFallenWeapon(tempWeapon);
        }
    }

    public void drop() {
        if (weapon.getDamage() != 0) {
            // means player equipped with a weapon
            if (cellInfo[x][y].getFallenWeapon() == null) {
                cellInfo[x][y].setFallenWeapon(weapon);
                weapon = new NoWeapon(Color.CYAN, getTeamNumber(), effects, this);
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
