package org.mendora.jdbc;

import java.util.Collection;
import java.util.Iterator;

/**
 * 定容栈
 *
 * @param <T>
 * @author menfre
 */
public class FixedCapacityStack<T> implements Iterable<T> {

    /**
     * 数组缓存区间
     */
    private T[] a;

    /**
     * 栈大小
     */
    private int size;

    /**
     * 载入数据
     *
     * @param collection 集合
     * @param <E>        集合类型
     * @return 指定类型的定容栈
     */
    public static <E> FixedCapacityStack<E> of(Collection<E> collection) {
        final FixedCapacityStack<E> es = new FixedCapacityStack<>(collection.size());
        collection.forEach(es::push);
        return es;
    }

    public FixedCapacityStack(int capacity) {
        a = (T[]) new Object[capacity];
    }

    /**
     * 是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 栈大小
     *
     * @return 栈大小
     */
    public int size() {
        return size;
    }

    /**
     * 添加元素
     *
     * @param item 元素
     * @return 定容栈
     */
    private FixedCapacityStack<T> push(T item) {
        a[size++] = item;
        return this;
    }

    /**
     * 弹出元素
     *
     * @return 元素
     */
    public T pop() {
        return a[--size];
    }

    @Override
    public Iterator<T> iterator() {
        return new FixedCapacityStackIterator();
    }

    private class FixedCapacityStackIterator implements Iterator<T> {
        private int i = size;

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public T next() {
            return a[--i];
        }
    }
}
