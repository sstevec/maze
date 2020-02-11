package MazeGame;

import MazeGame.effect.Effect;

import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.Info.roomSize;

public class GameInitializer {

    private int mapSize = 10;
    private JFrame jFrame;
    private MazeGenerator mazeGenerator;
    private ItemFrame itemFrame;
    private Player player;
    private Graphic graphic;

    private GameInitializer gameInitializer = this;

    private AbilityCDGraphic abilityCDGraphic;
    private Timer cdTimer = new Timer();

    private Cell[][] totalMap;
    private Room[][] rooms;

    private Timer graphicDriver = new Timer();

    private CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    private Timer enemyDriver = new Timer();

    private CopyOnWriteArrayList<Effect> effects = new CopyOnWriteArrayList<>();
    private Timer effectDriver = new Timer();

    private Timer playerDriver = new Timer();
    private boolean U = false, D = false, L = false, R = false;

    private Timer playerShootingDriver = new Timer();
    private boolean openFire = false;
    private int mouseX, mouseY;

    GameInitializer(int mapSize) {
        this.mapSize = mapSize;
        mazeGenerator = new MazeGenerator(mapSize);
        mazeGenerator.generateRooms();

        totalMap = mazeGenerator.getTotalMap();
        rooms = mazeGenerator.getRooms();

        // init map variables
        player = new Player(mapSize * roomSize, 1, 1, totalMap, rooms, enemies, effects);
        graphic = new Graphic(totalMap, player, enemies, effects);
        abilityCDGraphic = new AbilityCDGraphic(player);

        itemFrame = new ItemFrame(player);
    }

    public void initGame() {
        jFrame = new JFrame("MAZE");

        // 10 margin, and 20 cells on each side
        jFrame.setBounds(300, 100, 1215, 825);
        jFrame.setVisible(true);
        jFrame.setLayout(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add elements to frame
        JLayeredPane jLayeredPane = new JLayeredPane();
        jLayeredPane.add(graphic, new Integer(100));
        jLayeredPane.add(abilityCDGraphic, new Integer(200));
        jLayeredPane.setBounds(0, 0, 1215, 825);
        jFrame.add(jLayeredPane);

        graphic.drawElements();
        startGame();
    }

    private void startGame() {

        jFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char charA = e.getKeyChar();
                if (charA == 'w') {
                    U = true;
                } else if (charA == 'a') {
                    L = true;
                } else if (charA == 's') {
                    D = true;
                } else if (charA == 'd') {
                    R = true;
                } else if (charA == 'q') {
                    U = false;
                    R = false;
                    D = false;
                    L = false;
                    itemFrame.show();
                } else if (charA == 'f') {
                    player.pick();
                } else if (charA == 'g') {
                    player.drop();
                } else if (charA == 'e') {
                    if (totalMap[player.getX()][player.getY()].getIntractable() != null) {
                        totalMap[player.getX()][player.getY()].getIntractable().interact(gameInitializer);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        L = false;
                        break;
                    case KeyEvent.VK_D:
                        R = false;
                        break;
                    case KeyEvent.VK_W:
                        U = false;
                        break;
                    case KeyEvent.VK_S:
                        D = false;
                        break;
                }
            }
        });

        jFrame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    // left button pressed
                    openFire = true;
                    mouseX = e.getX();
                    mouseY = e.getY();
                } else if (e.getButton() == 3) {
                    // right button pressed
                    player.castAbility(e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    // left button pressed
                    openFire = false;
                }
            }

        });

        jFrame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });


        // all drivers from here

        playerDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                if (U) {
                    player.move("up");
                }
                if (D) {
                    player.move("down");
                }
                if (L) {
                    player.move("left");
                }
                if (R) {
                    player.move("right");
                }
            }
        }, 0, 1000 / 35);

        playerShootingDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                if (openFire) {
                    player.fire(mouseX, mouseY);
                }
            }
        }, 0, 1000 / 20);


        // keep checking enemy
        enemyDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                int enemySize = enemies.size();
                for (int i = 0; i < enemySize; i++) {
                    Enemy temp = enemies.get(i);
                    if (temp.getCurrentHealth() <= 0) {
                        enemies.remove(i);
                        i--;
                        enemySize--;
                        temp.die();
                    }
                }
            }
        }, 0, 1000 / 5);

        effectDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                int effectSize = effects.size();
                for (int i = 0; i < effectSize; i++) {
                    Effect temp = effects.get(i);
                    if (!temp.isDealDamage()) {
                        temp.doDamage(totalMap);
                    } else {
                        if (temp.disappear()) {
                            effects.remove(i);
                            i--;
                            effectSize--;
                        } else {
                            temp.animate();
                        }
                    }
                }
            }
        }, 0, 1000 / 30);

        graphicDriver.schedule(new TimerTask() {
            @Override
            public void run() {
                graphic.drawElements();
            }
        }, 0, 1000 / 40);

        cdTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                abilityCDGraphic.repaint();
            }
        }, 0, 1000 / 10);
    }

    public void regenerate() {
        mazeGenerator.generateRooms();
        player.teleport(1, 1);
        // clear all enemy
        for (Enemy temp : enemies
        ) {
            temp.die();
        }
        enemies.clear();
        graphic.drawElements();
    }
}
