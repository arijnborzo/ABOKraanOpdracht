package org.example;

public class Container {

    private int id;
    private int Lc;


    public Container(int id, int length) {
        this.id = id;
        this.Lc = length;

    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLc() {
        return Lc;
    }

    public void setLc(int lc) {
        this.Lc = lc;
    }



}