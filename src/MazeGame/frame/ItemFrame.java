package MazeGame.frame;

import MazeGame.GameResourceController;
import MazeGame.creature.Player;
import MazeGame.equipment.weaponComponent.WeaponComponent;
import MazeGame.weapons.Weapon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ItemFrame {

    private boolean visible = false;

    private Player player;
    private JFrame jFrame;
    private JLabel nameTag;
    private JLabel begTag;
    private JLabel weaponTag;
    private JLabel weaponDamageTag;
    private JLabel weaponFireRateTag;
    private JLabel weaponBulletSpeedTag;

    private JLabel weaponDamage;
    private JLabel weaponFireRate;
    private JLabel weaponBulletSpeed;

    private Weapon weapon;
    private final ArrayList<JButton> beg;
    private final ArrayList<JButton> equipSlots;
    private final int begSlotPerRow = 4;
    private final int equipSlotPerRow = 3;

    public ItemFrame(GameResourceController gameResourceController) {
        this.player = gameResourceController.getPlayer();
        beg = new ArrayList<>();
        equipSlots = new ArrayList<>();
        jFrame = new JFrame("Bag");

        jFrame.setBounds(600, 300, 615, 425);
        jFrame.setVisible(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLayout(null);
        jFrame.setFocusable(true);

        nameTag = new JLabel("Name");
        weaponTag = new JLabel("Weapon");
        weaponDamage = new JLabel("0");
        weaponFireRate = new JLabel("0");
        weaponBulletSpeed = new JLabel("0");
        weaponDamageTag = new JLabel("Damage");
        weaponFireRateTag = new JLabel("Fire Rate");
        weaponBulletSpeedTag = new JLabel("Bullet Speed");
        begTag = new JLabel("My Beg");

        nameTag.setBounds(100, 20, 100, 40);
        weaponTag.setBounds(430, 20, 100, 40);
        weaponDamage.setBounds(500, 200, 50, 30);
        weaponFireRate.setBounds(500, 240, 50, 30);
        weaponBulletSpeed.setBounds(500, 280, 50, 30);
        weaponDamageTag.setBounds(400, 200, 90, 30);
        weaponFireRateTag.setBounds(400, 240, 90, 30);
        weaponBulletSpeedTag.setBounds(400, 280, 90, 30);
        begTag.setBounds(110, 60, 100, 40);

        jFrame.getContentPane().add(nameTag);
        jFrame.getContentPane().add(weaponTag);
        jFrame.getContentPane().add(weaponDamage);
        jFrame.getContentPane().add(weaponBulletSpeed);
        jFrame.getContentPane().add(weaponFireRate);
        jFrame.getContentPane().add(weaponDamageTag);
        jFrame.getContentPane().add(weaponBulletSpeedTag);
        jFrame.getContentPane().add(weaponFireRateTag);
        jFrame.getContentPane().add(begTag);

        WeaponComponent[] playerBeg = player.getBeg();
        for (int compNum = 0; compNum < playerBeg.length; compNum++) {
                JButton begSlot = new JButton("");
                begSlot.setBounds(20 + compNum % begSlotPerRow * 60, 110 + compNum / begSlotPerRow * 70, 50, 50);
                jFrame.getContentPane().add(begSlot);
                beg.add(begSlot);
        }

        WeaponComponent[] playerEquip = player.getPostProcessComponent();
        for (int compNum = 0; compNum < playerEquip.length; compNum++) {
            JButton equipSlot = new JButton("");
            equipSlot.setBounds(400 + compNum % equipSlotPerRow * 60, 70 + compNum / equipSlotPerRow * 70, 50, 50);
            jFrame.getContentPane().add(equipSlot);
            equipSlots.add(equipSlot);
        }

        jFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char charA = e.getKeyChar();
                if (charA == 'q') {
                    show();
                }
            }
        });
        initFrame();
        jFrame.requestFocus();
    }

    private void initFrame() {
        weapon = player.getWeapon();

        nameTag.setText(player.getName());
        weaponTag.setText(weapon.getName());
        weaponDamage.setText(weapon.getDamage() + "");
        weaponFireRate.setText(weapon.getFireRate() + "");
        weaponBulletSpeed.setText(weapon.getBulletSpeed() + "");

        updateBegSlot();
        updateEquipSlot();
    }

    private void updateBegSlot(){
        WeaponComponent[] playerBeg = player.getBeg();

        for (int compNum = 0; compNum < playerBeg.length; compNum++) {
            JButton button = beg.get(compNum);
            if (playerBeg[compNum] == null) {
                button.setText("");
                button.setToolTipText("Empty!");
                button.setFocusable(false);
            } else {
                button.setFocusable(true);
                button.setText(playerBeg[compNum].getIcon());
                button.setToolTipText(playerBeg[compNum].getName() + ":\n" + playerBeg[compNum].getDescription());
                int finalCompNum = compNum;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        WeaponComponent com = playerBeg[finalCompNum];
                        if(player.putIntoPostProcess(com)){
                            playerBeg[finalCompNum] = null;
                            button.setText("");
                            button.setToolTipText("Empty!");
                            button.removeActionListener(this);
                            button.setFocusable(false);
                            updateEquipSlot();
                            jFrame.requestFocus();
                        }

                    }
                });
            }
        }
    }

    private void updateEquipSlot(){
        WeaponComponent[] playerEquip = player.getPostProcessComponent();

        for (int compNum = 0; compNum < playerEquip.length; compNum++) {
            JButton button = equipSlots.get(compNum);
            if (playerEquip[compNum] == null) {
                button.setText("");
                button.setToolTipText("Empty!");
                button.setFocusable(false);
            } else {
                button.setFocusable(true);
                button.setText(playerEquip[compNum].getIcon());
                button.setToolTipText(playerEquip[compNum].getName() + ":\n" + playerEquip[compNum].getDescription());
                int finalCompNum = compNum;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        WeaponComponent com = playerEquip[finalCompNum];
                        if(player.putIntoBeg(com)){
                            playerEquip[finalCompNum] = null;
                            button.setText("");
                            button.setToolTipText("Empty!");
                            button.removeActionListener(this);
                            button.setFocusable(false);
                            updateBegSlot();
                            jFrame.requestFocus();
                        }
                    }
                });
            }
        }
    }
    
    public void show() {
        if (!visible) {
            visible = true;
            initFrame();
        } else {
            visible = false;
        }
        jFrame.setVisible(visible);
    }
}
