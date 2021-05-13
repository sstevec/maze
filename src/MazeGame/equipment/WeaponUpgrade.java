package MazeGame.equipment;

import MazeGame.Player;

public class WeaponUpgrade extends Equipment{
    public WeaponUpgrade(int level){
        super( "LV " + level +" upgrade");
        this.level = level;

        setAttribute(0,0,level,0,0);
    }

    @Override
    public void equipEffect(Player p) {
    }

    class WeaponUpgradeFactory implements EquipmentInstance<WeaponUpgrade>{

        @Override
        public WeaponUpgrade create(int level) {
            return new WeaponUpgrade(level);
        }
    }
}
