package MazeGame;

import MazeGame.bullets.Bullet;
import MazeGame.effect.Effect;
import MazeGame.helper.Coordinate;
import MazeGame.helper.Point;
import MazeGame.helper.enemyPositionRecorder;
import MazeGame.weapons.BrokenGun;
import MazeGame.weapons.Weapon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.*;

public class Enemy extends Creature implements Runnable {

    private int iRoom;
    private int jRoom;
    private Player playerInfo;
    private Creature betrayedTarget;
    private boolean betraied = false;
    private Weapon weapon;
    private enemyPositionRecorder[] enemiesSlot;
    private ArrayList<Integer> avaSlot;
    private int assignPosition;
    private Point[][] map;
    private ArrayList<Coordinate> openSupport;
    private int[] moveDirX = {1, 1, 1, 0, 0, -1, -1, -1};
    private int[] moveDirY = {1, 0, -1, 1, -1, 1, 0, -1};
    private int mapUpdateCounter = 0;

    private Room[][] roomInfo; // a reference of room info

    private CopyOnWriteArrayList<Effect> effects = new CopyOnWriteArrayList<>();

    private Timer moveDriver = new Timer();
    private Timer shotDriver = new Timer();

    private int mapSize;

    private int moveSpeed = 3; //default
    private int attackRate = 2; //default

    private ArrayList<Coordinate> moveQueue = new ArrayList<>();

    public Enemy(int roomI, int roomJ, Player player, Room[][] rooms, Cell[][] cells, enemyPositionRecorder[] enemies, ArrayList<Integer> avaSlot) {
        // in future you should move these property to child class
        super(100, 100, 0, cells, player);

        this.iRoom = roomI;
        this.jRoom = roomJ;
        roomInfo = rooms;
        mapSize = rooms.length;
        this.cellInfo = cells;
        playerInfo = player;
        this.color = Color.MAGENTA;
        weapon = new BrokenGun(color, getTeamNumber(), effects, this);
        this.enemiesSlot = enemies;
        this.avaSlot = avaSlot;

    }

    @Override
    public void run() {
        init();
        initDriver();
    }

