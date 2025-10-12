package com.example.dsa_ca1.models;

public class CustomLinkedList<T> {
    private class Node {
        private T data;
        private Node next;

        Node(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    private Node head;

    public void add(T data) {
        Node nn = new Node(data);
        if (head != null) { // not first node in LL
            Node current = head; // start at head of LL
            while (current.getNext() != null) { // not at final node
                current = current.next;
            }
            current.setNext(nn);
        } else head = nn; // make head node
    }

    public void display() {
        Node current = head;
        while (current != null) {
            System.out.println(current.getData());
            current = current.getNext();
        }
    }
}