package MazeGame.equipment.weaponComponent;

import MazeGame.creature.Creature;
import MazeGame.Item;
import MazeGame.helper.FireData;

import static MazeGame.helper.Info.WEAPON_COMPONENT_KIND;

public abstract class WeaponComponent extends Item {
    protected int priority;
    protected String description;
    protected String icon;

    public WeaponComponent() {
        super(WEAPON_COMPONENT_KIND);
    }

    public abstract void processFireData(FireData fireData);

    public void pickUp(Creature creature){
        creature.putIntoBeg(this);
    }

    public void equip(Creature creature){
        if(priority < 0){
            // pre process item
            creature.putIntoPreProcess(this);
        } else {
            creature.putIntoPostProcess(this);
        }
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
