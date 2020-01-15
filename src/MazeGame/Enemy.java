package MazeGame;

import MazeGame.helper.Coordinate;
import MazeGame.weapons.BrokenGun;
import MazeGame.weapons.Gun;

import java.awt.*;
import java.util.ArrayList;

import static MazeGame.Info.cellWidth;
import static MazeGame.Info.roomSize;

public class Enemy extends Creature{

    private double iPos;
    private double jPos;
    private int iRoom;
    private int jRoom;
    private double rowDir;
    private double columnDir;
    private Player playerInfo;
    private boolean lostTarget = false;
    private Weapon weapon;

    private Room[][] roomInfo; // a reference of room info
    private Cell[][] cellInfo; // a reference of cell info

    private int mapSize;

    private double moveSpeed = 1.0; //default

    private ArrayList<Coordinate> moveQueue = new ArrayList<>();

    public Enemy(int iPos, int jPos,Player player, Room[][] rooms, Cell[][] cells){
        // in future you should move these property to child class
        super(100,100, 0);

        this.iPos = iPos;
        this.jPos = jPos;
        this.iRoom = iPos/roomSize;
        this.jRoom = jPos/roomSize;
        roomInfo = rooms;
        mapSize = rooms.length;
        this.cellInfo = cells;
        playerInfo = player;
        weapon = new BrokenGun(Color.MAGENTA, getTeamNumber());

    }

    public ArrayList<Bullet> move(){
        lostTarget = false;

        cellInfo[(int)iPos][(int)jPos].setOccupiedCreature(null);

        if(moveQueue.isEmpty()){
            getNewMoveQueue(playerInfo.getX(),playerInfo.getY());
        }

        // moveQueue is not empty now, check if need move
        int targetIRoom = playerInfo.getX()/roomSize;
        int targetJRoom = playerInfo.getY()/roomSize;
        if(iRoom == targetIRoom && jRoom == targetJRoom){
            // meet the player now, no need to move
            moveQueue.clear();

            if(!(iPos >= iRoom*roomSize+(roomSize-5)/2.0 && iPos <= iRoom*roomSize+(roomSize+5)/2.0-1 && jPos >= jRoom*roomSize+(roomSize-5)/2.0 && jPos <= jRoom*roomSize+(roomSize+5)/2.0-1)){
                // move towards the center of the current room
                int iDest = iRoom*roomSize+(roomSize-1)/2;
                int jDest = jRoom*roomSize+(roomSize-1)/2;
                double dist = Math.sqrt((iDest-iPos)*(iDest-iPos) + (jDest-jPos)*(jDest-jPos));
                rowDir = (iDest-iPos)/dist;
                columnDir = (jDest-jPos)/dist;
                iPos = iPos + rowDir*moveSpeed;
                jPos = jPos + columnDir*moveSpeed;
            }
            if(cellInfo[(int)iPos][(int)jPos].boarder || cellInfo[(int)iPos][(int)jPos].getOccupiedCreature() != null){
                searchAround();
            }
            cellInfo[(int)iPos][(int)jPos].setOccupiedCreature(this);

            return weapon.CheckFireStatus((int)jPos*cellWidth,(int)iPos*cellWidth,playerInfo.getY()*cellWidth,playerInfo.getX()*cellWidth);
        }else{
            // not meet the player, need move

            if(lostTarget){
                return null;
            }

            // check if reach the center of the last moveQueue
            int iCoor = moveQueue.get(moveQueue.size()-1).getRow();
            int jCoor = moveQueue.get(moveQueue.size()-1).getColumn();
            if(iPos >= iCoor*roomSize+(roomSize-5)/2.0 && iPos <= iCoor*roomSize+(roomSize+5)/2.0-1 && jPos >= jCoor*roomSize+(roomSize-5)/2.0 && jPos <= jCoor*roomSize+(roomSize+5)/2.0-1){
                // reach the center
                // remove last
                moveQueue.remove(moveQueue.size()-1);
                // check if there is still queue remain
                if(moveQueue.isEmpty()){
                    getNewMoveQueue(playerInfo.getX(),playerInfo.getY());
                }

                // calculate new move dir
                if(lostTarget){
                    return null;
                }
                iCoor = moveQueue.get(moveQueue.size()-1).getRow();
                jCoor = moveQueue.get(moveQueue.size()-1).getColumn();

                int iDest = iCoor*roomSize+(roomSize-1)/2;
                int jDest = jCoor*roomSize+(roomSize-1)/2;
                double dist = Math.sqrt((iDest-iPos)*(iDest-iPos) + (jDest-jPos)*(jDest-jPos));
                rowDir = (iDest-iPos)/dist;
                columnDir = (jDest-jPos)/dist;
            }else{
                // not at the center, check if having the move dir
                if(rowDir == 0.0 && columnDir == 0.0){
                    // need init Dir
                    int iDest = iCoor*roomSize+(roomSize-1)/2;
                    int jDest = jCoor*roomSize+(roomSize-1)/2;
                    double dist = Math.sqrt((iDest-iPos)*(iDest-iPos) + (jDest-jPos)*(jDest-jPos));
                    rowDir = (iDest-iPos)/dist;
                    columnDir = (jDest-jPos)/dist;
                }
            }
            // move
            iPos = iPos + rowDir*moveSpeed;
            jPos = jPos + columnDir*moveSpeed;
            if(cellInfo[(int)iPos][(int)jPos].boarder || cellInfo[(int)iPos][(int)jPos].getOccupiedCreature() != null){
                searchAround();
            }
            cellInfo[(int)iPos][(int)jPos].setOccupiedCreature(this);
            this.iRoom = (int)iPos/roomSize;
            this.jRoom = (int)jPos/roomSize;
        }
        return null;
    }


