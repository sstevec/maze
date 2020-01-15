package MazeGame;

import java.awt.*;

public abstract class Intractable {

    private Color color;

    public Intractable(Color color){
        this.color = color;
    }

    public abstract void interact(GameInitializer gameInitializer);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
