package MazeGame.equipment.weaponComponent;

import MazeGame.bullets.Bullet;
import MazeGame.bullets.FireBullet;
import MazeGame.equipment.ItemFactoryInterface;
import MazeGame.helper.FireData;

import java.util.ArrayList;
import java.util.Map;

public class SplitFireComponent extends WeaponComponent{

    public SplitFireComponent(){
        super();
        this.priority = 1;
        this.name = "Bullet split device";
        this.icon = "S";
        this.description = "Every time you shoot, it will create two copy of " +
                "your bullet and shoot them in 30 degree angle with half of the damage";
    }

    @Override
    public void processFireData(FireData fireData) {
        ArrayList<Bullet> bullets = fireData.getBullets();
        ArrayList<Bullet> newBullet = new ArrayList<>();
        if(bullets == null){
            return;
        }
        for(Bullet originBullet: bullets){
            double degree = Math.toDegrees(Math.atan(originBullet.getyDir() / originBullet.getxDir()));
            if (originBullet.getxDir() < 0) {
                degree = degree - 180;
            }

            double newRDown = Math.toRadians(degree - 30);
            double newRUp = Math.toRadians(degree + 30);
            newBullet.add(originBullet.copy(originBullet.getX(), originBullet.getY(), Math.cos(newRDown),
                    Math.sin(newRDown), originBullet.getSpeed(), originBullet.getDamage()/2));
            newBullet.add(originBullet.copy(originBullet.getX(), originBullet.getY(), Math.cos(newRUp),
                    Math.sin(newRUp), originBullet.getSpeed(), originBullet.getDamage()/2));

        }
        bullets.addAll(newBullet);
    }

    public class SplitFireComponentFactory implements ItemFactoryInterface<SplitFireComponent>{

        @Override
        public SplitFireComponent create(int level) {
            return new SplitFireComponent();
        }
    }
}
