package MazeGame.rooms;

import MazeGame.Cell;
import MazeGame.Room;

public class Room_Wall_1 extends Room {
    public Room_Wall_1(){
        super();
        this.setOpenBot(true);
        this.setOpenRight(true);
        this.setOpenLeft(true);

        int roomSize = getCells().length;
        for(int i = 0; i<roomSize; i++){
            for(int j = 0; j<roomSize; j++){
                getCells()[i][j] = new Cell();
            }
        }

        // top part 1
        for(int i = 0; i<roomSize; i++){
            getCells()[0][i].setBoarder(true);
        }

    }
}
