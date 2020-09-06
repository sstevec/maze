package MazeGame;

import MazeGame.Interactions.Portal;

import java.util.Random;

import static MazeGame.Info.roomSize;

public class Room {
    private Boolean openTop = false;
    private Boolean openBot = false;
    private Boolean openLeft = false;
    private Boolean openRight = false;
    private boolean visited = false;
    private int enemyNumber;
    Cell[][] cells = new Cell[roomSize][roomSize];

    public Room(){
        Random random = new Random();
        enemyNumber = random.nextInt(5);
    }

    public boolean visit(){
        if(!visited){
            visited = true;
            return true;
        }else {
            return false;
        }
    }

    public void setPortal(){
        int startCor = (roomSize-3)/2;
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                cells[i+startCor][j+startCor].setIntractable(new Portal());
            }
        }
    }


    public Boolean getOpenTop() {
        return openTop;
    }

    public void setOpenTop(Boolean openTop) {
        this.openTop = openTop;
    }

    public Boolean getOpenBot() {
        return openBot;
    }

    public void setOpenBot(Boolean openBot) {
        this.openBot = openBot;
    }

    public Boolean getOpenLeft() {
        return openLeft;
    }

    public void setOpenLeft(Boolean openLeft) {
        this.openLeft = openLeft;
    }

    public Boolean getOpenRight() {
        return openRight;
    }

    public void setOpenRight(Boolean openRight) {
        this.openRight = openRight;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getEnemyNumber() {
        return enemyNumber;
    }
}
