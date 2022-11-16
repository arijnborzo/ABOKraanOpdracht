package org.example;

public class Slot {
    private final int id;

    public org.example.Coordinate getCoordinate() {
        return Coordinate;
    }

    private Coordinate Coordinate;


    public Slot(int id, Coordinate coordinate) {
        this.id = id;
        Coordinate = coordinate;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        Slot slot = (Slot)obj;
        return this.getCoordinate().getX() == slot.getCoordinate().getX() && this.Coordinate.getY() == slot.getCoordinate().getY();
    }


}
