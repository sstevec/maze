package MazeGame.frame;

import MazeGame.map.Cell;
import MazeGame.GameResourceController;
import MazeGame.creature.Player;
import MazeGame.effect.Effect;
import MazeGame.graphic.AbilityCDGraphic;
import MazeGame.graphic.Graphic;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.event.*;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static MazeGame.helper.Info.roomSize;

public class GameInitializer {

    private GameResourceController gameResourceController;
    private boolean U = false, D = false, L = false, R = false;
    private int mouseX, mouseY;
    private boolean openFire = false;
    private Graphic graphic;
    private AbilityCDGraphic abilityCDGraphic;
    private Cell[][] totalMap;
    private CopyOnWriteArrayList<Effect> effects;

    GameInitializer(int mapSize, StartMenu startMenu) {
        // init resource center
        gameResourceController = new GameResourceController(this, startMenu, mapSize);

        // let resource center init map
        gameResourceController.initMap();

        // let resource center init enemies
        gameResourceController.initCreatureSlot(200);

        // let resource center init player
        gameResourceController.initPlayer(roomSize / 3, roomSize / 3);

        // let resource center init graphic
        gameResourceController.initGraphicSystem();

        // let resource center init game controller
        gameResourceController.initGameController();

        graphic = gameResourceController.getGraphic();
        abilityCDGraphic = gameResourceController.getAbilityCDGraphic();
        totalMap = gameResourceController.getTotalMap();
        effects = gameResourceController.getEffects();
    }

    public void initGame() {
        JFrame jFrame = new JFrame("MAZE");

        // 10 margin, and 20 cells on each side
        jFrame.setBounds(300, 100, 1215, 855);
        jFrame.setVisible(true);
        jFrame.setLayout(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add elements to frame
        JLayeredPane jLayeredPane = new JLayeredPane();
        jLayeredPane.add(gameResourceController.getGraphic(), new Integer(100));
        jLayeredPane.add(gameResourceController.getAbilityCDGraphic(), new Integer(200));
        jLayeredPane.setBounds(0, 0, 1215, 825);
        jFrame.add(jLayeredPane);

        gameResourceController.getGraphic().drawElements();
        gameResourceController.setjFrame(jFrame);
        startGame();
    }

    private void startGame() {
        JFrame gamePanel = gameResourceController.getjFrame();
        Player player = gameResourceController.getPlayer();

        gamePanel.addKeyListener(new KeyAdapter() {
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
                    gameResourceController.getItemFrame().show();
                } else if (charA == 'f') {
                    player.pick();
                } else if (charA == 'g') {
                    player.drop();
                } else if (charA == 'e') {
                    player.interact();
                } else if (charA == ' ') {
                    gameResourceController.updateBorderCoordinate();
                    gameResourceController.getGraphic().updateViewLocation();
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

        gamePanel.addMouseListener(new MouseAdapter() {
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

        gamePanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });


        // all drivers from here

        gameResourceController.getPlayerDriver().schedule(new TimerTask() {
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
        }, 0, 1000 / 40);

        gameResourceController.getPlayerShootingDriver().schedule(new TimerTask() {
            @Override
            public void run() {
                if (openFire) {
                    player.fire(mouseX, mouseY);
                }
            }
        }, 0, 1000 / 20);


        gameResourceController.getEffectDriver().schedule(new TimerTask() {
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

        gameResourceController.getGraphicDriver().schedule(new TimerTask() {
            @Override
            public void run() {
                graphic.drawElements();
            }
        }, 0, 1000 / 40);

        gameResourceController.getCdTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                abilityCDGraphic.repaint();
            }
        }, 0, 1000 / 20);
    }


}
