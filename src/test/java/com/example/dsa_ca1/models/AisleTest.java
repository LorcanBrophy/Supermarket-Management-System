package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AisleTest {

    private Aisle aisle;
    private Shelf shelf1, shelf2;

    @BeforeEach
    void setup() {
        aisle = new Aisle("Cereal", 10, 3, "Room");

        shelf1 = new Shelf(1);
        shelf2 = new Shelf(2);

        Product p1 = new Product("Cornflakes", 4.0f, 720f, 7, "Room", "a.png");
        Product p2 = new Product("Weetabix", 4.5f, 720f, 16, "Room", "b.png");
        Product p3 = new Product("Porridge", 3f, 950f, 16, "Room", "c.png");

        shelf1.addProduct(p1);
        shelf2.addProduct(p2);
        shelf2.addProduct(p3);
    }

    @Test
    void testConstructor() {
        assertEquals("Cereal", aisle.getAisleName());
        assertEquals(10, aisle.getAisleWidth());
        assertEquals(3, aisle.getAisleHeight());
        assertEquals("Room", aisle.getAisleTemperature());
        
        assertNotNull(aisle.getShelves());
    }
    
    @Test
    void testAddShelf() {
        assertEquals(0, aisle.getShelves().size());

        aisle.addShelf(shelf1);
        assertEquals(1, aisle.getShelves().size());

        aisle.addShelf(shelf2);
        assertEquals(2, aisle.getShelves().size());

        assertEquals(shelf1.toString() + "\n" + shelf2.toString() + "\n", aisle.getShelves().display());
    }

    @Test
    void testRemoveShelf() {
        aisle.addShelf(shelf1);
        aisle.addShelf(shelf2);
        assertEquals(shelf1.toString() + "\n" + shelf2.toString() + "\n", aisle.getShelves().display());

        aisle.removeShelf(shelf2);
        assertEquals(shelf1.toString() + "\n", aisle.getShelves().display());

        aisle.removeShelf(shelf1);
        assertEquals("", aisle.getShelves().display());
    }

}
