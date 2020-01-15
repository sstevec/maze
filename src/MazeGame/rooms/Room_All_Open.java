package MazeGame.rooms;

import MazeGame.Cell;
import MazeGame.Room;

public class Room_All_Open extends Room {
    public Room_All_Open(){
        super();
        this.setOpenTop(true);
        this.setOpenBot(true);
        this.setOpenRight(true);
        this.setOpenLeft(true);

        int roomSize = getCells().length;
        for(int i = 0; i<roomSize; i++){
            for(int j = 0; j<roomSize; j++){
                getCells()[i][j] = new Cell();
            }
        }
    }
}
