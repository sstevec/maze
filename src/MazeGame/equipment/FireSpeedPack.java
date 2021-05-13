package MazeGame.equipment;

import MazeGame.Player;

public class FireSpeedPack extends Equipment{
    public FireSpeedPack(int level){
        super( "LV " + level +" FireSpeed");
        this.level = level;

        setAttribute(0,0,0,0,level*5);
    }

    @Override
    public void equipEffect(Player p) {
    }

    class FireSpeedFactory implements EquipmentInstance<FireSpeedPack>{

        @Override
        public FireSpeedPack create(int level) {
            return new FireSpeedPack(level);
        }
    }
}
