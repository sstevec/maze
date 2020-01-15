package MazeGame;

import java.awt.*;

public class Bullet {

    private double x;
    private double y;
    private double xDir;
    private double yDir;
    private int speed;
    private int damage;
    private Color color;
    private int belongTeam;

    public Bullet(int x, int y, double xDir, double yDir, int speed, Color color, int damage, int belongTeam){
        this.x = x;
        this.y = y;
        this.xDir = xDir;
        this.yDir = yDir;
        this.speed = speed;
        this.color = color;
        this.damage = damage;
        this.belongTeam = belongTeam;
    }

    public void fly(){
        x = x + xDir*speed;
        y = y + yDir*speed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public int getDamage() {
        return damage;
    }

    public int getBelongTeam() {
        return belongTeam;
    }
}
