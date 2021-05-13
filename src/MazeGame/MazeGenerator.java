package MazeGame;

import MazeGame.helper.Coordinate;
import MazeGame.helper.Point;


import java.util.ArrayList;
import java.util.Random;

import static MazeGame.Info.roomSize;

public class MazeGenerator {

    private int mapSize;
    private Room[][] rooms;
    private Cell[][] totalMap;
    private Random random = new Random();
    private Point[][] map;

    MazeGenerator(int mapSize) {
        this.mapSize = mapSize;
        rooms = new Room[mapSize][mapSize];
        totalMap = new Cell[mapSize * roomSize][mapSize * roomSize];
        map = new Point[mapSize*3][mapSize*3];
    }

    public void generateRooms() {
        initMapBound();
        createMaze();
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                rooms[i][j] = new Room();
                for(int k = 0; k<3; k++){
                    for(int l = 0; l<3; l++){
                        rooms[i][j].wallSet[k][l] = map[i*3+k][j*3+l].isWall();
                    }
                }
                rooms[i][j].generateRoom();
            }
        }
        rememberRooms();
        setProtal();
    }

    void initMapBound() {
        for (int i = 0; i < mapSize*3; i++) {
            for (int j = 0; j < mapSize*3; j++) {
                map[i][j] = new Point();
            }
        }
        for (int i = 0; i < mapSize*3; i++) {
            map[i][0].setBound(true);
            map[i][mapSize*3 - 1].setBound(true);
        }

        for(int i = 0; i<mapSize*3; i++){
            map[mapSize*3 - 1][i].setBound(true);
            map[0][i].setBound(true);
        }

        map[1][1].setStart(true);
        map[mapSize*3 - 2][mapSize*3 - 2].setEnd(true);
    }

    void createMaze() {
        ArrayList<Coordinate> wallList = new ArrayList<>();
        int fx[] = new int[]{1, -1, 0, 0};
        int fy[] = new int[]{0, 0, 1, -1};
        Random random = new Random();
        for (int i = 0; i < mapSize*3; i++) {
            for (int j = 0; j < mapSize*3; j++) {
                map[i][j].setWall(true);
            }
        }
        int currentI = random.nextInt(mapSize*3 - 3) + 1;
        int currentJ = random.nextInt(mapSize*3 - 3) + 1;
        map[currentI][currentJ].setWall(false);

        for (int i = 0; i < 4; i++) {
            if (currentI + fx[i] > 0 && currentI + fx[i] < mapSize*3 - 1 && currentJ + fy[i] > 0 && currentJ + fy[i] < mapSize*3 - 1) {
                wallList.add(new Coordinate(currentI + fx[i], currentJ + fy[i]));
                map[currentI + fx[i]][currentJ + fy[i]].setInWallList(true);
                map[currentI + fx[i]][currentJ + fy[i]].setComeDir(i%2 == 0? i+1: i-1);
            }
        }

        while (!wallList.isEmpty()) {
            int wallNumber = random.nextInt(wallList.size());
            Coordinate wallPoint =  wallList.get(wallNumber);
            int comeDir = map[wallPoint.getRow()][wallPoint.getColumn()].getComeDir();
            currentI = wallPoint.getRow() + fx[comeDir];
            currentJ = wallPoint.getColumn() + fy[comeDir];

            int targetI = 2 *wallPoint.getRow() - currentI;
            int targetJ = 2 *wallPoint.getColumn() - currentJ;

            // the problem is this maze lack of round road, so I decide to give it 1/9 chance to to
            //   create round road
            if (map[targetI][targetJ].isWall()) {
                map[targetI][targetJ].setWall(false);
                map[wallPoint.getRow()][wallPoint.getColumn()].setWall(false);
                for (int i = 0; i < 4; i++) {
                    if (targetI + fx[i] > 0 && targetI + fx[i] < mapSize*3 - 1 && targetJ + fy[i] > 0 && targetJ + fy[i] < mapSize*3 - 1) {
                        if (map[targetI + fx[i]][targetJ + fy[i]].isWall() && !map[targetI + fx[i]][targetJ + fy[i]].isInWallList()) {
                            wallList.add(new Coordinate(targetI + fx[i], targetJ + fy[i]));
                            map[targetI + fx[i]][targetJ + fy[i]].setInWallList(true);
                            map[targetI + fx[i]][targetJ + fy[i]].setComeDir(i%2 == 0? i+1: i-1);
                        }
                    }
                }

            }else{
                int num = random.nextInt(9);
                if(num == 1){
                    // 1/9 chance open the way
                    map[wallPoint.getRow()][wallPoint.getColumn()].setWall(false);
                }
            }
            wallList.remove(wallNumber);
            map[wallPoint.getRow()][wallPoint.getColumn()].setInWallList(false);
        }
        map[1][1].setWall(false);
        map[mapSize*3-2][mapSize*3-2].setWall(false);
    }


    private void rememberRooms() {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                // cell loop
                for (int k = 0; k < roomSize; k++) {
                    for (int h = 0; h < roomSize; h++) {
                        rooms[i][j].cells[k][h].setX(i * roomSize + k);
                        rooms[i][j].cells[k][h].setY(j * roomSize + h);
                        totalMap[i * roomSize + k][j * roomSize + h] = rooms[i][j].cells[k][h];
                    }
                }
            }
        }
    }


    private void setProtal(){
        rooms[mapSize-1][mapSize-1].setPortal();
    }

    public Cell[][] getTotalMap() {
        return totalMap;
    }

    public Room[][] getRooms() {
        return rooms;
    }
}
