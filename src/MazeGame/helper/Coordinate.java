package MazeGame.helper;

import java.awt.*;

public class Coordinate {
    int row;
    int column;

    public Coordinate(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
