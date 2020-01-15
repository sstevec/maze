package MazeGame.rooms;

import MazeGame.Cell;
import MazeGame.Room;

public class Room_Wall_3 extends Room {
    public Room_Wall_3(){
        super();
        this.setOpenTop(true);
        this.setOpenLeft(true);
        this.setOpenRight(true);
        
        int roomSize = getCells().length;
        for(int i = 0; i<roomSize; i++){
            for(int j = 0; j<roomSize; j++){
                getCells()[i][j] = new Cell();
            }
        }

        // bot part 1
        for(int i = 0; i<roomSize; i++){
            getCells()[roomSize-1][i].setBoarder(true);
        }

    }
}
