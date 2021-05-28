package MazeGame.equipment.boost;

import MazeGame.creature.Creature;
import MazeGame.equipment.ItemFactoryInterface;

public class FireSpeedPack extends Equipment {
    public FireSpeedPack(int level) {
        super();
        this.level = level;
        this.name = "LV " + level + " FireSpeed";
        setAttribute(0, 0, 0, 0, level * 5);
    }

    @Override
    public void equipEffect(Creature p) {
    }

    public class FireSpeedFactory implements ItemFactoryInterface<FireSpeedPack> {

        @Override
        public FireSpeedPack create(int level) {
            return new FireSpeedPack(level);
        }
    }
}
