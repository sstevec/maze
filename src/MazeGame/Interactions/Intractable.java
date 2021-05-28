package MazeGame.Interactions;

import MazeGame.map.Cell;
import MazeGame.GameResourceController;

import java.awt.*;

public abstract class Intractable {

    protected Cell location;
    private Color color;

    public Intractable(Color color){
        this.color = color;
    }

    public abstract void interact(GameResourceController gameResourceController);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLocation(Cell cell){
        location = cell;
    }
}
