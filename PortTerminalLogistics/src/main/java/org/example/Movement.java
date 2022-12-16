package org.example;

import java.util.HashMap;

public class Movement {
    private double startTime;
    private double endTime;
    private Coordinate startLocation;
    private Coordinate endLocation;
    private double xSpeed;
    private double ySpeed;

    private int containerId;

    private int vanSlotId;
    private int naarSlotId;
    private int id; //crane id

    public Movement(int id,int containerId,int vanSlotId,int naarSlotId,int t, Coordinate p1 , Coordinate p2 , double vx , double vy)
    {
        this.id = id;
        this.containerId = containerId;
        this.vanSlotId = vanSlotId;
        this.naarSlotId = naarSlotId;
        startTime = t;
        startLocation = p1;
        endLocation = p2;

        xSpeed = vx;
        ySpeed = vy;

        endTime = t + getTravelTime();

    }

    public double getTravelTime()
    {
        double xTijd = Math.abs(endLocation.getX() - startLocation.getX()) / xSpeed ;
        double yTijd = Math.abs(endLocation.getY() - startLocation.getY()) / ySpeed ;

        if(xTijd > yTijd) return xTijd;
        else return yTijd ;
    }

    public boolean collision(Movement k1, Movement k2) {
        return (Math.min(k1.startLocation.getX(),k1.endLocation.getX()) <= Math.max(k2.startLocation.getX(),k2.endLocation.getX())
                && Math.min(k2.endLocation.getX(),k2.startLocation.getX()) <= Math.max(k1.startLocation.getX(),k1.endLocation.getX()));
    }

    public int[] getOverlapArea(Movement m1, Movement m2) {

        int minmax= Math.min(Math.max(m1.startLocation.getX(),m1.endLocation.getX()),Math.max(m2.startLocation.getX(),m2.endLocation.getX()));
        int maxmin= Math.max(Math.min(m1.startLocation.getX(),m1.endLocation.getX()),Math.min(m2.startLocation.getX(),m2.endLocation.getX()));

        if (minmax < maxmin)
            return null;
        else
            return new int[]{maxmin, minmax};
    }
    public int getContainerId() {
        return containerId;
    }
    public int getId() {
        return id;
    }
    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public Coordinate getStartLocation() {
        return startLocation;
    }

    public Coordinate getEndLocation() {
        return endLocation;
    }

}
