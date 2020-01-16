package MazeGame;

import MazeGame.helper.Coordinate;
import MazeGame.weapons.BrokenGun;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static MazeGame.Info.*;

public class Enemy extends Creature {

    private double iPos;
    private double jPos;
    private int iRoom;
    private int jRoom;
    private double rowDir;
    private double columnDir;
    private Player playerInfo;
    private boolean lostTarget = false;
    private Weapon weapon;

    private int searchCounter = 0;

    private Room[][] roomInfo; // a reference of room info
    private Cell[][] cellInfo; // a reference of cell info
    private HashMap<String, Integer> movePriorityList;

    private int mapSize;

    private double moveSpeed = 1.0; //default

    private ArrayList<Coordinate> moveQueue = new ArrayList<>();

    public Enemy(int iPos, int jPos, Player player, Room[][] rooms, Cell[][] cells, HashMap<String, Integer> movePriorityList) {
        // in future you should move these property to child class
        super(100, 100, 0);

        this.iPos = iPos;
        this.jPos = jPos;
        this.iRoom = iPos / roomSize;
        this.jRoom = jPos / roomSize;
        roomInfo = rooms;
        mapSize = rooms.length;
        this.cellInfo = cells;
        playerInfo = player;
        weapon = new BrokenGun(Color.MAGENTA, getTeamNumber());
        this.movePriorityList = movePriorityList;
    }

    public ArrayList<Bullet> move() {

        int targetIRoom = playerInfo.getX() / roomSize;
        int targetJRoom = playerInfo.getY() / roomSize;

        if (lostTarget) {
            if (iRoom == targetIRoom && jRoom == targetJRoom) {
                lostTarget = false;
                moveQueue.clear();
                cellInfo[(int) iPos][(int) jPos].setOccupiedCreature(null);
                return fireWhileMoving();
            } else {
                System.out.println("lost target no move");
                getNewMoveQueue();
                return null;
            }
        } else {
            // does not lost
            if (iRoom == targetIRoom && jRoom == targetJRoom) {
                moveQueue.clear();
                cellInfo[(int) iPos][(int) jPos].setOccupiedCreature(null);
                return fireWhileMoving();
            } else {
                if (moveQueue.isEmpty()) {
                    getNewMoveQueue();
                    return null;
                } else {
                    // check if reach the center of the last moveQueue
                    cellInfo[(int) iPos][(int) jPos].setOccupiedCreature(null);
                    int iCoor = moveQueue.get(0).getRow();
                    int jCoor = moveQueue.get(0).getColumn();
                    if (iPos >= iCoor * roomSize + (roomSize - 5) / 2.0 && iPos <= iCoor * roomSize + (roomSize + 5) / 2.0 - 1 && jPos >= jCoor * roomSize + (roomSize - 5) / 2.0 && jPos <= jCoor * roomSize + (roomSize + 5) / 2.0 - 1) {
                        // reach the center
                        // remove first
                        moveQueue.remove(0);
                        // check if there is still queue remain
                        if (moveQueue.isEmpty()) {
                            getNewMoveQueue();
                            return null;
                        } else {
                            iCoor = moveQueue.get(0).getRow();
                            jCoor = moveQueue.get(0).getColumn();
                            int iDest = iCoor * roomSize + (roomSize - 1) / 2;
                            int jDest = jCoor * roomSize + (roomSize - 1) / 2;
                            double dist = Math.sqrt((iDest - iPos) * (iDest - iPos) + (jDest - jPos) * (jDest - jPos));
                            rowDir = (iDest - iPos) / dist;
                            columnDir = (jDest - jPos) / dist;
                        }
                    } else {
                        // not at the center, check if having the move dir
                        int iDest = iCoor * roomSize + (roomSize - 1) / 2;
                        int jDest = jCoor * roomSize + (roomSize - 1) / 2;
                        double iSub =(iDest - iPos);
                        double jSub = (jDest - jPos);
                        if ((rowDir == 0.0 && columnDir == 0.0) || iSub*rowDir < 0 || jSub*columnDir < 0) {
                            // need init Dir
                            double dist = Math.sqrt( iSub* iSub +  jSub* jSub);
                            rowDir = (iDest - iPos) / dist;
                            columnDir = (jDest - jPos) / dist;
                        }
                    }
                    // move
                    iPos = iPos + rowDir * moveSpeed;
                    jPos = jPos + columnDir * moveSpeed;
                    if (cellInfo[(int) iPos][(int) jPos].boarder || cellInfo[(int) iPos][(int) jPos].getOccupiedCreature() != null) {
                        searchAround();
                    }
                    cellInfo[(int) iPos][(int) jPos].setOccupiedCreature(this);
                    this.iRoom = (int) iPos / roomSize;
                    this.jRoom = (int) jPos / roomSize;
                    return null;
                }
            }
        }

    }

