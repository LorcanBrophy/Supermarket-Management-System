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
        p1 = new Product("Cornflakes", 4.0f, 720f, 7, "Room", "a.png");
        p2 = new Product("Weetabix", 4.5f, 720f, 16, "Room", "b.png");
    }

    @Test
    void testConstructor() {
        assertEquals(1, shelf.getShelfNum());
        assertNotNull(shelf.getProducts());
        assertEquals(0, shelf.getProducts().size());
    }

    @Test
    void testGetShelfNum() {
        assertEquals(1, shelf.getShelfNum());
    }

    @Test
    void testGetProducts() {
        Shelf shelf = new Shelf(3);

        assertNotNull(shelf.getProducts());
        assertEquals(0, shelf.getProducts().size());

        shelf.addProduct(p1);

        assertEquals(1, shelf.getProducts().size());
        assertEquals(p1.toString() + "\n", shelf.getProducts().display());
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

    @Test
    void testTotalValue() {
        assertEquals(0, shelf.totalValue());

        shelf.addProduct(p1);
        assertEquals(28.0f, shelf.totalValue());

        shelf.addProduct(p2);
        assertEquals(100.0f, shelf.totalValue());
    }

    @Test
    void testToString() {
        String s = shelf.toString();

        assertTrue(s.contains("Shelf " + shelf.getShelfNum()));
        assertTrue(s.contains("Total Products: " + shelf.getProducts().size()));

        assertFalse(s.contains("Shelf " + 4));
        assertFalse(s.contains("Total Products: " + 100));
    }
}
