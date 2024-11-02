package fr.myriapod.milkywayexplorer.tools;

public class Tuple<K, V> {

    K a;
    V b;

    public Tuple(K a, V b) {
        this.a = a;
        this.b = b;
    }

    public K getA() {
        return a;
    }

    public V getB() {
        return b;
    }
}
