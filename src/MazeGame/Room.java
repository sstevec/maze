package MazeGame;

import MazeGame.Interactions.EquipmentChest;
import MazeGame.Interactions.Portal;
import MazeGame.Interactions.WeaponChest;

import java.util.Random;

import static MazeGame.Info.roomSize;

public class Room {

    private boolean visited = false;
    Cell[][] cells = new Cell[roomSize][roomSize];
    boolean[][] wallSet;

    public Room() {
        Random random = new Random();
        wallSet = new boolean[3][3];
        for(int i = 0; i<roomSize; i++){
            for(int j = 0; j<roomSize; j++){
                cells[i][j] = new Cell();
            }
        }
    }

    public boolean visit() {
        if (!visited) {
            visited = true;
            return true;
        } else {
            return false;
        }
    }

    public void setPortal() {
        int startCor = (roomSize - 3) / 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i + startCor][j + startCor].setIntractable(new Portal());
            }
        }
    }

    public void generateRoom() {
        int mapGridSize = roomSize / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < mapGridSize; k++) {
                    for (int l = 0; l < mapGridSize; l++) {
                        cells[i * mapGridSize + k][j * mapGridSize + l].setBoarder(wallSet[i][j]);
                    }
                }
            }
        }

        // have 1/15 chance generate a weapon chest
        Random r = new Random();
        if(r.nextInt(15) == 0){
            // generate a chest
            for(int i = 0; i<roomSize; i++){
                for (int j = 0; j<roomSize; j++){
                    if(!cells[i][j].isBoarder() && cells[i][j].getIntractable() == null){
                        cells[i][j].setIntractable(new WeaponChest());
                        return;
                    }
                }
            }
        }

        for(int i = 0; i<roomSize; i++){
            for (int j = 0; j<roomSize; j++){
                if(!cells[i][j].isBoarder() && cells[i][j].getIntractable() == null){
                    if(r.nextInt(300) == 0) {
                        int level;
                        int num = r.nextInt(1000)+1;

                        // 70% chance level 1 chest
                        if( num < 700){
                            level = 1;
                        } else if(num < 950){
                            // 25% chance level 2
                            level = 2;
                        } else if(num < 999){
                            // 4.8% chance level 3
                            level = 3;
                        } else{
                            // 0.2% chance level 10
                            level = 10;
                        }
                        cells[i][j].setIntractable(new EquipmentChest(level));
                    }
                }
            }
        }
    }

}
