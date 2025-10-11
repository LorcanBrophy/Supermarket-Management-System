package com.example.dsa_ca1.main;

import com.example.dsa_ca1.models.Product;

public class ProductTest {
    public static void main(String[] args) {
        Product p1 = new Product("Cornflakes", 3.99, 720, 7, "room", "bread.jpg");

        System.out.println(p1);

        p1.updatePrice(4.49);
        p1.updatePhoto("cornflakes.jpg");

        p1.updatePrice(4.49);
        p1.updatePhoto("milk.jpg");

        System.out.println("Total value: €" + p1.getTotalValue());

        p1.updateQuantity(16); // note: currently private; can make public for testing or use addQuantity
        System.out.println("After adding 16 units: " + p1);
    }
}
