package com.example.dsa_ca1.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListTest {

    private CustomLinkedList<Integer> list;

    @BeforeEach
    void setup() {
        list = new CustomLinkedList<>();
    }

    @Test
    void testAddAndSize() {
        assertEquals(0, list.size());

        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(3, list.size());
    }

    @Test
    void testRemoveValue() {
        list.add(1);
        list.add(2);
        list.add(3);

        assertEquals(3, list.size());

        list.removeValue(2);
        assertEquals(2, list.size());
        assertFalse(list.display().contains("2"));

        list.removeValue(1);
        assertEquals(1, list.size());
        assertFalse(list.display().contains("1"));

        list.removeValue(3);
        assertEquals(0, list.size());
        assertEquals("", list.display());

        list.removeValue(100);
        assertEquals(0, list.size());
    }

    @Test
    void testClear() {
        list.add(5);
        list.add(10);
        list.add(15);

        assertEquals(3, list.size());

        list.clear();
        assertEquals(0, list.size());
        assertEquals("", list.display());
    }

    @Test
    void testDisplay() {
        list.add(7);
        list.add(14);
        list.add(21);

        String expected = "7\n14\n21\n";
        assertEquals(expected, list.display());
    }

    @Test
    void testIterator() {
        list.add(100);
        list.add(200);
        list.add(300);

        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        assertEquals(600, sum);
    }

    @Test
    void testRemoveNonExistentValue() {
        list.add(1);
        list.add(2);
        list.add(3);

        list.removeValue(999);
        assertEquals(3, list.size());
    }

}
