package MazeGame.graphic;

import MazeGame.GameResourceController;
import MazeGame.creature.Player;

import javax.swing.*;
import java.awt.*;

public class AbilityCDGraphic extends JPanel {
    private Player player;

    public AbilityCDGraphic(GameResourceController gameResourceController){
        this.player = gameResourceController.getPlayer();
        this.setBounds(1100,700,51,51);
    }

    public void paint(Graphics graphics){
        super.paint(graphics);

        graphics.setColor(Color.gray);
        graphics.fillArc(-10,-10,70,70,90,(int)(360*player.getWeaponCD()));

        graphics.setColor(Color.blue);
        graphics.drawString("Ability",10,30);

        graphics.setColor(Color.orange);
        graphics.drawRect(0,0,50,50);
    }

}
