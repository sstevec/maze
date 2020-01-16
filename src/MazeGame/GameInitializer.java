package MazeGame;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.*;

public class GameInitializer{

    private int mapSize = 10;
    private JFrame jFrame;
    private MazeGenerator mazeGenerator;
    private ItemFrame itemFrame;
    private Player player;
    private Graphic graphic;
    private GameInitializer gameInitializer = this;

    private Cell[][] totalMap;
    private Room[][] rooms;
    private CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    private Timer bulletDriver = new Timer();

    private CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    private Timer enemyDriver = new Timer();

    GameInitializer(int mapSize) {
        this.mapSize = mapSize;
        mazeGenerator = new MazeGenerator(mapSize);
        mazeGenerator.generateRooms();

        totalMap = mazeGenerator.getTotalMap();
        rooms = mazeGenerator.getRooms();

        // init map variables
        player = new Player(mapSize * roomSize, 1, 1, totalMap,rooms, enemies);
        graphic = new Graphic(totalMap, player,bullets,enemies);

        itemFrame = new ItemFrame(player);
    }

    public void initGame() {
        jFrame = new JFrame("MAZE");

        // 10 margin, and 20 cells on each side
        jFrame.setBounds(300, 100, 1215, 825);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add elements to frame
        jFrame.add(graphic);
        graphic.draw(1, 1);
        startGame();
    }

    private void startGame() {
        jFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char charA = e.getKeyChar();
                if (charA == 'w') {
                    player.move("up");
                    graphic.draw(player.getX(), player.getY());
                } else if (charA == 'a') {
                    player.move("left");
                    graphic.draw(player.getX(), player.getY());
                } else if (charA == 's') {
                    player.move("down");
                    graphic.draw(player.getX(), player.getY());
                } else if (charA == 'd') {
                    player.move("right");
                    graphic.draw(player.getX(), player.getY());
                }else if (charA == 'q') {
                    itemFrame.show();
                }else if (charA == 'f') {
                    player.pick();
                }else if (charA == 'g') {
                    player.drop();
                }else if (charA == 'e') {
                    if(totalMap[player.getX()][player.getY()].getIntractable() != null){
                        totalMap[player.getX()][player.getY()].getIntractable().interact(gameInitializer);
                    }
                }
            }
        });

        jFrame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    // left button pressed
                    ArrayList<Bullet> temp = player.fire(e.getX(), e.getY());
                    if (temp != null) {
                        bullets.addAll(temp);
                    }
                } else if (e.getButton() == 3) {
                    // left button pressed
                    ArrayList<Bullet> temp = player.castAbility(e.getX(), e.getY());
                    if (temp != null) {
                        bullets.addAll(temp);
                    }
                }
            }
        });

        // keep update bullet location
        bulletDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                // let bullet fly
                for (Bullet temp : bullets
                ) {
                    temp.fly();
                }

                // after flying, check if it reach the bound
                int bulletSize = bullets.size();
                for (int i = 0; i<bulletSize; i++) {
                    Bullet temp = bullets.get(i);
                    int j1 = (int) (temp.getX() + 5) / 15;
                    int i1 = (int) (temp.getY() + 5) / 15;
                    int j2 = (int) temp.getX() / 15;
                    int i2 = (int) temp.getY() / 15;
                    if (totalMap[i1][j1].boarder || totalMap[i2][j2].boarder) {
                        bullets.remove(i);
                        i--;
                        bulletSize--;
                    }
                    if (totalMap[i1][j1].getOccupiedCreature() != null) {
                        if (totalMap[i1][j1].getOccupiedCreature().getTeamNumber() != temp.getBelongTeam()) {
                            totalMap[i1][j1].getOccupiedCreature().takeDamage(temp.getDamage());
                            bullets.remove(i);
                            i--;
                            bulletSize--;
                            continue;
                        }
                    }
                    if (totalMap[i2][j2].getOccupiedCreature() != null) {
                        if (totalMap[i2][j2].getOccupiedCreature().getTeamNumber() != temp.getBelongTeam()) {
                            totalMap[i2][j2].getOccupiedCreature().takeDamage(temp.getDamage());
                            bullets.remove(i);
                            i--;
                            bulletSize--;
                        }
                    }
                }

                int enemySize = enemies.size();
                for (int i = 0 ; i<enemySize; i++) {
                    Enemy temp = enemies.get(i);
                    if(temp.getCurrentHealth() <= 0){
                        enemies.remove(i);
                        i--;
                        enemySize--;
                    }
                }

                graphic.drawElements();
            }
        }, 0, 1000 / 40);

        // keep the enemy moving
        enemyDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                // make the enemy move
                for (Enemy temp : enemies
                ) {
                    ArrayList<Bullet> tempBullet = temp.move();
                    if (tempBullet != null) {
                        bullets.addAll(tempBullet);
                    }
                }
            }
        }, 0, 1000 / 2);

    }

    public void regenerate(){
        mazeGenerator.generateRooms();
        player.teleport(1,1);
        enemies.clear();
        bullets.clear();
        graphic.draw(1,1);
    }
}
