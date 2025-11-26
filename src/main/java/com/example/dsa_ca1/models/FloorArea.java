package com.example.dsa_ca1.models;

public class FloorArea {
    // fields
    private String floorAreaName;
    private String floorLevel;

    private CustomLinkedList<Aisle> aisles = new CustomLinkedList<>();

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
    public void setAisles(CustomLinkedList<Aisle> aisles) {
        this.aisles = aisles;
    }

    // methods
    public void addAisle(Aisle a) {
        aisles.add(a);
    }
    public void removeAisle(Aisle a) {
        aisles.removeValue(a);
    }

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
