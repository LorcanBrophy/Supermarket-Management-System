package com.example.dsa_ca1.models;

public class Product {

    // fields
    private String name;
    private float price;
    private float weight;
    private int quantity;
    private String temperature; // "room", "fridge", "freezer"
    private String photoURL;

    // constructor
    public Product(String name, float price, float weight, int quantity, String temperature, String photoURL) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.quantity = quantity;
        this.temperature = temperature;
        this.photoURL = photoURL;
    }

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public String getPhotoURL() { return photoURL; }
    public void setPhotoURL(String photoURL) { this.photoURL = photoURL; }

    // helper methods
    public void updateQuantity(int amount) {
        this.quantity += amount;
    }
    public float totalValue() {
        return price * quantity;
    }

    // toString
    @Override
    public String toString() {
        return "Product: " + name +
                " | Price: €" + String.format("%.2f", price) +
                " | Weight: " + weight + "g" +
                " | Quantity: " + quantity +
                " | Temp: " + temperature +
                " | Photo: " + photoURL;
    }
}
