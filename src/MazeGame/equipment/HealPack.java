package MazeGame.equipment;

import MazeGame.Player;

public class HealPack extends Equipment{
    public HealPack(int level){
        super("LV " + level +" Healer");
        this.level = level;

        setAttribute(0,level * 15,0,0,0);
    }

    @Override
    public void equipEffect(Player p) {
    }


    class HealPackFactory implements EquipmentInstance<HealPack>{

        @Override
        public HealPack create(int level) {
            return new HealPack(level);
        }
    }
}
