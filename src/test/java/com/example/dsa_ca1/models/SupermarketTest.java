package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SupermarketTest {

    private Supermarket supermarket;
    private FloorArea floorArea1, floorArea2;
    private Aisle aisle1, aisle2;
    private Shelf shelf1, shelf2;
    private Product p1, p2, p3;

    @BeforeEach
    void setup() {
        supermarket = new Supermarket("Lidl", 2);

        floorArea1 = new FloorArea("Fruit and Veg", "Ground Floor");
        floorArea2 = new FloorArea("Dairy", "First Floor");

        aisle1 = new Aisle("Fruit", 1, 1, "Room");
        aisle2 = new Aisle("Milk", 1, 1, "Room");

        shelf1 = new Shelf(1);
        shelf2 = new Shelf(2);

        p1 = new Product("Apples", 2.0f, 500f, 5, "Room", "a.png");
        p2 = new Product("Milk", 1.5f, 1000f, 4, "Refrigerated", "b.png");
        p3 = new Product("Cheese", 3.0f, 300f, 2, "Refrigerated", "c.png");
    }

    @Test
    void testConstructor() {
        assertEquals("Lidl", supermarket.getName());
        assertNotNull(supermarket.getFloorAreas());
    }

    @Test
    void testGetNumFloors() {
        assertEquals(2, supermarket.getNumFloors());
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

    @Test
    void testTotalValue() {
        shelf1.addProduct(p1);
        shelf2.addProduct(p2);
        shelf2.addProduct(p3);

        aisle1.addShelf(shelf1);
        aisle2.addShelf(shelf2);

        floorArea1.addAisle(aisle1);
        floorArea2.addAisle(aisle2);

        supermarket.addFloorArea(floorArea1);
        supermarket.addFloorArea(floorArea2);

        assertEquals(22f, supermarket.totalValue());
    }

    @Test
    void testSaveAndLoad() throws Exception {
        shelf1.addProduct(p1);
        shelf2.addProduct(p2);
        shelf2.addProduct(p3);

        aisle1.addShelf(shelf1);
        aisle2.addShelf(shelf2);

        floorArea1.addAisle(aisle1);
        floorArea2.addAisle(aisle2);

        supermarket.addFloorArea(floorArea1);
        supermarket.addFloorArea(floorArea2);

        String fileName = "testSupermarket";

        supermarket.save(fileName);

        Supermarket loaded = Supermarket.load(fileName);

        assertNotNull(loaded);
        assertEquals("Lidl", loaded.getName());
        assertEquals(2, loaded.getNumFloors());
        assertEquals(2, loaded.getFloorAreas().size());
        assertEquals(supermarket.totalValue(), loaded.totalValue());

        java.io.File f = new java.io.File(fileName + ".xml");
        if (f.exists()) {
            boolean deleted = f.delete();
            assertTrue(deleted);
        }
    }

    @Test
    void testToString() {
        String s = supermarket.toString();

        assertTrue(s.contains("Lidl"));
        assertTrue(s.contains("2 Floors"));
        assertTrue(s.contains("Total Floor Areas: 0"));

        supermarket.addFloorArea(new FloorArea("Test", "Floor"));
        s = supermarket.toString();

        assertTrue(s.contains("Total Floor Areas: 1"));
    }
}
