package org.mendora.jdbc;

import java.util.Collection;
import java.util.Iterator;

/**
 * 定容栈
 *
 * @param <T>
 */
public class FixedCapacityStack<T> implements Iterable<T> {

    private T[] a;

    private int len;

    public static <E> FixedCapacityStack<E> wrap(Collection<E> collection) {
        final FixedCapacityStack<E> es = new FixedCapacityStack<>(collection.size());
        collection.forEach(es::push);
        return es;
    }

    public FixedCapacityStack(int capacity) {
        a = (T[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return len == 0;
    }

    public int size() {
        return len;
    }

    public FixedCapacityStack<T> push(T item) {
        a[len++] = item;
        return this;
    }

    public T pop() {
        return a[--len];
    }

    private class FixedCapacityStackIterator implements Iterator<T> {
        private int i = len;

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public T next() {
            return a[--i];
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new FixedCapacityStackIterator();
    }
}
