package no.vegvesen.vt.nvdb.commons.core.collection;

public class Pair<F,S> {
    private final F first;
    private final S second;

    public static <F,S> Pair<F,S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
