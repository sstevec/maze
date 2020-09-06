package MazeGame;

import MazeGame.helper.enemyPositionRecorder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class gameController {

    private Executor pool = Executors.newFixedThreadPool(200);
    private Timer controlDriver = new Timer();
    private ArrayList<Enemy> existEnemies = new ArrayList<>();

    private Room[][] rooms;
    private Player player;
    private enemyPositionRecorder[] enemies ;
    private ArrayList<Integer> enemySlot;
    private Cell[][] totalMap;

    public gameController(Room[][] rooms, Player player, enemyPositionRecorder[] enemies, ArrayList<Integer> enemySlot, Cell[][] totalMap){
        this.rooms = rooms;
        this.player = player;
        this.enemies = enemies;
        this.enemySlot = enemySlot;
        this.totalMap = totalMap;
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
            int enemyNum = rooms[roomI][roomJ].getEnemyNumber();
            for(int i = 0 ; i<1; i++){
                Enemy enemy;
                pool.execute(enemy = new Enemy(roomI,roomJ,player,rooms,totalMap,enemies,enemySlot));
                existEnemies.add(enemy);
            }
        }
    }

    public void clearAllEnemies(){
        controlDriver.cancel();
        for(Enemy enemy:existEnemies){
            enemy.die();
        }

        // init enemies array
        enemySlot.clear();
        for(int i = 0; i<200; i++){
            enemies[i].clear();
            enemySlot.add(i);
        }
    }
}
