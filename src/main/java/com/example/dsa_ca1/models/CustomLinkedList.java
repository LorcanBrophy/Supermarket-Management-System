package com.example.dsa_ca1.models;

public class CustomLinkedList<T> {
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
        String str = "";
        Node<T> temp = head;
        while (temp != null) {
            str += temp.getData() + "\n";
            temp = temp.getNext();
        }
        return str;
    }

    public int size() {
        return size;
    }
}
