package MazeGame.Interactions;

import MazeGame.GameResourceController;
import MazeGame.weapons.*;

import java.awt.*;

public class WeaponChest extends Intractable{
    private WeaponFactory weaponFactory = new WeaponFactory();

    public WeaponChest(){
        super(new Color(255, 64, 64));
    }
    @Override
    public void interact(GameResourceController gameResourceController) {
        synchronized (location){
            location.setIntractable(null);
            location.setFallenItem(weaponFactory.randomWeapon());
        }
    }


}
