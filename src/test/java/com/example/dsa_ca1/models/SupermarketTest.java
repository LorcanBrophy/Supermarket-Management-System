package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SupermarketTest {

    private Supermarket supermarket;
    private FloorArea floorArea1, floorArea2;

    @BeforeEach
    void setup() {
        supermarket = new Supermarket("Tesco", 2);

        floorArea1 = new FloorArea("Fruit and Veg", "Ground Floor");
        floorArea2 = new FloorArea("Dairy", "First Floor");
    }

    @Test
    void testConstructor() {
        assertEquals("Tesco", supermarket.getName());
        assertNotNull(supermarket.getFloorAreas());
    }

    @Test
    void testAddFloorArea() {
        supermarket.addFloorArea(floorArea1);
        assertEquals(1, supermarket.getFloorAreas().size());

        supermarket.addFloorArea(floorArea2);
        assertEquals(2, supermarket.getFloorAreas().size());

        assertEquals(floorArea1.toString() + "\n" + floorArea2.toString() + "\n", supermarket.getFloorAreas().display());
    }

    @Test
    void testRemoveFloorArea() {
        supermarket.addFloorArea(floorArea1);
        supermarket.addFloorArea(floorArea2);

        assertEquals(2, supermarket.getFloorAreas().size());

        supermarket.removeFloorArea(floorArea1);
        assertEquals(1, supermarket.getFloorAreas().size());

        supermarket.removeFloorArea(floorArea2);
        assertEquals(0, supermarket.getFloorAreas().size());
    }
}