    private void init() {
        while (true) {
            synchronized (avaSlot) {
                if (!avaSlot.isEmpty()) {
                    assignPosition = avaSlot.remove(0);
                    break;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("ERROR: enemy sleep fail");
            }

        }

        openSupport = new ArrayList<>();
        Random random = new Random();
        iPos = iRoom * roomSize + random.nextInt(roomSize - 11) + 5;
        jPos = jRoom * roomSize + random.nextInt(roomSize - 11) + 5;

        // create a map used to find way
        map = new Point[cellInfo.length][cellInfo.length];
        for (int i = 0; i < cellInfo.length; i++) {
            for (int j = 0; j < cellInfo.length; j++) {
                map[i][j] = new Point();
                map[i][j].setWall(cellInfo[i][j].isBoarder());
            }
        }

        enemiesSlot[assignPosition].setiPos(iPos);
        enemiesSlot[assignPosition].setjPos(jPos);
        enemiesSlot[assignPosition].setColor(color);
        enemiesSlot[assignPosition].setBullets(bullets);
        enemiesSlot[assignPosition].setEnemyReference(this);
    }

    private void move() {
        mapUpdateCounter++;
        if (mapUpdateCounter == moveSpeed * 30) {
            updateMap();
            mapUpdateCounter = 0;
        }

        int playerRoomI = playerInfo.getRoomI();
        int playerRoomJ = playerInfo.getRoomJ();

        if (iRoom == playerRoomI && jRoom == playerRoomJ) {
            // in the same room
            synchronized (moveQueue) {
                moveQueue.clear();
            }
            clearMap();
            // random move in the room
            moveAround();
        } else {
            // try to move
            synchronized (moveQueue) {
                if (moveQueue.isEmpty()) {
                    // generate a new move queue
                    clearMap();
                    generateMoveQueue();
                } else {
                    // follow the move queue
                    Coordinate moveTarget;

                    moveTarget = moveQueue.remove(moveQueue.size() - 1);

                    // update my position
                    int oldI = iPos;
                    int oldJ = jPos;
                    iPos = moveTarget.getRow();
                    jPos = moveTarget.getColumn();
                    if (oldI == iPos && oldJ == jPos) {
                        return;
                    }

                    //System.out.println("old position is: " + oldI + "," + oldJ + "   " + iPos + ","+jPos);

                    // update room
                    iRoom = iPos / roomSize;
                    jRoom = jPos / roomSize;

                    // update graphic position
                    enemiesSlot[assignPosition].setiPos(iPos);
                    enemiesSlot[assignPosition].setjPos(jPos);
                }
            }
        }
    }

    private void updateMap() {
        for (int i = 0; i < cellInfo.length; i++) {
            for (int j = 0; j < cellInfo.length; j++) {
                map[i][j].setWall(cellInfo[i][j].isBoarder());
            }
        }
    }

    private void clearMap() {
        synchronized (openSupport) {
            openSupport.clear();
        }

        synchronized (map) {
            for (int i = 0; i < cellInfo.length; i++) {
                for (int j = 0; j < cellInfo.length; j++) {
                    map[i][j].clear();
                }
            }
        }
    }

    private ArrayList<Bullet> tryToShot(Creature target) {
        if (target == null) {
            return null;
        }
        int targetRoomI = target.iPos / roomSize;
        int targetRoomJ = target.jPos / roomSize;

        if (iRoom == targetRoomI && jRoom == targetRoomJ) {
            // in the same room
            return weapon.CheckFireStatus(jPos * cellWidth, iPos * cellWidth, target.jPos * cellWidth, target.iPos * cellWidth);
        } else {
            return null;
        }
    }


    private void moveAround() {
        Random random = new Random();
        boolean findPos = false;


        int num = random.nextInt(8);
        int i = iPos + moveDirX[num];
        int j = jPos + moveDirY[num];


        if (i >= 0 && j >= 0 && i < mapSize * roomSize && j < mapSize * roomSize) {
            if (!map[i][j].isWall()) {
                iPos = i;
                jPos = j;

                // update room
                iRoom = iPos / roomSize;
                jRoom = jPos / roomSize;

                enemiesSlot[assignPosition].setiPos(iPos);
                enemiesSlot[assignPosition].setjPos(jPos);
            }
        }
    }

    @Override
    public void dieEffect() {
        moveDriver.cancel();
        shotDriver.cancel();
        enemiesSlot[assignPosition].setiPos(-1);
        enemiesSlot[assignPosition].setjPos(-1);
        enemiesSlot[assignPosition].setColor(null);
        enemiesSlot[assignPosition].setEnemyReference(null);
        enemiesSlot[assignPosition].setBullets(null);
        synchronized (avaSlot) {
            avaSlot.add(assignPosition);
        }


    }

    public void betray(int teamNumber) {
        betraied = true;
        color = Color.GREEN;
        setTeamNumber(teamNumber);
        weapon.setColor(color);
        weapon.setBelongTeam(teamNumber);

        synchronized (moveQueue) {
            moveQueue.clear();
        }
        enemiesSlot[assignPosition].setColor(color);

    }

    private void betrayedFire() {
        if (betrayedTarget == null || betrayedTarget.getCurrentHealth() <= 0 || betrayedTarget.getTeamNumber() == getTeamNumber()) {
            betrayedTarget = null;
            findEnemy();
        }
    }

    private void findEnemy() {
        // change to enemy
        for (enemyPositionRecorder newEnemy: enemiesSlot) {
            Creature creature = newEnemy.getEnemyReference();
            if(creature != null){
                int iDis = creature.iPos - iPos;
                int jDis = creature.jPos - jPos;
                if(Math.sqrt(iDis * iDis + jDis * jDis) < 10){
                    betrayedTarget = creature;
                    return;
                }
            }
        }
        // can not find enemy, kill itself
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                takeDamage(10000);
            }
        }, 10000);
    }

    private void initDriver() {
        // keep the enemy moving
        moveDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentHealth <= 0) {
                    die();
                }

                if (!betraied) {
                    // I'm a normal enemy :)
                    // check if i find the player, if not, find a path to the player
                    move();
                }
            }
        }, 0, 1000 / moveSpeed);

        shotDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                ArrayList<Bullet> tempBullet;
                if (betraied) {
                    // stop moving and shot towers another enemy
                    // check if have target, if not find one
                    betrayedFire();
                    tempBullet = tryToShot(betrayedTarget);
                } else {
                    // I'm a normal enemy :)
                    // check if i can shot anyone
                    tempBullet = tryToShot(playerInfo);
                }


                // make the enemy move

                if (tempBullet != null) {
                    addBullets(tempBullet);
                }
            }
        }, 0, 1000 / attackRate);
    }


    private void generateMoveQueue() {
        int currentI = iPos;
        int currentJ = jPos;
        int endPointI = playerInfo.iPos;
        int endPointJ = playerInfo.jPos;

        //System.out.println("starting generate new move queue from " + iPos + ","+jPos + "  to "+endPointI+","+endPointJ);

        synchronized (map) {
            map[currentI][currentJ].setPathWeight(0);
            while (!map[endPointI][endPointJ].isOpen()) {
//            System.out.println("current i is " + currentI + " , current j is " + currentJ);
                int newPathWeight = map[currentI][currentJ].getPathWeight() + 1;
                // add current one into close
                map[currentI][currentJ].setClosed(true);
                int finalCurrentI = currentI;
                int finalCurrentJ = currentJ;
                openSupport.removeIf(temp -> temp.getRow() == finalCurrentI && temp.getColumn() == finalCurrentJ);

                // add adjacent point into open
                if (!map[currentI - 1][currentJ].isWall()) {
                    if (!map[currentI - 1][currentJ].isClosed()) {
                        // open or not touched
                        if (map[currentI - 1][currentJ].isOpen()) {
                            // update pathWeight
                            if (map[currentI - 1][currentJ].getPathWeight() > newPathWeight) {
                                map[currentI - 1][currentJ].setPathWeight(newPathWeight);
                                map[currentI - 1][currentJ].setParentI(currentI);
                                map[currentI - 1][currentJ].setParentJ(currentJ);
                            }
                        } else {
                            map[currentI - 1][currentJ].setPathWeight(newPathWeight);
                            map[currentI - 1][currentJ].setParentI(currentI);
                            map[currentI - 1][currentJ].setParentJ(currentJ);
                            map[currentI - 1][currentJ].setOpen(true);
                            map[currentI - 1][currentJ].setEstimateWeight(calculateEstimate(currentI - 1, currentJ, endPointI, endPointJ));
                            openSupport.add(new Coordinate(currentI - 1, currentJ));
                        }
                    }
                }

                if (!map[currentI + 1][currentJ].isWall()) {
                    if (!map[currentI + 1][currentJ].isClosed()) {
                        // open or not touched
                        if (map[currentI + 1][currentJ].isOpen()) {
                            // update pathWeight
                            if (map[currentI + 1][currentJ].getPathWeight() > newPathWeight) {
                                map[currentI + 1][currentJ].setPathWeight(newPathWeight);
                                map[currentI + 1][currentJ].setParentI(currentI);
                                map[currentI + 1][currentJ].setParentJ(currentJ);
                            }
                        } else {
                            map[currentI + 1][currentJ].setPathWeight(newPathWeight);
                            map[currentI + 1][currentJ].setParentI(currentI);
                            map[currentI + 1][currentJ].setParentJ(currentJ);
                            map[currentI + 1][currentJ].setOpen(true);
                            map[currentI + 1][currentJ].setEstimateWeight(calculateEstimate(currentI + 1, currentJ, endPointI, endPointJ));
                            openSupport.add(new Coordinate(currentI + 1, currentJ));
                        }
                    }
                }

                if (!map[currentI][currentJ - 1].isWall()) {
                    if (!map[currentI][currentJ - 1].isClosed()) {
                        // open or not touched
                        if (map[currentI][currentJ - 1].isOpen()) {
                            // update pathWeight
                            if (map[currentI][currentJ - 1].getPathWeight() > newPathWeight) {
                                map[currentI][currentJ - 1].setPathWeight(newPathWeight);
                                map[currentI][currentJ - 1].setParentI(currentI);
                                map[currentI][currentJ - 1].setParentJ(currentJ);
                            }
                        } else {
                            map[currentI][currentJ - 1].setPathWeight(newPathWeight);
                            map[currentI][currentJ - 1].setParentI(currentI);
                            map[currentI][currentJ - 1].setParentJ(currentJ);
                            map[currentI][currentJ - 1].setOpen(true);
                            map[currentI][currentJ - 1].setEstimateWeight(calculateEstimate(currentI, currentJ - 1, endPointI, endPointJ));
                            openSupport.add(new Coordinate(currentI, currentJ - 1));
                        }
                    }
                }

                if (!map[currentI][currentJ + 1].isWall()) {
                    if (!map[currentI][currentJ + 1].isClosed()) {
                        // open or not touched
                        if (map[currentI][currentJ + 1].isOpen()) {
                            // update pathWeight
                            if (map[currentI][currentJ + 1].getPathWeight() > newPathWeight) {
                                map[currentI][currentJ + 1].setPathWeight(newPathWeight);
                                map[currentI][currentJ + 1].setParentI(currentI);
                                map[currentI][currentJ + 1].setParentJ(currentJ);
                            }
                        } else {
                            map[currentI][currentJ + 1].setPathWeight(newPathWeight);
                            map[currentI][currentJ + 1].setParentI(currentI);
                            map[currentI][currentJ + 1].setParentJ(currentJ);
                            map[currentI][currentJ + 1].setOpen(true);
                            map[currentI][currentJ + 1].setEstimateWeight(calculateEstimate(currentI, currentJ + 1, endPointI, endPointJ));
                            openSupport.add(new Coordinate(currentI, currentJ + 1));
                        }
                    }
                }

                if (!openSupport.isEmpty()) {
                    // find smallest weight and set as next iteration
                    int nextI = openSupport.get(0).getRow();
                    int nextJ = openSupport.get(0).getColumn();
                    double smallestWeight = map[nextI][nextJ].getEstimateWeight() + map[nextI][nextJ].getPathWeight();
                    for (Coordinate temp : openSupport) {
                        double weight = map[temp.getRow()][temp.getColumn()].getEstimateWeight() + map[temp.getRow()][temp.getColumn()].getPathWeight();
                        if (weight < smallestWeight) {
                            smallestWeight = weight;
                            nextI = temp.getRow();
                            nextJ = temp.getColumn();
                        }
                    }
                    currentI = nextI;
                    currentJ = nextJ;
                } else {
                    break;
                }

            }

            if (map[endPointI][endPointJ].isOpen()) {
                int nextI = endPointI;
                int nextJ = endPointJ;
                while (!(nextI == -1 && nextJ == -1)) {
                    moveQueue.add(new Coordinate(nextI, nextJ));
                    int parentI = map[nextI][nextJ].getParentI();
                    int parentJ = map[nextI][nextJ].getParentJ();
                    //System.out.println("current point " + nextI + "," + nextJ + " parent point " + parentI + "," + parentJ);
                    nextI = parentI;
                    nextJ = parentJ;
                }
                //System.out.println("new road size is " + moveQueue.size());
                //System.out.println("last move command is " + moveQueue.get(0).getRow() + "," + moveQueue.get(0).getColumn());

            }
        }
    }

    double calculateEstimate(int i, int j, int endPointI, int endPointJ) {
        int iDis = endPointI - i;
        iDis *= iDis;
        int jDis = endPointJ - j;
        jDis *= jDis;
        return Math.sqrt(iDis + jDis);
    }
}
