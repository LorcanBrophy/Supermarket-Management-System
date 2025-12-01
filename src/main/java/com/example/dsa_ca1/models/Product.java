package com.example.dsa_ca1.models;

public class Product {

    // fields
    private String productName;
    private float price;
    private float weight;
    private int quantity;
    private String temperature; // "Room", "Refrigerated", "Frozen"
    private String photoURL;

    // constructor
    public Product(String productName, float price, float weight, int quantity, String temperature, String photoURL) {
        this.productName = productName;
        setPrice(price);
        setWeight(weight);
        setQuantity(quantity);
        setTemperature(temperature);
        this.photoURL = photoURL;
    }

    // getters and setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        // keep price valid (no negatives)
        if (price < 0) {
            this.price = 1;
            return;
        }
        this.price = price;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        // keep weight valid (no negatives)
        if (weight < 0) {
            this.weight = 1;
            return;
        }
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        // keep quantity valid (no negatives)
        if (quantity < 0) {
            this.quantity = 1;
            return;
        }
        this.quantity = quantity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        // only allow preset temps
        if (temperature.equals("Room") || temperature.equals("Refrigerated") || temperature.equals("Frozen")) {
            this.temperature = temperature;
        } else {
            this.temperature = "Room";
        }
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    // helper methods

    // increase or decrease quantity
    public void updateQuantity(int amount) {
        this.quantity += amount;
    }

    // calculates totalValue of a product
    public float totalValue() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return "Product: " + productName +
                " | Price: €" + String.format("%.2f", price) +
                " | Weight: " + weight + "g" +
                " | Quantity: " + quantity +
                " | Temp: " + temperature +
                " | Photo: " + photoURL +
                " | Total Value: €" + String.format("%.2f", totalValue());
    }
}
