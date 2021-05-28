package MazeGame.equipment.boost;

import MazeGame.creature.Creature;
import MazeGame.Item;

import static MazeGame.helper.Info.BOOST_KIND;

public abstract class Equipment extends Item {
    private int extraHealth = 0;
    private int heal = 0;
    private int extraDamage = 0;
    private int extraCDReduce = 0;
    private int extraAttackSpeed = 0;
    protected int level = 0;

    public Equipment(){
        super(BOOST_KIND);
    }

    protected void setAttribute(int extraHealth, int heal, int extraDamage, int extraCDReduce, int extraAttackSpeed){
        this.extraHealth = extraHealth;
        this.heal = heal;
        this.extraDamage = extraDamage;
        this.extraCDReduce = extraCDReduce;
        this.extraAttackSpeed = extraAttackSpeed;
    }

    public int getExtraHealth() {
        return extraHealth;
    }

    public int getHeal() {
        return heal;
    }

    public int getExtraDamage() {
        return extraDamage;
    }

    public int getExtraCDReduce() {
        return extraCDReduce;
    }

    public int getExtraAttackSpeed() {
        return extraAttackSpeed;
    }

    public abstract void equipEffect(Creature p);

    public void pickUp(Creature player){
        player.updateAttribute(extraHealth, heal, extraDamage, extraCDReduce, extraAttackSpeed);
        equipEffect(player);
    }
}
