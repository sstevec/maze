package MazeGame.creature;

import MazeGame.GameResourceController;
import MazeGame.creature.Creature;
import MazeGame.creature.Player;
import MazeGame.effect.Effect;
import MazeGame.helper.Coordinate;
import MazeGame.helper.Point;
import MazeGame.helper.CreaturePositionRecorder;
import MazeGame.weapons.BrokenGun;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.helper.Info.*;

public class Enemy extends Creature implements Runnable {

    private int iRoom;
    private int jRoom;
    private final Player playerInfo;
    private Creature betrayedTarget;
    private boolean betrayed = false;
    private final ArrayList<Integer> avaSlot;
    private int assignPosition;
    private Point[][] map;
    private ArrayList<Coordinate> openSupport;
    private final int[] moveDirX = {1, 1, 1, 0, 0, -1, -1, -1};
    private final int[] moveDirY = {1, 0, -1, 1, -1, 1, 0, -1};
    private int mapUpdateCounter = 0;


    private final CopyOnWriteArrayList<Effect> effects;

    private Timer moveDriver = new Timer();
    private Timer shotDriver = new Timer();

    private int mapSize;

    private int moveSpeed = 3; //default
    private int attackRate = 2; //default

    private final ArrayList<Coordinate> moveQueue = new ArrayList<>();

    public Enemy(int roomI, int roomJ, GameResourceController gameResourceController) {
        // in future you should move these property to child class
        super(100, 100, 0, gameResourceController);

        this.iRoom = roomI;
        this.jRoom = roomJ;
        mapSize = gameResourceController.getMapSize();
        playerInfo = gameResourceController.getPlayer();
        effects = gameResourceController.getEffects();
        this.color = Color.MAGENTA;
        weapon = new BrokenGun(color, getTeamNumber(), effects, this);
        this.avaSlot = gameResourceController.getCreatureSlot();
    }

    @Override
    public void run() {
        init();
        initDriver();
    }

    protected void customInit() {

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

        // generate enemy position
        do {
            iDPos = iRoom * roomSize + random.nextInt(roomSize - 11) + 5;
            jDPos = jRoom * roomSize + random.nextInt(roomSize - 11) + 5;
        } while (cellInfo[(int) iDPos][(int) jDPos].isBoarder());


        // create a map used to find way
        map = new Point[cellInfo.length][cellInfo.length];
        for (int i = 0; i < cellInfo.length; i++) {
            for (int j = 0; j < cellInfo.length; j++) {
                map[i][j] = new Point();
                map[i][j].setWall(cellInfo[i][j].isBoarder());
            }
        }

        creatures[assignPosition].setiDPos(iDPos);
        creatures[assignPosition].setjDPos(jDPos);
        creatures[assignPosition].setColor(color);
        creatures[assignPosition].setBullets(bullets);
        creatures[assignPosition].setCreatureReference(this);

        generateMoveQueue();
    }


    private void initDriver() {
        // keep the enemy moving
        moveDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!betrayed) {
                    // I'm a normal enemy :)
                    // check if i find the player, if not, find a path to the player
                    move();
                }
            }
        }, 0, 1000 / moveSpeed);

        shotDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                if (betrayed) {
                    // stop moving and shot towers another enemy
                    // check if have target, if not find one
                    findEnemy();
                    tryToShot(betrayedTarget);
                } else {
                    // I'm a normal enemy :)
                    // check if i can shot anyone
                    tryToShot(playerInfo);
                }
            }
        }, 0, 1000 / attackRate);
    }


    private void move() {
        mapUpdateCounter++;
        if (mapUpdateCounter == moveSpeed * 30) {
            updateMap();
            mapUpdateCounter = 0;
        }

        double iDest = playerInfo.getiDPos() - iDPos;
        double jDest = playerInfo.getjDPos() - jDPos;

        if (Math.sqrt(iDest * iDest + jDest * jDest) <= 5) {
            // really close
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
                    double oldI = iDPos;
                    double oldJ = jDPos;
                    iDPos = moveTarget.getRow();
                    jDPos = moveTarget.getColumn();
                    if (oldI == iDPos && oldJ == jDPos) {
                        return;
                    }

                    // update room
                    iRoom = (int)iDPos / roomSize;
                    jRoom = (int)jDPos / roomSize;

                    // update graphic position
                    creatures[assignPosition].setiDPos(iDPos);
                    creatures[assignPosition].setjDPos(jDPos);
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

    private void tryToShot(Creature target) {
        if (target == null) {
            return;
        }
        double iDest = target.getiDPos() - iDPos;
        double jDest = target.getjDPos() - jDPos;

        if (Math.sqrt(iDest * iDest + jDest * jDest) <= 8) {
            // in the same room
            weapon.CheckFireStatus(jDPos * cellWidth, iDPos * cellWidth, target.jDPos * cellWidth, target.iDPos * cellWidth);
        }
    }


    private void moveAround() {
        Random random = new Random();

        int num = random.nextInt(8);
        int i = (int)iDPos + moveDirX[num];
        int j = (int)jDPos + moveDirY[num];


        if (i >= 0 && j >= 0 && i < mapSize * roomSize && j < mapSize * roomSize) {
            if (!map[i][j].isWall()) {
                iDPos = i;
                jDPos = j;
                // update room
                iRoom = (int)iDPos / roomSize;
                jRoom = (int)jDPos / roomSize;

                creatures[assignPosition].setiDPos(iDPos);
                creatures[assignPosition].setjDPos(jDPos);
            }
        }
    }

    @Override
    public void dieClear() {
        moveDriver.cancel();
        shotDriver.cancel();
        creatures[assignPosition].setiPos(-1);
        creatures[assignPosition].setjPos(-1);
        creatures[assignPosition].setiDPos(-1);
        creatures[assignPosition].setjDPos(-1);
        creatures[assignPosition].setColor(null);
        creatures[assignPosition].setCreatureReference(null);
        creatures[assignPosition].setBullets(null);
        synchronized (avaSlot) {
            avaSlot.add(assignPosition);
        }
    }

    @Override
    public void dieEffect() {
    }

    public void betray(int teamNumber) {
        betrayed = true;
        color = Color.GREEN;
        setTeamNumber(teamNumber);
        weapon.setColor(color);
        weapon.setBelongTeam(teamNumber);

        synchronized (moveQueue) {
            moveQueue.clear();
        }
        creatures[assignPosition].setColor(color);

        // betrayed target will kill itself after 10s
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                takeDamage(10000);
            }
        }, 30000);
    }

    private void findEnemy() {
        betrayedTarget = null;
        double shortestDist = 10000;
        // change to enemy
        for (CreaturePositionRecorder newEnemy : creatures) {
            Creature creature = newEnemy.getCreatureReference();
            if (creature != null && creature != this && creature.getTeamNumber() != getTeamNumber()) {
                double iDis = creature.iDPos - iDPos;
                double jDis = creature.jDPos - jDPos;
                double dist = Math.sqrt(iDis * iDis + jDis * jDis);
                if (dist < shortestDist) {
                    betrayedTarget = creature;
                    shortestDist = dist;
                }
            }
        }

    }


    private void generateMoveQueue() {
        int currentI = (int)iDPos;
        int currentJ = (int)jDPos;
        int endPointI = (int)playerInfo.iDPos;
        int endPointJ = (int)playerInfo.jDPos;

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
