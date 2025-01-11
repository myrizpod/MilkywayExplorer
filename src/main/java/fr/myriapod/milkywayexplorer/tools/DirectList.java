package fr.myriapod.milkywayexplorer.tools;

import java.util.*;

public class DirectList<T> {

    List<T> list = new ArrayList<>();

    @SafeVarargs
    public DirectList(T... part) {
        list.addAll(Arrays.asList(part));
    }

    public List<T> getList() {
        return list;
    }

    public Iterator<T> getIterator() {return list.iterator();}


}
