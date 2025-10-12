package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FloorAreaTest {

    private FloorArea floorArea;
    private Aisle aisle1, aisle2;

    @BeforeEach
    void setup() {
        floorArea = new FloorArea("Fruit and Veg", "Ground Floor");
        aisle1 = new Aisle("Apples", 6, 2, "Room");
        aisle2 = new Aisle("Bananas", 2, 2, "Room");
    }

    @Test
    void testConstructor() {
        assertEquals("Fruit and Veg", floorArea.getFloorAreaName());
        assertEquals("Ground Floor", floorArea.getFloorLevel());
        assertNotNull(floorArea.getAisles());
    }

    @Test
    void testAddAisle() {
        floorArea.addAisle(aisle1);
        assertEquals(1, floorArea.getAisles().size());

        floorArea.addAisle(aisle2);
        assertEquals(2, floorArea.getAisles().size());
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
    void testToString() {
        floorArea.addAisle(aisle1);
        String result = floorArea.toString();
        assertTrue(result.contains("Fruit and Veg"));
        assertTrue(result.contains("Ground Floor"));
        assertTrue(result.contains("Aisles: 1"));
    }
}
