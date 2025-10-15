package com.example.dsa_ca1.models;

public class Aisle {
    private String aisleName = ""; // must be unique
    private float aisleWidth;
    private float aisleHeight;
    private String aisleTemperature = "";

    private CustomLinkedList<Shelf> shelves = new CustomLinkedList<>();

    public Aisle(String aisleName, float aisleWidth, float aisleHeight, String aisleTemperature) {
        this.aisleName = aisleName;
        this.aisleWidth = aisleWidth;
        this.aisleHeight = aisleHeight;
        this.aisleTemperature = aisleTemperature;
    }

    public String getAisleName() {
        return aisleName;
    }
    public void setAisleName(String aisleName) {
        this.aisleName = aisleName;
    }

    public float getAisleWidth() {
        return aisleWidth;
    }
    public void setAisleWidth(float aisleWidth) {
        this.aisleWidth = aisleWidth;
    }

    public float getAisleHeight() {
        return aisleHeight;
    }
    public void setAisleHeight(float aisleHeight) {
        this.aisleHeight = aisleHeight;
    }

    public String getAisleTemperature() {
        return aisleTemperature;
    }
    public void setAisleTemperature(String aisleTemperature) {
        this.aisleTemperature = aisleTemperature;
    }

    public CustomLinkedList<Shelf> getShelves() {
        return shelves;
    }
    public void setShelves(CustomLinkedList<Shelf> shelves) {
        this.shelves = shelves;
    }

    public void addShelf(Shelf s) {
        shelves.add(s);
    }

    public void removeShelf(Shelf s) {
        shelves.remove(s);
    }
}
