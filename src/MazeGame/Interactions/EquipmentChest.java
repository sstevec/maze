package MazeGame.Interactions;

import MazeGame.GameInitializer;
import MazeGame.GameResourceController;
import MazeGame.equipment.EquipmentFactory;
import MazeGame.weapons.WeaponFactory;

import java.awt.*;

public class EquipmentChest extends Intractable {
    private EquipmentFactory equipmentFactory = new EquipmentFactory();
    private int level;

    public EquipmentChest(int level) {
        super(null);
        this.level = level;
        if (level == 1) {
            setColor(new Color(79, 72, 72));
        } else if (level == 2) {
            setColor(new Color(58, 197, 37));
        } else if (level == 3) {
            setColor(new Color(55, 185, 251));
        } else {
            setColor(new Color(247, 91, 36));
        }
    }

    @Override
    public void interact(GameResourceController gameResourceController) {
        synchronized (location) {
            location.setIntractable(null);
            location.setFallenItem(equipmentFactory.randomEquipment(level));
        }
    }
}
