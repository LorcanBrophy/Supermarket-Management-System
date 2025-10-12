package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product1;

    @BeforeEach
    void setup() {
        product1 = new Product("Cornflakes", 4, 720, 1, "room", "apple.png");
    }

    @Test
    void testConstructor() {
        assertEquals("Cornflakes", product1.getproductName());
        assertEquals(4, product1.getPrice());
        assertEquals(720, product1.getWeight());
        assertEquals(1, product1.getQuantity());
        assertEquals("room", product1.getTemperature());
        assertEquals("apple.png", product1.getPhotoURL());
    }

    @Test
    void testSetters() {
        product1.setproductName("Frosties");
        product1.setPrice(5);
        product1.setWeight(850);
        product1.setQuantity(2);
        product1.setTemperature("cold");
        product1.setPhotoURL("frosties.png");

        assertEquals("Frosties", product1.getproductName());
        assertEquals(5, product1.getPrice());
        assertEquals(850, product1.getWeight());
        assertEquals(2, product1.getQuantity());
        assertEquals("cold", product1.getTemperature());
        assertEquals("frosties.png", product1.getPhotoURL());
    }

    @Test
    void testUpdateQuantity() {
        product1.updateQuantity(3);
        assertEquals(4, product1.getQuantity());

        product1.updateQuantity(-2);
        assertEquals(2, product1.getQuantity());
    }

    @Test
    void testTotalValue() {
        assertEquals(4, product1.totalValue());

        product1.setPrice(6);
        assertEquals(6, product1.getPrice());

        product1.updateQuantity(3);
        assertEquals(24, product1.totalValue());
    }

    @Test
    void testToString() {
        assertTrue(product1.toString().contains("Cornflakes"));
        assertFalse(product1.toString().contains("Weetabix"));

        assertTrue(product1.toString().contains("room"));
        assertFalse(product1.toString().contains("freezer"));
    }

}
