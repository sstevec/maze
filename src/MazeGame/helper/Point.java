package MazeGame.helper;

public class Point {
    private boolean isWall = false;
    private boolean isBound = false;
    private boolean isOpen = false;
    private boolean isClosed = false;
    private boolean isStart = false;
    private boolean isEnd = false;
    private int parentI = -1;
    private int parentJ = -1;
    private double estimateWeight = -1;
    private int pathWeight = -1;
    private boolean isWay = false;
    private boolean isInWallList = false;
    private int comeDir = -1;

    public int getComeDir() {
        return comeDir;
    }

    public void setComeDir(int comeDir) {
        this.comeDir = comeDir;
    }

    public boolean isInWallList() {
        return isInWallList;
    }

    public void setInWallList(boolean inWallList) {
        isInWallList = inWallList;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        if(!isBound){
            isWall = wall;
        }
    }

    public boolean isBound() {
        return isBound;
    }

    public void setBound(boolean bound) {
        isBound = bound;
        isWall = bound;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
        isClosed = !open;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
        isOpen = !closed;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public int getParentI() {
        return parentI;
    }

    public void setParentI(int parentI) {
        this.parentI = parentI;
    }

    public int getParentJ() {
        return parentJ;
    }

    public void setParentJ(int parentJ) {
        this.parentJ = parentJ;
    }

    public double getEstimateWeight() {
        return estimateWeight;
    }

    public void setEstimateWeight(double estimateWeight) {
        this.estimateWeight = estimateWeight;
    }

    public int getPathWeight() {
        return pathWeight;
    }

    public void setPathWeight(int pathWeight) {
        this.pathWeight = pathWeight;
    }

    public boolean isWay() {
        return isWay;
    }

    public void setWay(boolean way) {
        isWay = way;
    }

    public void clear(){
        if(!isBound){
            isOpen = false;
            isClosed = false;
            isWay = false;
            parentI = -1;
            parentJ = -1;
            estimateWeight = -1;
            pathWeight = -1;
        }
    }
}

