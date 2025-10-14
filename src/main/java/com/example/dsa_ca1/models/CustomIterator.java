package com.example.dsa_ca1.models;

import java.util.Iterator;

public class CustomIterator<T> implements Iterator<T> {
    private Node<T> pos;

    public CustomIterator(Node<T> node) {
        this.pos = node;
    }


    @Override
    public boolean hasNext() {
        return pos != null;
    }

    @Override
    public T next() {
        Node<T> temp = pos;
        pos = pos.next;
        return temp.getData();
    }
}
