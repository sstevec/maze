package MazeGame.rooms;

import MazeGame.Cell;
import MazeGame.Room;

public class Room_Wall_6 extends Room {
    public Room_Wall_6(){
        super();
        this.setOpenTop(true);
        this.setOpenBot(true);
        
        int roomSize = getCells().length;
        for(int i = 0; i<roomSize; i++){
            for(int j = 0; j<roomSize; j++){
                getCells()[i][j] = new Cell();
            }
        }

        // left
        for(int i = 0; i<roomSize; i++){
            getCells()[i][0].setBoarder(true);
        }

        // right
        for(int i = 0; i<roomSize; i++){
            getCells()[i][roomSize-1].setBoarder(true);
        }

    }
}
