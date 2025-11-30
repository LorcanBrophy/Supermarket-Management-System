package com.example.dsa_ca1.models;

public class Aisle {

    // fields
    private String aisleName; // must be unique
    private float aisleWidth;
    private float aisleHeight;
    private String aisleTemperature; // Room, Refrigerated, Frozen

    private final CustomLinkedList<Shelf> shelves = new CustomLinkedList<>();

    // constructor
    public Aisle(String aisleName, float aisleWidth, float aisleHeight, String aisleTemperature) {
        this.aisleName = aisleName;
        setAisleWidth(aisleWidth);
        setAisleHeight(aisleHeight);
        setAisleTemperature(aisleTemperature);
    }

    // getters and setters
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
        // keep width valid (no negatives)
        if (aisleWidth <= 0) {
            this.aisleWidth = 1;
            return;
        }
        this.aisleWidth = aisleWidth;
    }

    public float getAisleHeight() {
        return aisleHeight;
    }
    public void setAisleHeight(float aisleHeight) {
        // keep height valid (no negatives)
        if (aisleHeight <= 0) {
            this.aisleHeight = 1;
            return;
        }
        this.aisleHeight = aisleHeight;
    }

    public String getAisleTemperature() {
        return aisleTemperature;
    }
    public void setAisleTemperature(String aisleTemperature) {
        // only allow preset temps
        if (aisleTemperature.equals("Room") || aisleTemperature.equals("Refrigerated") || aisleTemperature.equals("Frozen")) {
            this.aisleTemperature = aisleTemperature;
        } else {
            this.aisleTemperature = "Room";
        }
    }

    public CustomLinkedList<Shelf> getShelves() {
        return shelves;
    }

    // methods

    // adds a shelf to the aisle
    public void addShelf(Shelf shelf) {
        shelves.linkedListAdd(shelf);
    }

    // removes a shelf from the aisle
    public void removeShelf(Shelf shelf) {
        shelves.linkedListRemove(shelf);
    }

    // calculates totalValue of the aisle
    public float totalValue() {
        float total = 0;
        for (Shelf shelf : shelves) {
            total += shelf.totalValue();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Aisle: " + aisleName +
                " | W: " + aisleWidth +
                "  H: " + aisleHeight +
                "  Temp: " + aisleTemperature +
                " | Total Shelves: " + shelves.size();
    }
}
