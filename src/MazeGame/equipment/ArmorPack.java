package MazeGame.equipment;

import MazeGame.Player;

public class ArmorPack extends Equipment{
    public ArmorPack(int level){
        super( "LV " + level +" Armor");
        this.level = level;

        setAttribute(level*10,0,0,0,0);
    }

    @Override
    public void equipEffect(Player p) {
    }

    class ArmorPackFactory implements EquipmentInstance<ArmorPack>{

        @Override
        public ArmorPack create(int level) {
            return new ArmorPack(level);
        }
    }
}
