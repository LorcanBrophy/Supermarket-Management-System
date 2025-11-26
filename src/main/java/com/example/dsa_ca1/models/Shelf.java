package com.example.dsa_ca1.models;

public class Shelf {
    private int shelfNum;

    private CustomLinkedList<Product> products = new CustomLinkedList<>();

    public Shelf(int shelfNum) {
        this.shelfNum = shelfNum;
    }

    public int getShelfNum() {
        return shelfNum;
    }
    public void setShelfNum(int shelfNum) {
        this.shelfNum = shelfNum;
    }

    public CustomLinkedList<Product> getProducts() {
        return products;
    }
    public void setProducts(CustomLinkedList<Product> products) {
        this.products = products;
    }

    public void addProduct(Product p) {
        products.add(p);
    }
    public void removeProduct(Product p) {
        products.removeValue(p);
    }

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
