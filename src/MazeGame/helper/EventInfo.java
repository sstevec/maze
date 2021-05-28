package MazeGame.helper;

import MazeGame.creature.Creature;

import java.util.HashMap;

public class EventInfo {

    protected Creature source;
    protected HashMap<String, Object> dataMap;

    public EventInfo(Creature source){
        this.source = source;
        dataMap = new HashMap<>();
    }

    public HashMap<String, Object> getDataMap() {
        return dataMap;
    }

    public Creature getSource() {
        return source;
    }
}
