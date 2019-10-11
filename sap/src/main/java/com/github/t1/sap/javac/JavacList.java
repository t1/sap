package com.github.t1.sap.javac;

import com.sun.tools.javac.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Function;

/**
 * A view on the <code>List</code> class used by <code>javac</code>
 */
@RequiredArgsConstructor
class JavacList<T, U> implements java.util.List<T> {
    private final @NonNull List<U> list;
    private final @NonNull Function<U, T> constructor;

    @Override public int size() { return list.size(); }

    @Override public boolean isEmpty() { return list.isEmpty(); }

    @Override public boolean contains(Object o) { return list.contains(o); }

    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i = 0;

            @Override public boolean hasNext() {
                return i < size();
            }

            @Override public T next() {
                return get(i++);
            }

            @Override public void remove() {
                if (i == 1) {
                    list.head = list.tail.head;
                    list.tail = list.tail.tail;
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        };
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }

    @Override public <T1> T1[] toArray(T1[] a) { throw new UnsupportedOperationException(); }

    @Override public boolean add(T t) { throw new UnsupportedOperationException(); }

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override public boolean addAll(Collection<? extends T> c) { throw new UnsupportedOperationException(); }

    @Override public boolean addAll(int index, Collection<? extends T> c) { throw new UnsupportedOperationException(); }

    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override public void clear() { list.clear(); }

    @Override public T get(int index) { return constructor.apply(list.get(index)); }

    @Override public T set(int index, T element) { throw new UnsupportedOperationException(); }

    @Override public void add(int index, T element) { throw new UnsupportedOperationException(); }

    @Override public T remove(int index) { throw new UnsupportedOperationException(); }

    @Override public int indexOf(Object o) { return list.indexOf(o); }

    @Override public int lastIndexOf(Object o) { return list.lastIndexOf(o); }

    @Override public ListIterator<T> listIterator() { throw new UnsupportedOperationException(); }

    @Override public ListIterator<T> listIterator(int index) { throw new UnsupportedOperationException(); }

    @Override public java.util.List<T> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
}
