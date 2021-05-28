package MazeGame.equipment.boost;

import MazeGame.creature.Creature;
import MazeGame.equipment.ItemFactoryInterface;

public class ArmorPack extends Equipment {
    public ArmorPack(int level) {
        super();
        this.name = "LV " + level + " Armor";
        this.level = level;

        setAttribute(level * 10, 0, 0, 0, 0);
    }

    @Override
    public void equipEffect(Creature p) {
    }

    public class ArmorPackFactory implements ItemFactoryInterface<ArmorPack> {

        @Override
        public ArmorPack create(int level) {
            return new ArmorPack(level);
        }
    }
}
