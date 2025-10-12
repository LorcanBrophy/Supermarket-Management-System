package com.example.dsa_ca1.models;

class Node<T> {
    T data;
    Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public Node<T> getNext() { return next; }
    public void setNext(Node<T> next) { this.next = next; }
}

public class CustomLinkedList<T> {
    private Node<T> head = null;
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
            return;
        }

        Node<T> temp = head;
        while (temp.getNext() != null) {
            if (temp.getNext().getData().equals(data)) {
                temp.setNext(temp.getNext().getNext());
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
