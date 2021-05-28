package MazeGame.equipment.weaponComponent;

import MazeGame.bullets.Bullet;
import MazeGame.equipment.ItemFactoryInterface;
import MazeGame.helper.Event;
import MazeGame.helper.EventInfo;
import MazeGame.helper.FireData;

import java.util.ArrayList;

public class BoostSpeedComponent extends WeaponComponent{

    public BoostSpeedComponent(){
        super();
        this.priority = 1;
        this.name = "Bullet speed boost";
        this.icon = "B";
        this.description = "Boost up the speed of bullet";
    }

    @Override
    public void processFireData(FireData fireData) {
        ArrayList<Bullet> bullets = fireData.getBullets();
        if(bullets == null){
            return;
        }
        for(Bullet originBullet: bullets){
            originBullet.getCreateEvents().add(new Event() {
                @Override
                public void invoke(EventInfo info) {
                    originBullet.setSpeed(originBullet.getSpeed() * 1.5);
                }
            });
        }

    }

    public class BoostSpeedComponentFactory implements ItemFactoryInterface<BoostSpeedComponent>{

        @Override
        public BoostSpeedComponent create(int level) {
            return new BoostSpeedComponent();
        }
    }
}
