package MazeGame;

import MazeGame.Interactions.Intractable;

public class Cell {

    private int x; // row number
    private int y; // column number
    private boolean boarder = false;

    private Item fallenItem = null;
    private Intractable intractable = null;

    public Intractable getIntractable() {
        return intractable;
    }

    public void setIntractable(Intractable intractable) {
        this.intractable = intractable;
        if(intractable != null) {
            this.intractable.setLocation(this);
        }
    }

    public void setBoarder(boolean boarder) {
        this.boarder = boarder;
    }

    public boolean isBoarder() {
        return boarder;
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

    public Item getFallenItem() {
        return fallenItem;
    }

    public void setFallenItem(Item fallenItem) {
        this.fallenItem = fallenItem;
    }
}
