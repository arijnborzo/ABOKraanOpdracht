package org.example;

public class Cranes {

    private int craneId;
    private Coordinate positie;
    private int yMin;
    private int yMax;
    private int xMin;
    private int xMax;

    private int xSpeed;
    private int ySpeed;

    public Cranes(int craneId, Coordinate positie, int yMin, int yMax, int xMin, int xMax, int xSpeed, int ySpeed) {
        this.craneId = craneId;
        this.positie = positie;
        this.yMin = yMin;
        this.yMax = yMax;
        this.xMin = xMin;
        this.xMax = xMax;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }


    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }
    public int getCraneId() {
        return craneId;
    }

    public void setCraneId(int craneId) {
        this.craneId = craneId;
    }

    public Coordinate getPositie() {
        return positie;
    }

    public void setPositie(Coordinate positie) {
        this.positie = positie;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

}
