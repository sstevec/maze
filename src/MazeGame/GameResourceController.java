package MazeGame;

import MazeGame.creature.Player;
import MazeGame.effect.Effect;
import MazeGame.frame.GameInitializer;
import MazeGame.frame.ItemFrame;
import MazeGame.frame.StartMenu;
import MazeGame.graphic.AbilityCDGraphic;
import MazeGame.graphic.Graphic;
import MazeGame.helper.CreaturePositionRecorder;
import MazeGame.map.Cell;
import MazeGame.map.MazeGenerator;
import MazeGame.map.Room;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.helper.Info.roomSize;

public class GameResourceController {
    private int mapSize = 10;
    private int totalMapSize;
    private JFrame jFrame;
    private MazeGenerator mazeGenerator;
    private ItemFrame itemFrame;
    private Player player;
    private Graphic graphic;
    private GameController gameController;
    private StartMenu startMenu;

    private GameInitializer gameInitializer;

    private AbilityCDGraphic abilityCDGraphic;
    private java.util.Timer cdTimer = new Timer();

    private Cell[][] totalMap;
    private Room[][] rooms;

    private Timer graphicDriver = new Timer();

    private CreaturePositionRecorder[] creatures;
    private ArrayList<Integer> creatureSlot = new ArrayList<>();

    private CopyOnWriteArrayList<Effect> effects = new CopyOnWriteArrayList<>();
    private Timer effectDriver = new Timer();

    private Timer playerDriver = new Timer();

    private Timer playerShootingDriver = new Timer();



    public GameResourceController(GameInitializer initializer, StartMenu s, int mapSize){
        this.startMenu = s;
        this.gameInitializer = initializer;
        this.mapSize = mapSize;
        this.totalMapSize = mapSize * roomSize;
        this.mazeGenerator = new MazeGenerator(mapSize);
    }

    public void initMap(){
        mazeGenerator.generateRooms();
        totalMap = mazeGenerator.getTotalMap();
        rooms = mazeGenerator.getRooms();
    }

    public void initCreatureSlot(int slotSize){
        creatures =  new CreaturePositionRecorder[slotSize];
        for(int i = 1; i<slotSize; i++){
            creatures[i] = new CreaturePositionRecorder();
            creatureSlot.add(i);
        }
    }

    public void initPlayer(int x, int y){
        player = new Player( x, y, this);
        creatures[0] = new CreaturePositionRecorder();
        creatures[0].setiPos(player.getiPos());
        creatures[0].setjPos(player.getjPos());
        creatures[0].setColor(player.getColor());
        creatures[0].setBullets(player.getBullets());
        creatures[0].setCreatureReference(player);
    }

    public void initGraphicSystem(){
        graphic = new Graphic(this);
        abilityCDGraphic = new AbilityCDGraphic(this);
        itemFrame = new ItemFrame(this);
    }

    public void initGameController(){
        gameController = new GameController(this);
    }


    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    public void setjFrame(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    public MazeGenerator getMazeGenerator() {
        return mazeGenerator;
    }

    public void setMazeGenerator(MazeGenerator mazeGenerator) {
        this.mazeGenerator = mazeGenerator;
    }

    public ItemFrame getItemFrame() {
        return itemFrame;
    }

    public void setItemFrame(ItemFrame itemFrame) {
        this.itemFrame = itemFrame;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public StartMenu getStartMenu() {
        return startMenu;
    }

    public void setStartMenu(StartMenu startMenu) {
        this.startMenu = startMenu;
    }

    public GameInitializer getGameInitializer() {
        return gameInitializer;
    }

    public void setGameInitializer(GameInitializer gameInitializer) {
        this.gameInitializer = gameInitializer;
    }

    public AbilityCDGraphic getAbilityCDGraphic() {
        return abilityCDGraphic;
    }

    public void setAbilityCDGraphic(AbilityCDGraphic abilityCDGraphic) {
        this.abilityCDGraphic = abilityCDGraphic;
    }

    public Timer getCdTimer() {
        return cdTimer;
    }

    public void setCdTimer(Timer cdTimer) {
        this.cdTimer = cdTimer;
    }

    public Cell[][] getTotalMap() {
        return totalMap;
    }

    public void setTotalMap(Cell[][] totalMap) {
        this.totalMap = totalMap;
    }

    public Room[][] getRooms() {
        return rooms;
    }

    public void setRooms(Room[][] rooms) {
        this.rooms = rooms;
    }

    public Timer getGraphicDriver() {
        return graphicDriver;
    }

    public void setGraphicDriver(Timer graphicDriver) {
        this.graphicDriver = graphicDriver;
    }

    public CreaturePositionRecorder[] getCreatures() {
        return creatures;
    }

    public ArrayList<Integer> getCreatureSlot() {
        return creatureSlot;
    }

    public CopyOnWriteArrayList<Effect> getEffects() {
        return effects;
    }

    public void setEffects(CopyOnWriteArrayList<Effect> effects) {
        this.effects = effects;
    }

    public Timer getEffectDriver() {
        return effectDriver;
    }

    public void setEffectDriver(Timer effectDriver) {
        this.effectDriver = effectDriver;
    }

    public Timer getPlayerDriver() {
        return playerDriver;
    }

    public void setPlayerDriver(Timer playerDriver) {
        this.playerDriver = playerDriver;
    }

    public Timer getPlayerShootingDriver() {
        return playerShootingDriver;
    }

    public void setPlayerShootingDriver(Timer playerShootingDriver) {
        this.playerShootingDriver = playerShootingDriver;
    }

    public int getTotalMapSize() {
        return totalMapSize;
    }

    public void setTotalMapSize(int totalMapSize) {
        this.totalMapSize = totalMapSize;
    }
}
