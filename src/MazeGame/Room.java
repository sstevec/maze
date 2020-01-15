package MazeGame;

import java.util.Random;

import static MazeGame.Info.roomSize;

public class Room {
    private Boolean openTop = false;
    private Boolean openBot = false;
    private Boolean openLeft = false;
    private Boolean openRight = false;
    private boolean visited = false;
    private int enemyNumber;

    public Room(){
        Random random = new Random();
        enemyNumber = random.nextInt(5);
    }

    public void visit(Player player){
        if(!visited){
            visited = true;
            player.sumEnemy(enemyNumber);
        }
    }

    Cell[][] cells = new Cell[roomSize][roomSize];

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
}