    private ArrayList<Bullet> fireWhileMoving() {
        if (!(iPos >= iRoom * roomSize + (roomSize - 5) / 2.0 && iPos <= iRoom * roomSize + (roomSize + 5) / 2.0 - 1 && jPos >= jRoom * roomSize + (roomSize - 5) / 2.0 && jPos <= jRoom * roomSize + (roomSize + 5) / 2.0 - 1)) {
            // move towards the center of the current room
            int iDest = iRoom * roomSize + (roomSize - 1) / 2;
            int jDest = jRoom * roomSize + (roomSize - 1) / 2;
            double dist = Math.sqrt((iDest - iPos) * (iDest - iPos) + (jDest - jPos) * (jDest - jPos));
            rowDir = (iDest - iPos) / dist;
            columnDir = (jDest - jPos) / dist;
            iPos = iPos + rowDir * moveSpeed;
            jPos = jPos + columnDir * moveSpeed;
        }
        if (cellInfo[(int) iPos][(int) jPos].boarder || cellInfo[(int) iPos][(int) jPos].getOccupiedCreature() != null) {
            searchAround();
        }
        cellInfo[(int) iPos][(int) jPos].setOccupiedCreature(this);

        return weapon.CheckFireStatus((int) jPos * cellWidth, (int) iPos * cellWidth, playerInfo.getY() * cellWidth, playerInfo.getX() * cellWidth);
    }


    public void getNewMoveQueue() {
        if (movePriorityList.get(iRoom+","+jRoom) == null) {
            // current room is not in range
            lostTarget = true;
        } else {
            checkRoom(iRoom, jRoom, movePriorityList.get(iRoom+","+jRoom) + 1);
        }
    }

    private void checkRoom(int row, int column, int movePriority) {
        moveQueue.add(new Coordinate(row, column));
        if (movePriority > traceRange) {
            lostTarget = false;
            return;
        } else {

            // check top
            if (roomInfo[row][column].getOpenTop() && row - 1 >= 0 && roomInfo[row - 1][column].getOpenBot()) {
                // can go to top room
                if (movePriorityList.get((row-1)+","+column) == movePriority) {
                    // top room is the target
                    checkRoom(row - 1, column, movePriority + 1);
                    return;
                }
            }


            // check right
            if (roomInfo[row][column].getOpenRight() && column + 1 < mapSize && roomInfo[row][column + 1].getOpenLeft()) {
                // can go to right room
                if (movePriorityList.get((row)+","+(column+1)) == movePriority) {
                    // right room is the target
                    checkRoom(row, column + 1, movePriority + 1);
                    return;
                }
            }


            // check bot
            if (roomInfo[row][column].getOpenBot() && row + 1 < mapSize && roomInfo[row + 1][column].getOpenTop()) {
                // can go to bot room
                if (movePriorityList.get((row+1)+","+column) == movePriority) {
                    // bot room is the target
                    checkRoom(row + 1, column, movePriority + 1);
                    return;
                }
            }


            // check left
            if (roomInfo[row][column].getOpenLeft() && column - 1 >= 0 && roomInfo[row][column - 1].getOpenRight()) {
                // can go to left room
                if (movePriorityList.get((row)+","+(column-1)) == movePriority) {
                    // left room is the target
                    checkRoom(row, column - 1, movePriority + 1);
                    return;
                }
            }
        }
        moveQueue.clear();
        lostTarget = true;
    }

    private void searchAround() {
        int startRange = 1;
        while (true) {
            for (int i = (int) iPos - startRange; i <= (int) iPos + startRange; i++) {
                for (int j = (int) jPos - startRange; j <= (int) jPos + startRange; j++) {
                    if (i >= 0 && j >= 0 && i < mapSize * roomSize && j < mapSize * roomSize) {
                        if (cellInfo[i][j].getOccupiedCreature() == null && (i != iPos && j!= jPos)) {
                            if(cellInfo[i][j].boarder){
                                if(searchCounter >= 5){
                                    moveQueue.clear();
                                    return;
                                }
                                searchCounter++;
                            }else{
//                              findLocation
                                iPos = i;
                                jPos = j;
                                return;
                            }
                        }
                    }
                }
            }
            startRange++;
            if (startRange > 2) {
                moveQueue.clear();
            }
        }
    }

    @Override
    public void die() {
        cellInfo[(int) iPos][(int) jPos].setOccupiedCreature(null);
    }


    public double getiPos() {
        return iPos;
    }

    public double getjPos() {
        return jPos;
    }

}
