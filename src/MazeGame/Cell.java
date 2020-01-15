package MazeGame;

public class Cell {

    private int x; // row number
    private int y; // column number
    Boolean boarder = false;
    private Creature occupiedCreature = null;
    private Weapon fallenWeapon = null;

    public void setBoarder(Boolean boarder) {
        this.boarder = boarder;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Creature getOccupiedCreature() {
        return occupiedCreature;
    }

    public void setOccupiedCreature(Creature occupiedCreature) {
        this.occupiedCreature = occupiedCreature;
    }

    public Weapon getFallenWeapon() {
        return fallenWeapon;
    }

    public void setFallenWeapon(Weapon fallenWeapon) {
        this.fallenWeapon = fallenWeapon;
    }
}