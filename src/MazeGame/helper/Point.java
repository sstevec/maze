package MazeGame.helper;

public class Point {
    private boolean isWall = false;
    private boolean isBound = false;
    private boolean isOpen = false;
    private boolean isClosed = false;

    private int parentI = -1;
    private int parentJ = -1;
    private double estimateWeight = -1;
    private int pathWeight = -1;



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



    public void clear(){
        if(!isBound){
            isOpen = false;
            isClosed = false;
            parentI = -1;
            parentJ = -1;
            estimateWeight = -1;
            pathWeight = -1;
        }
    }
}

