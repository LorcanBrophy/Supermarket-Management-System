package com.example.dsa_ca1.models;

import java.util.Iterator;

public class CustomLinkedList<T> implements Iterable<T> {
    private Node<T> head = null;
    private Node<T> tail = null;
    private int size;

    // add elements to linked list
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

    public void addHead(T data) {
        Node<T> nn = new Node<>(data);
        if (size == 0) {
            head = tail = nn;
        } else {
            nn.setNext(head);
            head = nn;
        }
        size++;
    }

    public void addTail(T data) {
        Node<T> nn = new Node<>(data);
        if (size == 0) {
            head = tail = nn;
        } else {
            tail.setNext(nn);
            tail = tail.getNext();
        }
        size++;
    }


    // remove elements from linked list
    public void removeValue(T data) {
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
        while (temp!=null) {
            if (data.equals(temp.getData())) {
                temp=temp.next;
            }
        }
    }

    public void removeHead() {
        if (size == 0) return;

        head = head.getNext();
        size--;

        if (size == 0) tail = null;
    }

    public void removeTail() {
        if (size == 0) return;

        if (size == 1) head = tail = null;
        else {
            Node<T> temp = head;
            while (temp.getNext() != tail) {
                temp = temp.getNext();
            }
            temp.setNext(null);
            tail = temp;
        }
        size--;
    }

    // helper methods
    public int size() {
        return size;
    }

    public void clear() {
        head = null;
        tail = null;
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
