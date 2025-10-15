package com.example.dsa_ca1.models;

import java.util.Iterator;

public class CustomLinkedList<T> implements Iterable<T> {
    private Node<T> head = null;
    private Node<T> tail = null;
    private int size;



    //|| OLD METHOD ADDS TO TAIL ONLY ||

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

    /*public void addHead(T data) {
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
    }*/


    //|| OLD METHOD REMOVES TAIL ONLY ||

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

    /*public void removeHead() {
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
    }*/

    public String display() {
        String str = "";
        for (T item : this) {
            str += item + "\n";
        }
        return str;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator<>(head);
    }
}
