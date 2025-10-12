package com.example.dsa_ca1.models;

public class FloorArea {
    // fields
    private String floorAreaName = "";
    private String floorLevel = "";

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
        aisles.remove(a);
    }

    @Override
    public String toString() {
        return "FloorArea: " + floorAreaName + " (" + floorLevel + ")" +
                "\nAisles: " + aisles.size();
    }
}
