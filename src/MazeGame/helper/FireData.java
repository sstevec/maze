package MazeGame.helper;

import MazeGame.bullets.Bullet;

import java.util.ArrayList;

public class FireData {
    private double userX, userY, xDest, yDest, xDir, yDir, bulletSpeedFactor, bulletDamageFactor;
    private int belongTeamFactor;

    private ArrayList<Bullet> bullets;
    public FireData(double x, double y, double xDest, double yDest) {
        this.userX = x;
        this.userY = y;
        this.xDest = xDest;
        this.yDest = yDest;
        this.bulletSpeedFactor = 1.0;
        this.bulletDamageFactor = 1.0;
        this.belongTeamFactor = 1;

        double dist = Math.sqrt((xDest - x) * (xDest - x) + (yDest - y) * (yDest - y));
        this.xDir = (xDest - x) / dist;
        this.yDir = (yDest - y) / dist;
    }

    public double getUserX() {
        return userX;
    }

    public void setUserX(double userX) {
        this.userX = userX;
    }

    public double getUserY() {
        return userY;
    }

    public void setUserY(double userY) {
        this.userY = userY;
    }

    public double getxDest() {
        return xDest;
    }

    public void setxDest(double xDest) {
        this.xDest = xDest;
    }

    public double getyDest() {
        return yDest;
    }

    public void setyDest(double yDest) {
        this.yDest = yDest;
    }

    public double getxDir() {
        return xDir;
    }

    public void setxDir(double xDir) {
        this.xDir = xDir;
    }

    public double getyDir() {
        return yDir;
    }

    public void setyDir(double yDir) {
        this.yDir = yDir;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public double getBulletSpeedFactor() {
        return bulletSpeedFactor;
    }

    public void setBulletSpeedFactor(double bulletSpeedFactor) {
        this.bulletSpeedFactor = bulletSpeedFactor;
    }

    public double getBulletDamageFactor() {
        return bulletDamageFactor;
    }

    public void setBulletDamageFactor(double bulletDamageFactor) {
        this.bulletDamageFactor = bulletDamageFactor;
    }

    public int getBelongTeamFactor() {
        return belongTeamFactor;
    }

    public void setBelongTeamFactor(int belongTeamFactor) {
        this.belongTeamFactor = belongTeamFactor;
    }
}
