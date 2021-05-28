package MazeGame.equipment.boost;

import MazeGame.creature.Creature;
import MazeGame.equipment.ItemFactoryInterface;

public class HealPack extends Equipment {
    public HealPack(int level){
        super();
        this.level = level;
        this.name = "LV " + level +" Healer";
        setAttribute(0,level * 15,0,0,0);
    }

    @Override
    public void equipEffect(Creature p) {
    }


    public class HealPackFactory implements ItemFactoryInterface<HealPack> {

        @Override
        public HealPack create(int level) {
            return new HealPack(level);
        }
    }
}
