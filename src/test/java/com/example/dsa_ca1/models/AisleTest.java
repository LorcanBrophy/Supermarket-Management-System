package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AisleTest {

    private Aisle aisle;
    private Shelf shelf1, shelf2;
    private Product p1, p2, p3;

    @BeforeEach
    void setup() {
        aisle = new Aisle("Cereal", 10, 3, "room");

        shelf1 = new Shelf(1);
        shelf2 = new Shelf(2);

        p1 = new Product("Cornflakes", 4.0f, 720f, 7, "room", "a.png");
        p2 = new Product("Weetabix", 4.5f, 720f, 16, "room", "b.png");
        p3 = new Product("Porridge", 3f, 950f, 16, "room", "c.png");

        shelf1.addProduct(p1);
        shelf2.addProduct(p2);
        shelf2.addProduct(p3);
    }
    @Test
    void testAddShelf() {
        aisle.addShelf(shelf1);
        aisle.addShelf(shelf2);

        assertEquals(shelf1.toString() + "\n" + shelf2.toString() + "\n" + "\n", aisle.getShelves().display());
    }

}
