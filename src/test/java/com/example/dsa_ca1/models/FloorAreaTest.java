package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FloorAreaTest {

    private FloorArea floorArea;
    private Aisle aisle1, aisle2;

    @BeforeEach
    void setup() {
        floorArea = new FloorArea("Fruit and Veg", "Floor 1");
        aisle1 = new Aisle("Apples", 6, 2, "Room");
        aisle2 = new Aisle("Bananas", 2, 2, "Room");
    }

    @Test
    void testConstructor() {
        assertEquals("Fruit and Veg", floorArea.getFloorAreaName());
        assertEquals("Floor 1", floorArea.getFloorLevel());
        assertNotNull(floorArea.getAisles());
    }

    @Test
    void testSetFloorAreaName() {
        floorArea.setFloorAreaName("Dairy");
        assertEquals("Dairy", floorArea.getFloorAreaName());
    }

    @Test
    void testSetFloorAreaLevel() {
        floorArea.setFloorLevel("Floor 2");
        assertEquals("Floor 2", floorArea.getFloorLevel());
    }

    @Test
    void testAddAisle() {
        floorArea.addAisle(aisle1);
        assertEquals(1, floorArea.getAisles().size());

        floorArea.addAisle(aisle2);
        assertEquals(2, floorArea.getAisles().size());

        assertEquals(aisle1.toString() + "\n" + aisle2.toString() + "\n", floorArea.getAisles().display());
    }

    @Test
    void testRemoveAisle() {
        floorArea.addAisle(aisle1);
        floorArea.addAisle(aisle2);

        assertEquals(2, floorArea.getAisles().size());

        floorArea.removeAisle(aisle1);
        assertEquals(1, floorArea.getAisles().size());

        floorArea.removeAisle(aisle2);
        assertEquals(0, floorArea.getAisles().size());
    }

    @Test
    void testTotalValue() {
        floorArea.addAisle(aisle1);
        floorArea.addAisle(aisle2);

        float expected = aisle1.totalValue() + aisle2.totalValue();

        assertEquals(expected, floorArea.totalValue());
    }

    @Test
    void testToString() {
        String s = floorArea.toString();

        assertTrue(s.contains(floorArea.getFloorAreaName()));
        assertTrue(s.contains(floorArea.getFloorLevel()));
        assertTrue(s.contains("Total Aisles: " + floorArea.getAisles().size()));
    }
}
