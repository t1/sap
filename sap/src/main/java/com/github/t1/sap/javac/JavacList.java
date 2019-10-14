package com.github.t1.sap.javac;

import com.sun.tools.javac.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * A view on the <code>List</code> class used by <code>javac</code>, mapping between types.
 * Creating it is a tad complex as we also have to overwrite the original value.
 */
@SuppressWarnings("NullableProblems")
@RequiredArgsConstructor
class JavacList<T, U> implements java.util.List<T> {
    private final @NonNull List<U> list;
    private final @NonNull Function<U, T> constructor;
    private final @NonNull Function<T, U> deconstructor;
    private final @NonNull Consumer<List<U>> overwrite;

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
                    overwrite.accept(List.filter(list, list.get(i - 1)));
                }
            }
        };
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException("toArray"); }

    @Override public <T1> T1[] toArray(T1[] a) { throw new UnsupportedOperationException("toArray[]"); }

    @Override public boolean add(T t) { throw new UnsupportedOperationException("add"); }

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException("remove"); }

    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException("containsAll"); }

    @Override public boolean addAll(Collection<? extends T> c) {
        List<U> newTail = List.from(c.stream().map(deconstructor).collect(toList()));
        if (isEmpty())
            overwrite.accept(newTail);
        else
            lastTail().tail = newTail;
        return true;
    }

    private List<U> lastTail() {
        List<U> result = list;
        while (!result.tail.isEmpty())
            result = result.tail;
        return result;
    }

    @Override public boolean addAll(int index, Collection<? extends T> c) { throw new UnsupportedOperationException("addAll"); }

    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException("removeAll"); }

    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException("retainAll"); }

    @Override public void clear() { list.clear(); }

    @Override public T get(int index) { return constructor.apply(list.get(index)); }

    @Override public T set(int index, T element) { throw new UnsupportedOperationException("set"); }

    @Override public void add(int index, T element) { throw new UnsupportedOperationException("add"); }

    @Override public T remove(int index) { throw new UnsupportedOperationException("remove(i)"); }

    @Override public int indexOf(Object o) { return list.indexOf(o); }

    @Override public int lastIndexOf(Object o) { return list.lastIndexOf(o); }

    @Override public ListIterator<T> listIterator() { throw new UnsupportedOperationException("listIterator"); }

    @Override public ListIterator<T> listIterator(int index) { throw new UnsupportedOperationException("listIterator(i)"); }

    @Override public java.util.List<T> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException("subList"); }
}