    public void getNewMoveQueue(int playerX, int playerY){
        // get target room number
        int targetIRoom = playerX/roomSize;
        int targetJRoom = playerY/roomSize;
        if(!checkRoom(iRoom,jRoom,targetIRoom,targetJRoom,-1,0)){
            // no route
            lostTarget = true;
        }
    }

    private boolean checkRoom(int row, int column, int targetI, int targetJ, int comeDir, int checkTime){
        if(row == targetI && column == targetJ){
            // get the room
            moveQueue.add(new Coordinate(row,column));
            return true;
        }

        // check too many rooms
        if(checkTime >= 3){
            return false;
        }

        for(int i = 0; i<4; i++){
            if(i == comeDir){
                continue;
            }
            if(i == 0){
                // check top
                if(roomInfo[row][column].getOpenTop() && row - 1 >= 0 && roomInfo[row-1][column].getOpenBot()){
                    // can go to top room
                    if(checkRoom(row-1,column,targetI,targetJ,2,checkTime+1)){
                        // top room is the target
                        moveQueue.add(new Coordinate(row,column));
                        return true;
                    }
                }else {
                    continue;
                }
            }

            if(i == 1){
                // check right
                if(roomInfo[row][column].getOpenRight() && column+1<mapSize && roomInfo[row][column+1].getOpenLeft()){
                    // can go to right room
                    if(checkRoom(row,column+1,targetI,targetJ,3,checkTime+1)){
                        // right room is the target
                        moveQueue.add(new Coordinate(row,column));
                        return true;
                    }
                }else {
                    continue;
                }
            }

            if(i == 2){
                // check bot
                if(roomInfo[row][column].getOpenBot() && row + 1 < mapSize && roomInfo[row+1][column].getOpenTop()){
                    // can go to bot room
                    if(checkRoom(row+1,column,targetI,targetJ,0,checkTime+1)){
                        // bot room is the target
                        moveQueue.add(new Coordinate(row,column));
                        return true;
                    }
                }else {
                    continue;
                }
            }

            if(i == 3){
                // check left
                if(roomInfo[row][column].getOpenLeft() && column - 1>=0 && roomInfo[row][column-1].getOpenRight()){
                    // can go to left room
                    if(checkRoom(row,column-1,targetI,targetJ,1,checkTime+1)){
                        // left room is the target
                        moveQueue.add(new Coordinate(row,column));
                        return true;
                    }
                }else {
                    continue;
                }
            }
        }
        return false;
    }

    private void searchAround(){
        boolean findLocation = false;
        int startRange = 1;
        while(!findLocation){
            for(int i = (int)iPos-startRange; i<=(int)iPos+startRange;i++){
                for(int j = (int)jPos-startRange; j<=(int)jPos+startRange;j++){
                    if(i >= 0 && j >=0 && i<mapSize*roomSize && j <mapSize*roomSize) {
                        if (!cellInfo[i][j].boarder && cellInfo[i][j].getOccupiedCreature() == null) {
                            findLocation = true;
                            iPos = i;
                            jPos = j;
                            break;
                        }
                    }
                }
                if(findLocation){
                    break;
                }
            }
            startRange++;
            if(startRange > 3){
                moveQueue.clear();
            }
        }
    }

    @Override
    public void die(){
        cellInfo[(int)iPos][(int)jPos].setOccupiedCreature(null);
    }


    public double getiPos() {
        return iPos;
    }

    public double getjPos() {
        return jPos;
    }

}
