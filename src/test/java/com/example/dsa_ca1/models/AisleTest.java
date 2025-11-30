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
    void testAisleName() {
        aisle.setAisleName("Dairy");
        assertEquals("Dairy", aisle.getAisleName());
    }

    @Test
    void testAisleWidth() {
        aisle.setAisleWidth(-5);
        assertEquals(1, aisle.getAisleWidth());

        aisle.setAisleWidth(4.5f);
        assertEquals(4.5f, aisle.getAisleWidth());
    }

    @Test
    void testAisleHeight() {
        aisle.setAisleHeight(-10);
        assertEquals(1, aisle.getAisleHeight());

        aisle.setAisleHeight(7.0f);
        assertEquals(7.0f, aisle.getAisleHeight());
    }

    @Test
    void testAisleTemperature() {
        aisle.setAisleTemperature("Room");
        assertEquals("Room", aisle.getAisleTemperature());

        aisle.setAisleTemperature("Refrigerated");
        assertEquals("Refrigerated", aisle.getAisleTemperature());

        aisle.setAisleTemperature("Frozen");
        assertEquals("Frozen", aisle.getAisleTemperature());
    }

    @Test
    void testInvalidAisleTemperature() {
        aisle.setAisleTemperature("SuperCold");
        assertEquals("Room", aisle.getAisleTemperature());

        aisle.setAisleTemperature("hot");
        assertEquals("Room", aisle.getAisleTemperature());

        aisle.setAisleTemperature("325");
        assertEquals("Room", aisle.getAisleTemperature());
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

    @Test
    void testTotalValue() {
        aisle.addShelf(shelf1);
        aisle.addShelf(shelf2);

        float expected = shelf1.totalValue() + shelf2.totalValue();

        assertEquals(expected, aisle.totalValue());
    }

    @Test
    void testToString() {
        String s = aisle.toString();

        assertTrue(s.contains("Cereal"));
        assertFalse(s.contains("Drinks"));

        assertTrue(s.contains("W: 10"));
        assertTrue(s.contains("H: 3"));

        assertTrue(s.contains("Room"));
        assertFalse(s.contains("Cold"));

        assertTrue(s.contains("Total Shelves: 0"));

        aisle.addShelf(new Shelf(1));
        String updated = aisle.toString();
        assertTrue(updated.contains("Total Shelves: 1"));
    }

}
