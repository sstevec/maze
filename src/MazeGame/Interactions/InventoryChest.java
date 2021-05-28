package MazeGame.Interactions;

import MazeGame.GameResourceController;
import MazeGame.equipment.InventoryFactory;

import java.awt.*;

public class InventoryChest extends Intractable {
    private InventoryFactory inventoryFactory = new InventoryFactory();
    private int level;
    private int type;

    public InventoryChest(int level) {
        super(null);
        this.type = 0;
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

    public InventoryChest() {
        super(new Color(141, 6, 244));
        this.type = 1;
    }

    @Override
    public void interact(GameResourceController gameResourceController) {
        synchronized (location) {
            location.setIntractable(null);
            if(type == 0) {
                // boost chest
                location.setFallenItem(inventoryFactory.randomEquipment(level));
            } else if (type == 1){
                location.setFallenItem(inventoryFactory.randomWeaponComponent());
            }
        }
    }
}
