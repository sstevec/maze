package MazeGame.rooms;

import MazeGame.Cell;
import MazeGame.Room;

public class Room_Wall_4 extends Room {
    public Room_Wall_4(){
        super();
        this.setOpenTop(true);
        this.setOpenBot(true);
        this.setOpenRight(true);
        
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
    }
}
