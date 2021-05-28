package MazeGame.equipment;

import MazeGame.equipment.boost.*;
import MazeGame.equipment.weaponComponent.BoostSpeedComponent;
import MazeGame.equipment.weaponComponent.ReflectBulletComponent;
import MazeGame.equipment.weaponComponent.SplitFireComponent;
import MazeGame.equipment.weaponComponent.WeaponComponent;

import java.util.ArrayList;
import java.util.Random;


public class InventoryFactory {

    private final ArrayList<ItemFactoryInterface<? extends Equipment>> allBoostItemList = new ArrayList<>();
    private final ArrayList<ItemFactoryInterface<? extends WeaponComponent>> allComponentItemList = new ArrayList<>();
    private final Random random = new Random();

    /***
     *  Register all the boost equipment
     */
    public InventoryFactory(){
        // Register all boost item list
        allBoostItemList.add(new ArmorPack(0).new ArmorPackFactory());
        allBoostItemList.add(new Charger(0).new ChargerFactory());
        allBoostItemList.add(new FireSpeedPack(0).new FireSpeedFactory());
        allBoostItemList.add(new HealPack(0).new HealPackFactory ());
        allBoostItemList.add(new WeaponUpgrade(0).new WeaponUpgradeFactory());

        // Register all weapon components
        allComponentItemList.add(new SplitFireComponent().new SplitFireComponentFactory());
        allComponentItemList.add(new ReflectBulletComponent().new ReflectBulletComponentFactory());
        allComponentItemList.add(new BoostSpeedComponent().new BoostSpeedComponentFactory());
    }

    public Equipment randomEquipment(int level){
        return allBoostItemList.get(random.nextInt(allBoostItemList.size())).create(level);
    }

    public WeaponComponent randomWeaponComponent(){
        return allComponentItemList.get(random.nextInt(allComponentItemList.size())).create(0);
    }
}
