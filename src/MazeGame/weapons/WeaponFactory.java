package MazeGame.weapons;

import java.util.ArrayList;
import java.util.Random;

interface WeaponInstance<T>{
    public T create();
}

public class WeaponFactory {

    private ArrayList<WeaponInstance<? extends Weapon>> allWeaponList = new ArrayList<>();
    private Random random = new Random();

    /***
     *  Register all the weapon
     */
    public WeaponFactory(){
        // Register all weapon list
        allWeaponList.add(new BrokenGun().new BrokenGunFactory());
        allWeaponList.add(new FireGun().new FireGunFactory());
        allWeaponList.add(new Gun().new GunFactory());
        allWeaponList.add(new MindControlGun().new MindControlGunFactory());
        allWeaponList.add(new Rocket().new RocketFactory());

    }

    public Weapon randomWeapon(){
        return allWeaponList.get(random.nextInt(allWeaponList.size())).create();
    }
}
