package MazeGame.rooms;

import MazeGame.Cell;
import MazeGame.Room;

public class Room_Wall_5 extends Room {
    public Room_Wall_5(){
        super();
        this.setOpenLeft(true);
        this.setOpenRight(true);
        
        int roomSize = getCells().length;
        for(int i = 0; i<roomSize; i++){
            for(int j = 0; j<roomSize; j++){
                getCells()[i][j] = new Cell();
            }
        }

        // top
        for(int i = 0; i<roomSize; i++){
            getCells()[0][i].setBoarder(true);
        }


        // bot
        for(int i = 0; i<roomSize; i++){
            getCells()[roomSize-1][i].setBoarder(true);
        }

    }
}
