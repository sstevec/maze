package MazeGame;

import MazeGame.weapons.Weapon;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ItemFrame {

    private boolean visible = false;

    private Player player;
    private JFrame jFrame;
    private JLabel nameTag;
    private JLabel weaponTag;
    private JLabel weaponDamageTag;
    private JLabel weaponFireRateTag;
    private JLabel weaponBulletSpeedTag;

    private JLabel weaponDamage;
    private JLabel weaponFireRate;
    private JLabel weaponBulletSpeed;

    private Weapon weapon;

    public ItemFrame(GameResourceController gameResourceController){
        this.player = gameResourceController.getPlayer();
        jFrame = new JFrame("Bag");

        jFrame.setBounds(600, 300, 615, 425);
        jFrame.setVisible(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLayout(null);

        nameTag = new JLabel("Name");
        weaponTag = new JLabel("Weapon");
        weaponDamage = new JLabel("0");
        weaponFireRate = new JLabel("0");
        weaponBulletSpeed = new JLabel("0");
        weaponDamageTag = new JLabel("Damage");
        weaponFireRateTag = new JLabel("Fire Rate");
        weaponBulletSpeedTag = new JLabel("Bullet Speed");

        nameTag.setBounds(100,20,100,40);
        weaponTag.setBounds(450,20,100,40);
        weaponDamage.setBounds(500,200,50,30);
        weaponFireRate.setBounds(500,240,50,30);
        weaponBulletSpeed.setBounds(500,280,50,30);
        weaponDamageTag.setBounds(400,200,90,30);
        weaponFireRateTag.setBounds(400,240,90,30);
        weaponBulletSpeedTag.setBounds(400,280,90,30);

        jFrame.getContentPane().add(nameTag);
        jFrame.getContentPane().add(weaponTag);
        jFrame.getContentPane().add(weaponDamage);
        jFrame.getContentPane().add(weaponBulletSpeed);
        jFrame.getContentPane().add(weaponFireRate);
        jFrame.getContentPane().add(weaponDamageTag);
        jFrame.getContentPane().add(weaponBulletSpeedTag);
        jFrame.getContentPane().add(weaponFireRateTag);

        initFrame();

        jFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char charA = e.getKeyChar();
                if (charA == 'q') {
                    show();
                }
            }
        });
    }

    private void initFrame(){
        weapon = player.getWeapon();

        nameTag.setText(player.getName());
        weaponTag.setText(weapon.getName());
        weaponDamage.setText(weapon.getDamage()+"");
        weaponFireRate.setText(weapon.getFireRate()+"");
        weaponBulletSpeed.setText(weapon.getBulletSpeed()+"");
    }

    public void show(){
        if(!visible) {
            visible = true;
            initFrame();
        }else{
            visible = false;
        }
        jFrame.setVisible(visible);
    }
}
