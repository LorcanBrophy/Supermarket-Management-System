package com.example.dsa_ca1.models;

public class FloorArea {
    // fields
    private String floorAreaName;
    private String floorLevel;

    private final CustomLinkedList<Aisle> aisles = new CustomLinkedList<>();

    // constructor
    public FloorArea(String floorAreaName, String floorLevel) {
        this.floorAreaName = floorAreaName;
        this.floorLevel = floorLevel;
    }

    // getters & setters
    public String getFloorAreaName() {
        return floorAreaName;
    }
    public void setFloorAreaName(String floorAreaName) {
        this.floorAreaName = floorAreaName;
    }

    public String getFloorLevel() {
        return floorLevel;
    }
    public void setFloorLevel(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public CustomLinkedList<Aisle> getAisles() {
        return aisles;
    }

    // methods

    // adds an aisle to the floorArea
    public void addAisle(Aisle aisle) {
        aisles.linkedListAdd(aisle);
    }

    // removes an aisle from the floorArea
    public void removeAisle(Aisle aisle) {
        aisles.linkedListRemove(aisle);
    }

    // calculates total value of floorArea
    public float totalValue() {
        float total = 0;
        for (Aisle aisle : aisles) {
            total += aisle.totalValue();
        }
        return total;
    }

    @Override
    public String toString() {
        return "FloorArea: " + floorAreaName +
                " (" + floorLevel + ") | Total Aisles: " + aisles.size();
    }
}
