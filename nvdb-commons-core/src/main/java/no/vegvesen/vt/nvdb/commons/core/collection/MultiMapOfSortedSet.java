package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.Collections;
import java.util.TreeSet;

public class MultiMapOfSortedSet<K extends Comparable<K>, V> extends AbstractMultiMap<K, V> {

    public MultiMapOfSortedSet() {
        super(TreeSet::new, Collections::emptySet);
    }
}
