package com.example.dsa_ca1.models;

import java.util.Iterator;

public class CustomLinkedList<T> implements Iterable<T> {
    public Node<T> head = null;
    private int size;

    public void add(T data) {
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

    public void remove(T data) {
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

    public String display() {
        StringBuilder str = new StringBuilder();
        for (T item : this) {
            str.append(item).append("\n");
        }
        return str.toString();
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator<>(head);
    }
}
