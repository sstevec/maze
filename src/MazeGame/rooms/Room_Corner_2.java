package MazeGame.rooms;

import MazeGame.Cell;
import MazeGame.Room;

public class Room_Corner_2 extends Room {
    public Room_Corner_2(){
        super();
        this.setOpenBot(true);
        this.setOpenLeft(true);

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

        // left part 1
        for(int i = 0; i<(roomSize-5)/2; i++){
            getCells()[i][0].setBoarder(true);
        }

        // left part 2
        for(int i = (roomSize+5)/2; i<roomSize; i++){
            getCells()[i][0].setBoarder(true);
        }

        // bot part 1
        for(int i = 0; i<(roomSize-5)/2; i++){
            getCells()[roomSize-1][i].setBoarder(true);
        }

        // bot part 2
        for(int i = (roomSize+5)/2; i<roomSize; i++){
            getCells()[roomSize-1][i].setBoarder(true);
        }

        // right
        for(int i = 0; i<roomSize; i++){
            getCells()[i][roomSize-1].setBoarder(true);
        }
    }
}
