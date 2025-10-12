package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    private Shelf shelf;
    private Product p1, p2;

    @BeforeEach
    void setup() {
        shelf = new Shelf(1);
        p1 = new Product("Cornflakes", 4.0f, 720f, 7, "room", "a.png");
        p2 = new Product("Weetabix", 4.5f, 720f, 16, "room", "b.png");
    }

    @Test
    void testConstructor() {
        assertEquals(1, shelf.getShelfNum());
        assertNotNull(shelf.getProducts());
    }

    @Test
    void testAddProduct() {
        assertEquals(0, shelf.getProducts().size());

        shelf.addProduct(p1);
        assertEquals(p1.toString() + "\n", shelf.getProducts().display());
        assertEquals(1, shelf.getProducts().size());

        shelf.addProduct(p2);
        assertEquals(p1.toString() + "\n" + p2.toString() + "\n", shelf.getProducts().display());
        assertEquals(2, shelf.getProducts().size());
    }

    @Test
    void testRemoveProduct() {
        shelf.addProduct(p1);
        shelf.addProduct(p2);
        assertEquals(p1.toString() + "\n" + p2.toString() + "\n", shelf.getProducts().display());

        shelf.removeProduct(p2);
        assertEquals(p1.toString() + "\n", shelf.getProducts().display());

        shelf.removeProduct(p1);
        assertEquals("", shelf.getProducts().display());
    }
}
