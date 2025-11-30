package com.example.dsa_ca1.models;

public class Shelf {

    // fields
    private final int shelfNum;

    private final CustomLinkedList<Product> products = new CustomLinkedList<>();

    // constructor
    public Shelf(int shelfNum) {
        this.shelfNum = shelfNum;
    }

    // getters
    public int getShelfNum() {
        return shelfNum;
    }
    public CustomLinkedList<Product> getProducts() {
        return products;
    }

    // methods

    // adds a product to the shelf
    public void addProduct(Product product) {
        products.linkedListAdd(product);
    }

    // removes a product from a shelf
    public void removeProduct(Product product) {
        products.linkedListRemove(product);
    }

    // calculates the total value of all products on this shelf
    public float totalValue() {
        float total = 0;
        for (Product product : products) {
            total += product.totalValue();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Shelf " + shelfNum +
                " | Total Products: " + products.size();
    }
}
