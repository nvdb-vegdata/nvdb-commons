package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements a two-way map
 */
public class BiMap<P, Q> {

    Map<P, Q> right = new HashMap<>();
    Map<Q, P> left = new HashMap<>();

    public void put(P left, Q right) {
        this.right.put(left, right);
        this.left.put(right, left);
    }

    public Q getRight(P left) {
        return right.get(left);
    }

    public P getLeft(Q right) {
        return left.get(right);
    }

    public long size() { return right.size(); }

}
