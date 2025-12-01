package com.example.dsa_ca1.models;

import java.util.Iterator;

public class CustomIterator<T> implements Iterator<T> {

    // keeps track of position in the linked list
    private Node<T> pos;

    // starts the iterator at the given node
    public CustomIterator(Node<T> node) {
        this.pos = node;
    }

    // checks there is another element in the list
    @Override
    public boolean hasNext() {
        return pos != null;
    }

    @Override
    public T next() {
        // store the current node, move to the next one, then return the data
        Node<T> temp = pos;
        pos = pos.next;
        return temp.getData();
    }
}
