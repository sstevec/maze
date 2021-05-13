package MazeGame.equipment;

import MazeGame.weapons.*;

import java.util.ArrayList;
import java.util.Random;


interface EquipmentInstance<T>{
    public T create(int level);
}

public class EquipmentFactory {

    private final ArrayList<EquipmentInstance<? extends Equipment>> allEquipmentList = new ArrayList<>();
    private final Random random = new Random();

    /***
     *  Register all the weapon
     */
    public EquipmentFactory(){
        // Register all weapon list
        allEquipmentList.add(new ArmorPack(0).new ArmorPackFactory());
        allEquipmentList.add(new Charger(0).new ChargerFactory());
        allEquipmentList.add(new FireSpeedPack(0).new FireSpeedFactory());
        allEquipmentList.add(new HealPack(0).new HealPackFactory ());
        allEquipmentList.add(new WeaponUpgrade(0).new WeaponUpgradeFactory());

    }

    public Equipment randomEquipment(int level){
        return allEquipmentList.get(random.nextInt(allEquipmentList.size())).create(level);
    }
}
