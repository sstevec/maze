package MazeGame.equipment;

import MazeGame.Player;

public class Charger extends Equipment{
    public Charger(int level){
        super( "LV " + level +" Charger");
        this.level = level;

        setAttribute(0,0,0,4 * level,0);
    }

    @Override
    public void equipEffect(Player p) {
    }

    class ChargerFactory implements EquipmentInstance<Charger>{

        @Override
        public Charger create(int level) {
            return new Charger(level);
        }
    }
}
