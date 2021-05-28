package MazeGame.Interactions;

import MazeGame.GameResourceController;

import java.awt.*;

public class Portal extends Intractable {

    public Portal(){
        super(Color.YELLOW);
    }
    @Override
    public void interact(GameResourceController gameResourceController) {
        gameResourceController.getGameController().regenerate();
    }
}
