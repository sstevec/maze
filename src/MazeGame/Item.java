package MazeGame;

import MazeGame.creature.Creature;

public abstract class Item {
    protected String name;
    private final int kind;           // 1 stand for weapon, 2 stand for boost, 3 for weapon component

    public Item(int kind){
        this.kind = kind;
    }

    public abstract void pickUp(Creature creature);

    public String getName() {
        return name;
    }

    public int getKind(){
        return kind;
    }
}
