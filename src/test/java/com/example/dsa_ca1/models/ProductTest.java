package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product("Cornflakes", 4, 720, 1, "room", "apple.png");
    }

    @Test
    void testConstructor() {
        assertEquals("Cornflakes", product.getProductName());
        assertEquals(4, product.getPrice());
        assertEquals(720, product.getWeight());
        assertEquals(1, product.getQuantity());
        assertEquals("room", product.getTemperature());
        assertEquals("apple.png", product.getPhotoURL());

        assertNotNull(product);
    }

    @Test
    void testSetters() {
        product.setProductName("Frosties");
        product.setPrice(5);
        product.setWeight(850);
        product.setQuantity(2);
        product.setTemperature("cold");
        product.setPhotoURL("frosties.png");

        assertEquals("Frosties", product.getProductName());
        assertEquals(5, product.getPrice());
        assertEquals(850, product.getWeight());
        assertEquals(2, product.getQuantity());
        assertEquals("cold", product.getTemperature());
        assertEquals("frosties.png", product.getPhotoURL());
    }

    @Test
    void testUpdateQuantity() {
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
        assertTrue(product.toString().contains("Cornflakes"));
        assertFalse(product.toString().contains("Weetabix"));

        assertTrue(product.toString().contains("room"));
        assertFalse(product.toString().contains("freezer"));
    }

}
