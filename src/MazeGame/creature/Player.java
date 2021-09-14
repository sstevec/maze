package MazeGame.creature;

import MazeGame.GameResourceController;
import MazeGame.Item;
import MazeGame.map.Room;
import MazeGame.effect.Effect;
import MazeGame.equipment.boost.Equipment;
import MazeGame.equipment.weaponComponent.WeaponComponent;
import MazeGame.frame.GameInitializer;
import MazeGame.helper.Info;
import MazeGame.weapons.*;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.helper.Info.*;

public class Player extends Creature {

    private int roomI = 0;
    private int roomJ = 0;

    private String name = "Player 1";

    private int mapSize;
    private int totalMapSize;
    private final Room[][] rooms;
    private final CopyOnWriteArrayList<Effect> effects;
    private final Random random = new Random();
    private final GameInitializer gameInitializer;


    public Player(int iPos, int jPos, GameResourceController gameResourceController) {
        super(100, 100, 1, gameResourceController);
        this.iDPos = iPos;
        this.jDPos = jPos;

        this.totalMapSize = gameResourceController.getTotalMapSize();
        this.color = Color.CYAN;
        this.effects = gameResourceController.getEffects();
        weapon = new MindControlGun(color, getTeamNumber(), effects, this);
        this.rooms = gameResourceController.getRooms();
        this.mapSize = rooms.length;
        this.gameInitializer = gameResourceController.getGameInitializer();
    }

    @Override
    protected void customInit() {
    }

    /***
     *
     * @param xDest
     * @param yDest
     *
     * Note here xDest and yDest are relative position of mouse to the current game board
     * Thus, to calculate the definite position, we need to plus the board distance
     */

    public void fire(int xDest, int yDest) {

        weapon.CheckFireStatus(jDPos * Info.cellWidth + Info.cellWidth / 2, iDPos * Info.cellWidth + Info.cellWidth / 2,
                xDest + gameResourceController.getxBorder() * Info.cellWidth - 10, yDest + gameResourceController.getyBorder() * Info.cellWidth - 35);
    }

    public void castAbility(int xDest, int yDest) {
        weapon.CheckAbilityStatus(jDPos * Info.cellWidth + Info.cellWidth / 2, iDPos * Info.cellWidth + Info.cellWidth / 2,
                xDest + gameResourceController.getxBorder() * Info.cellWidth - 10, yDest + gameResourceController.getyBorder() * Info.cellWidth - 35);
    }

    public void move(String dir) {
        double newI = iDPos;
        double newJ = jDPos;
        if (dir.equals("up")) {
            newI = iDPos - moveSpeed;
        } else if (dir.equals("down")) {
            newI = iDPos + moveSpeed;
        } else if (dir.equals("left")) {
            newJ = jDPos - moveSpeed;
        } else if (dir.equals("right")) {
            newJ = jDPos + moveSpeed;
        }
        if (newI < 1) {
            newI = 1;
        }
        if (newJ < 1) {
            newJ = 1;
        }
        if (newI >= totalMapSize) {
            newI = totalMapSize - 1;
        }
        if (newJ >= totalMapSize) {
            newJ = totalMapSize - 1;
        }

        // check if new position is wall
        if (cellInfo[(int) (newI + 0.5)][(int) (newJ + 0.5)].isBoarder() ||
                cellInfo[(int) (newI)][(int) (newJ)].isBoarder()) {
            return;
        }

        iDPos = newI;
        jDPos = newJ;
        roomI = (int)iDPos / roomSize;
        roomJ = (int)jDPos / roomSize;

        creatures[0].setiDPos(iDPos);
        creatures[0].setjDPos(jDPos);

    }


    @Override
    public void dieClear() {
        gameResourceController.getGameController().gameOver();
    }

    @Override
    public void dieEffect() {

    }

    public void pick() {
        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                if (iDPos + i < cellInfo.length && iDPos + i >= 0 && jDPos + j < cellInfo.length && jDPos + j >= 0) {
                    Item fallenItem = cellInfo[(int)iDPos + i][(int)jDPos + j].getFallenItem();
                    if (fallenItem != null) {
                        // there is an item
                        if (fallenItem.getKind() == WEAPON_KIND) {
                            // it is a weapon
                            Weapon tempWeapon = null;
                            if (!(weapon instanceof NoWeapon)) {
                                // means player equipped with a weapon
                                tempWeapon = weapon;
                            }
                            weapon = (Weapon) fallenItem;
                            weapon.setColor(color);
                            weapon.setBelongTeam(getTeamNumber());
                            weapon.setUser(this);
                            weapon.setEffects(effects);
                            weapon.upgradeAttackSpeed(extraAttackSpeed);
                            weapon.upgradeDamage(extraDamage);
                            weapon.upgradeExtraCD(extraCDReduce);
                            cellInfo[(int)iDPos + i][(int)jDPos + j].setFallenItem(tempWeapon);
                            return;
                        } else if (fallenItem.getKind() == BOOST_KIND) {
                            // it is a equipment
                            Equipment fallenEquipment = (Equipment) fallenItem;
                            fallenEquipment.pickUp(this);
                            cellInfo[(int)iDPos + i][(int)jDPos + j].setFallenItem(null);
                            return;
                        } else if (fallenItem.getKind() == WEAPON_COMPONENT_KIND) {
                            WeaponComponent weaponComponent = (WeaponComponent) fallenItem;
                            weaponComponent.pickUp(this);
                            cellInfo[(int)iDPos + i][(int)jDPos + j].setFallenItem(null);
                            return;
                        }

                    }
                }

            }
        }

    }

    public void drop() {
        if (weapon.getDamage() != 0) {
            // means player equipped with a weapon
            if (cellInfo[(int)iDPos][(int)jDPos].getFallenItem() == null) {
                cellInfo[(int)iDPos][(int)jDPos].setFallenItem(weapon);
                weapon = new NoWeapon(Color.CYAN, getTeamNumber(), effects, this);
            }
        }
    }

    public void interact() {
        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                if (iDPos + i < cellInfo.length && iDPos + i >= 0 && jDPos + j < cellInfo.length && jDPos + j >= 0) {
                    if (cellInfo[(int)iDPos + i][(int)jDPos + j].getIntractable() != null) {
                        cellInfo[(int)iDPos + i][(int)jDPos + j].getIntractable().interact(gameResourceController);
                        return;
                    }
                }

            }
        }
    }

    public void teleport(int iPos, int jPos) {
        this.iDPos = iPos;
        this.jDPos = jPos;

        roomI = iPos / roomSize;
        roomJ = jPos / roomSize;
    }

    public double getWeaponCD() {
        return weapon.getCD();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getRoomI() {
        return roomI;
    }

    public int getRoomJ() {
        return roomJ;
    }
}
