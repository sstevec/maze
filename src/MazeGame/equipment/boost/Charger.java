package MazeGame.equipment.boost;

import MazeGame.creature.Creature;
import MazeGame.equipment.ItemFactoryInterface;

public class Charger extends Equipment {
    public Charger(int level) {
        super();
        this.level = level;
        this.name = "LV " + level + " Charger";
        setAttribute(0, 0, 0, 4 * level, 0);
    }

    @Override
    public void equipEffect(Creature p) {
    }

    public class ChargerFactory implements ItemFactoryInterface<Charger> {

        @Override
        public Charger create(int level) {
            return new Charger(level);
        }
    }
}
