package MazeGame;

import MazeGame.bullets.Bullet;
import MazeGame.effect.Effect;
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
    private CopyOnWriteArrayList<Enemy> enemies;
    private Random random = new Random();
    private ConcurrentHashMap<String,Integer> movePriorityList = new ConcurrentHashMap<>();

    Player(int totalMapSize, int x, int y, Cell[][] totalMap, Room[][] rooms, CopyOnWriteArrayList<Enemy> enemies, CopyOnWriteArrayList<Effect> effects) {
        super(100, 100, 1, totalMap);
        this.x = x;
        this.y = y;
        iPos = x;
        jPos = y;
        this.totalMapSize = totalMapSize;
        this.color = Color.CYAN;
        this.effects = effects;
        weapon = new Rocket(color, getTeamNumber(), effects);
        this.enemies = enemies;
        this.rooms = rooms;
        this.mapSize = rooms.length;
    }

    public void fire(int xDest, int yDest) {
        int yBorder = x - 27 > 0 ? x - 27 : 0;
        int xBorder = y - 40 > 0 ? y - 40 : 0;
        ArrayList<Bullet> tempList = weapon.CheckFireStatus(y * 15 + 1, x * 15 + 1, xDest + xBorder * 15 - 10, yDest + yBorder * 15 - 35);
        if(tempList != null){
            bullets.addAll(tempList);
        }
    }

    public void castAbility(int xDest, int yDest) {
        int yBorder = x - 27 > 0 ? x - 27 : 0;
        int xBorder = y - 40 > 0 ? y - 40 : 0;
        ArrayList<Bullet> tempList = weapon.CheckAbilityStatus(y * 15 + 1, x * 15 + 1, xDest + xBorder * 15 - 10, yDest + yBorder * 15 - 35);
        if(tempList != null){
            bullets.addAll(tempList);
        }
    }

    public void move(String dir) {
        cellInfo[x][y].setOccupiedCreature(null);
        double newX = iPos;
        double newY = jPos;
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
        if (cellInfo[(int)newX][(int)newY].isBoarder() || cellInfo[x][y].getOccupiedCreature() != null) {
            // is wall or other creature
            return;
        } else {
            iPos = newX;
            jPos = newY;
            x = (int)newX;
            y = (int)newY;
            cellInfo[x][y].setOccupiedCreature(this);

            int tempI = x / roomSize;
            int tempJ = y / roomSize;
            if (tempI != roomI || tempJ != roomJ) {
                roomI = tempI;
                roomJ = tempJ;
                rooms[tempI][tempJ].visit(this);
                checkRoomOpenness();
                synchronized (movePriorityList){
                    clearPriorityList();
                    setMovePriority(roomI, roomJ, traceRange, -1);
                }
            } else {
                roomI = tempI;
                roomJ = tempJ;
            }

        }
    }

    public void sumEnemy(int number) {
        for (int i = 0; i < number; i++) {
            int tempI = random.nextInt(roomSize - 5) + 2;
            int tempJ = random.nextInt(roomSize - 5) + 2;
            enemies.add(new Enemy(roomI * roomSize + tempI, roomJ * roomSize + tempJ, this, rooms, cellInfo,movePriorityList, effects));
        }
    }

    @Override
    public void die() {

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
                weapon = new NoWeapon(Color.CYAN, getTeamNumber(), effects);
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

    private void clearPriorityList() {
        movePriorityList.clear();
    }

    private void setMovePriority(int i, int j, int priorityNumber, int comeDir) {
        if (priorityNumber == 0) {
            return;
        } else {
            if(movePriorityList.get(i+","+j) == null || movePriorityList.get(i+","+j) < priorityNumber) {
                movePriorityList.put(i + "," + j, priorityNumber);
            }

            for (int k = 0; k < 4; k++) {
                if (k == comeDir) {
                    continue;
                }
                if (k == 0) {
                    // check top
                    if (rooms[i][j].getOpenTop() && i - 1 >= 0 && rooms[i - 1][j].getOpenBot()) {
                        // can go to top room
                        setMovePriority(i - 1, j, priorityNumber - 1, 2);
                    } else {
                        continue;
                    }
                }

                if (k == 1) {
                    // check right
                    if (rooms[i][j].getOpenRight() && j + 1 < mapSize && rooms[i][j + 1].getOpenLeft()) {
                        // can go to right room
                        setMovePriority(i, j + 1, priorityNumber - 1, 3);
                    } else {
                        continue;
                    }
                }

                if (k == 2) {
                    // check bot
                    if (rooms[i][j].getOpenBot() && i + 1 < mapSize && rooms[i + 1][j].getOpenTop()) {
                        // can go to bot room
                        setMovePriority(i + 1, j, priorityNumber - 1, 0);
                    } else {
                        continue;
                    }
                }

                if (k == 3) {
                    // check left
                    if (rooms[i][j].getOpenLeft() && j - 1 >= 0 && rooms[i][j - 1].getOpenRight()) {
                        // can go to left room
                        setMovePriority(i, j - 1, priorityNumber - 1, 1);
                    }
                }
            }
        }
    }

    public double getWeaponCD(){
        return weapon.getCD();
    }

    private void checkRoomOpenness(){
        rooms[roomI][roomJ].checkOpenness();
        if(roomI - 1 >= 0){
            rooms[roomI - 1][roomJ].checkOpenness();
        }

        if(roomJ - 1 >= 0){
            rooms[roomI][roomJ - 1].checkOpenness();
        }

        if(roomI + 1 < rooms.length){
            rooms[roomI + 1][roomJ].checkOpenness();
        }

        if(roomJ + 1 < rooms.length){
            rooms[roomI][roomJ + 1].checkOpenness();
        }
    }
}
