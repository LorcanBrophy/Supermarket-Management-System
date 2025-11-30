package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product("Cornflakes", 4, 720, 1, "Room", "apple.png");
    }

    @Test
    void testConstructor() {
        assertEquals("Cornflakes", product.getProductName());
        assertEquals(4, product.getPrice());
        assertEquals(720, product.getWeight());
        assertEquals(1, product.getQuantity());
        assertEquals("Room", product.getTemperature());
        assertEquals("apple.png", product.getPhotoURL());

        assertNotNull(product);
    }

    @Test
    void testSetters() {
        product.setProductName("Frosties");
        product.setPrice(5);
        product.setWeight(850);
        product.setQuantity(2);
        product.setTemperature("Room");
        product.setPhotoURL("frosties.png");

        assertEquals("Frosties", product.getProductName());
        assertEquals(5, product.getPrice());
        assertEquals(850, product.getWeight());
        assertEquals(2, product.getQuantity());
        assertEquals("Room", product.getTemperature());
        assertEquals("frosties.png", product.getPhotoURL());
    }

    @Test
    void testNegativeValues() {
        product.setPrice(-50);
        product.setWeight(-10);
        product.setQuantity(-5);

        assertEquals(1, product.getPrice());
        assertEquals(1, product.getWeight());
        assertEquals(1, product.getQuantity());
    }

    @Test
    void testTemperatures() {
        product.setTemperature("Frozen");
        assertEquals("Frozen", product.getTemperature());

        product.setTemperature("Refrigerated");
        assertEquals("Refrigerated", product.getTemperature());

        product.setTemperature("Room");
        assertEquals("Room", product.getTemperature());
    }

    @Test
    void testInvalidTemperature() {
        product.setTemperature("SuperCold");
        assertEquals("Room", product.getTemperature());

        product.setTemperature("hot");
        assertEquals("Room", product.getTemperature());

        product.setTemperature("325");
        assertEquals("Room", product.getTemperature());
    }

    @Test
    void testUpdateQuantity() {
        product.updateQuantity(0);
        assertEquals(1, product.getQuantity());

        product.updateQuantity(3);
        assertEquals(4, product.getQuantity());

        product.updateQuantity(-2);
        assertEquals(2, product.getQuantity());
    }

    @Test
    void testTotalValue() {
        assertEquals(4, product.totalValue());

        product.setPrice(6);
        assertEquals(6, product.getPrice());

        product.updateQuantity(3);
        assertEquals(24, product.totalValue());
    }

    @Test
    void testToString() {
        String s = product.toString();

        assertTrue(s.contains("Cornflakes"));
        assertFalse(s.contains("Weetabix"));

        assertTrue(s.contains("Room"));
        assertFalse(s.contains("Freezer"));
    }

}
