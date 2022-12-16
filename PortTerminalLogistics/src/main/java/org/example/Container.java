package org.example;

public class Container {

    private int id;
    private int Lc;
    private int slotId;
    private int slotH;

    public Container(int id, int length, int slotId, int slotH) {
        this.id = id;
        this.Lc = length;
        this.slotId = slotId;
        this.slotH = slotH;
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

    public int getSlotId() { return slotId; }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }
    public void setLc(int lc) {
        this.Lc = lc;
    }

    public int getSlotH() {
        return slotH;
    }

    public void setSlotH(int slotH) {
        this.slotH = slotH;
    }

}