package MazeGame;

import MazeGame.helper.creaturePositionRecorder;
import jdk.nashorn.internal.runtime.Context;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static MazeGame.Info.roomSize;

public class GameController {

    private ExecutorService pool = Executors.newFixedThreadPool(200);
    private Timer controlDriver = new Timer();
    private ArrayList<Enemy> existEnemies = new ArrayList<>();

    private Room[][] rooms;
    private Player player;
    private creaturePositionRecorder[] enemies ;
    private ArrayList<Integer> enemySlot;
    private Cell[][] totalMap;
    private GameResourceController gameResourceController;

    public GameController(GameResourceController gameResourceController){
        this.gameResourceController = gameResourceController;
        this.rooms = gameResourceController.getRooms();
        this.player = gameResourceController.getPlayer();
        this.enemies = gameResourceController.getEnemies();
        this.enemySlot = gameResourceController.getEnemySlot();
        this.totalMap = gameResourceController.getTotalMap();
        init();
    }

    public void init(){
        controlDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                detect();
            }
        },0 ,1000/3);
    }

    private void detect(){
        int roomI = player.getRoomI();
        int roomJ = player.getRoomJ();
        if(rooms[roomI][roomJ].visit()){
            for(int i = 0 ; i<1; i++){
                Enemy enemy;
                pool.execute(enemy = new Enemy(roomI,roomJ,gameResourceController));
                existEnemies.add(enemy);
            }
        }
    }

    public void clearAllEnemies(){
        controlDriver.cancel();

        // stop accepting new task
        pool.shutdown();
        try {
        // wait until all enemy create complete
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        for(Enemy enemy:existEnemies){
            if (enemy != null) {
                enemy.die();
            }
        }
        existEnemies.clear();


        // init enemies array
        enemySlot.clear();
        for(int i = 0; i<200; i++){
            enemies[i].clear();
            enemySlot.add(i);
        }
    }


    public void restart(){
        pool = Executors.newFixedThreadPool(200);
        controlDriver = new Timer();
        init();
    }

    public void regenerate() {
        // clear all enemy
        clearAllEnemies();
        gameResourceController.initMap();
        player.teleport(roomSize/3, roomSize/3);
        restart();
        gameResourceController.getGraphic().drawElements();
    }

    public void gameOver(){
        clearAllEnemies();
        gameResourceController.getPlayerShootingDriver().cancel();
        gameResourceController.getPlayerDriver().cancel();
        gameResourceController.getEffectDriver().cancel();
        gameResourceController.getCdTimer().cancel();
        gameResourceController.getGraphicDriver().cancel();
        gameResourceController.getjFrame().dispose();
        gameResourceController.getStartMenu().show();
    }
}
