package MazeGame.equipment;

public interface ItemFactoryInterface<T>{
    public T create(int level);
}
