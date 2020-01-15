package MazeGame.Interactions;

import MazeGame.GameInitializer;
import MazeGame.Intractable;

import java.awt.*;

public class Portal extends Intractable {

    public Portal(){
        super(Color.YELLOW);
    }
    @Override
    public void interact(GameInitializer gameInitializer) {
        gameInitializer.regenerate();
    }
}
