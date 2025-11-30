package com.example.dsa_ca1.models;

import java.util.Iterator;

public class CustomLinkedList<T> implements Iterable<T> {
    private Node<T> head = null;
    private int size;

    // add elements to linked list
    public void linkedListAdd(T data) {
        Node<T> nn = new Node<>(data);
        if (head == null) {
            head = nn;
        } else {
            Node<T> temp = head;
            while (temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.next = nn;
        }
        size++;
    }

    // remove elements from linked list
    public void linkedListRemove(T data) {
        if (head == null) return;

        if (head.getData().equals(data)) {
            head = head.getNext();
            size--;
            return;
        }

        Node<T> temp = head;
        while (temp.getNext() != null) {
            if (temp.getNext().getData().equals(data)) {
                temp.setNext(temp.getNext().getNext());
                size--;
                return;
            }
            temp = temp.getNext();
        }
    }

    // helper methods
    public int size() {
        return size;
    }

    public void clear() {
        head = null;
        size = 0;
    }

    // display elements using iterator
    public String display() {
        StringBuilder str = new StringBuilder();
        for (T item : this) {
            str.append(item).append("\n");
        }
        return str.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator<>(head);
    }
}
