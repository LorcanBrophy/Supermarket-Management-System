package com.example.dsa_ca1.models;

public class Supermarket {
    // fields
    private String name = "";
    private CustomLinkedList<FloorArea> floorAreas = new CustomLinkedList<>();

    // constructor
    public Supermarket(String name) {
        this.name = name;
    }

    // getters & setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public CustomLinkedList<FloorArea> getFloorAreas() {
        return floorAreas;
    }
    public void setFloorAreas(CustomLinkedList<FloorArea> floorAreas) {
        this.floorAreas = floorAreas;
    }

    // methods
    public void addFloorArea(FloorArea fA) {
        floorAreas.add(fA);
    }

    public void removeFloorArea(FloorArea fA) {
        floorAreas.removeValue(fA);
    }
}
