package MazeGame;

import MazeGame.weapons.Gun;
import MazeGame.weapons.NoWeapon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.roomSize;

public class Player extends Creature {

    // position
    private int x; // row number
    private int y; // column number
    private int roomI = 0;
    private int roomJ = 0;

    private String name = "Player 1";

    private int totalMapSize;
    private Cell[][] totalMap;
    private Room[][] rooms;
    private Weapon weapon;
    private CopyOnWriteArrayList<Enemy> enemies;
    private Random random = new Random();

    Player(int totalMapSize, int x, int y, Cell[][] totalMap, Room[][] rooms, CopyOnWriteArrayList<Enemy> enemies) {
        super(100, 100, 1);
        this.x = x;
        this.y = y;
        this.totalMapSize = totalMapSize;
        this.totalMap = totalMap;
        weapon = new Gun(Color.CYAN, getTeamNumber());
        this.enemies = enemies;
        this.rooms = rooms;
    }

    public ArrayList<Bullet> fire(int xDest, int yDest) {
        int yBorder = x - 27 > 0 ? x - 27 : 0;
        int xBorder = y - 40 > 0 ? y - 40 : 0;
        return weapon.CheckFireStatus(y * 15 + 1, x * 15 + 1, xDest + xBorder * 15 - 10, yDest + yBorder * 15 - 35);
    }

    public void move(String dir) {
        totalMap[x][y].setOccupiedCreature(null);
        int newX = x;
        int newY = y;
        if (dir.equals("up")) {
            newX = x - 1;
        } else if (dir.equals("down")) {
            newX = x + 1;
        } else if (dir.equals("left")) {
            newY = y - 1;
        } else if (dir.equals("right")) {
            newY = y + 1;
        }
        if (newX < 0) {
            newX = 0;
        }
        if (newY < 0) {
            newY = 0;
        }
        if (newX >= totalMapSize) {
            newX = totalMapSize - 1;
        }
        if (newY >= totalMapSize) {
            newY = totalMapSize - 1;
        }

        // check if new position is wall
        if (totalMap[newX][newY].boarder || totalMap[x][y].getOccupiedCreature() != null) {
            // is wall or other creature
            return;
        } else {
            x = newX;
            y = newY;
            totalMap[x][y].setOccupiedCreature(this);

            int tempI = x / roomSize;
            int tempJ = y / roomSize;
            if (tempI != roomI || tempJ != roomJ) {
                roomI = tempI;
                roomJ = tempJ;
                rooms[tempI][tempJ].visit(this);
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
            enemies.add(new Enemy(roomI * roomSize + tempI, roomJ * roomSize + tempJ, this, rooms, totalMap));
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
        if (totalMap[x][y].getFallenWeapon() != null) {
            Weapon tempWeapon = null;
            if (weapon.getDamage() != 0) {
                // means player equipped with a weapon
                tempWeapon = weapon;
            }
            weapon = totalMap[x][y].getFallenWeapon();
            totalMap[x][y].setFallenWeapon(tempWeapon);
        }
    }

    public void drop() {
        if (weapon.getDamage() != 0) {
            // means player equipped with a weapon
            if (totalMap[x][y].getFallenWeapon() == null) {
                totalMap[x][y].setFallenWeapon(weapon);
                weapon = new NoWeapon(Color.CYAN, getTeamNumber());
            }
        }
    }

    public void teleport(int x, int y) {
        this.x = x;
        this.y = y;
        roomI = x / roomSize;
        roomJ = y / roomSize;
    }
}
