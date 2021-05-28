package MazeGame.equipment.boost;

import MazeGame.creature.Creature;
import MazeGame.equipment.ItemFactoryInterface;

public class WeaponUpgrade extends Equipment {
    public WeaponUpgrade(int level) {
        super();
        this.level = level;
        this.name = "LV " + level + " upgrade";
        setAttribute(0, 0, level, 0, 0);
    }

    @Override
    public void equipEffect(Creature p) {
    }

    public class WeaponUpgradeFactory implements ItemFactoryInterface<WeaponUpgrade> {

        @Override
        public WeaponUpgrade create(int level) {
            return new WeaponUpgrade(level);
        }
    }
}
