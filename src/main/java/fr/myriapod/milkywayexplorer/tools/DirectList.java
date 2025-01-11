package fr.myriapod.milkywayexplorer.tools;

import java.util.*;
import java.util.function.Consumer;

public class DirectList<T> implements Iterator<T> {

    List<T> list = new ArrayList<>();

    @SafeVarargs
    public DirectList(T... part) {
        list.addAll(Arrays.asList(part));
    }

    public List<T> getList() {
        return list;
    }


    @Override
    public boolean hasNext() {
        return list.iterator().hasNext();
    }

    @Override
    public T next() {
        return list.iterator().next();
    }

}
