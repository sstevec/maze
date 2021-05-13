package MazeGame;

public class Item {
    protected String name;
    private int kind;           // 1 stand for weapon, 2 stand for equipment

    public Item(String name, int kind){
        this.name = name;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public int getKind(){
        return kind;
    }
}
