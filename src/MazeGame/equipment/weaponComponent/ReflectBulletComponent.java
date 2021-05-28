package MazeGame.equipment.weaponComponent;

import MazeGame.bullets.Bullet;
import MazeGame.equipment.ItemFactoryInterface;
import MazeGame.helper.Event;
import MazeGame.helper.EventInfo;
import MazeGame.helper.FireData;

import java.util.ArrayList;

public class ReflectBulletComponent extends WeaponComponent {

    public ReflectBulletComponent() {
        super();
        this.priority = 1;
        this.name = "Bullet Reflect device";
        this.icon = "R";
        this.description = "Every bullet will reflect once when it hits the wall";
    }

    @Override
    public void processFireData(FireData fireData) {
        ArrayList<Bullet> bullets = fireData.getBullets();
        if (bullets == null) {
            return;
        }
        for (Bullet originBullet : bullets) {
            originBullet.getDieEvents().add(new Event() {
                @Override
                public void invoke(EventInfo info) {
                    String reflectDir = (String) info.getDataMap().get("reflectDir");
                    if (reflectDir != null) {
                        ArrayList<Bullet> list = new ArrayList<>();
                        if (reflectDir.equals("x")) {
                            list.add(originBullet.copy(originBullet.getX(), originBullet.getY(), -1 * originBullet.getxDir(),
                                    originBullet.getyDir(), originBullet.getSpeed(), originBullet.getDamage()));
                        } else {
                            list.add(originBullet.copy(originBullet.getX(), originBullet.getY(), originBullet.getxDir(),
                                    -1 * originBullet.getyDir(), originBullet.getSpeed(), originBullet.getDamage()));
                        }
                        info.getSource().addBullets(list);
                    }
                }
            });
        }
    }

    public class ReflectBulletComponentFactory implements ItemFactoryInterface<ReflectBulletComponent> {

        @Override
        public ReflectBulletComponent create(int level) {
            return new ReflectBulletComponent();
        }
    }
}
