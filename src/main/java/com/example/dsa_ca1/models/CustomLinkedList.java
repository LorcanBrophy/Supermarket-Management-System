package com.example.dsa_ca1.models;

import java.util.Iterator;

public class CustomLinkedList<T> implements Iterable<T> {

    // fields
    private Node<T> head = null;
    private int size;

    // add elements to end of linked list
    public void linkedListAdd(T data) {
        Node<T> nn = new Node<>(data);

        // list empty, add to head
        if (head == null) {
            head = nn;
            size++;
            return;
        }

        // loop through list till at the final element
        Node<T> temp = head;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }

        // attach nn at the end
        temp.next = nn;
        size++;
    }

    // remove specific element from linked list
    public void linkedListRemove(T data) {
        if (head == null) return; // nothing to remove

        // if the head itself is the match, remove it
        if (head.getData().equals(data)) {
            head = head.getNext();
            size--;
            return;
        }

        // move through the list looking for the node to remove
        Node<T> temp = head;
        while (temp.getNext() != null) {
            if (temp.getNext().getData().equals(data)) {
                temp.setNext(temp.getNext().getNext()); // change pointer to skip over the element
                size--;
                return;
            }
            temp = temp.getNext(); // if no match, move forward
        }
    }

    // helper methods

    // returns how many items are currently in the list
    public int size() {
        return size;
    }

    // clears the whole list
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
        // allows for each loops
        return new CustomIterator<>(head);
    }
}