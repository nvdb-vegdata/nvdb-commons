package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.Collections;
import java.util.HashSet;

public class MultiMapOfSet<K extends Comparable<K>, V> extends AbstractMultiMap<K, V> {

    public MultiMapOfSet() {
        super(HashSet::new, Collections::emptySet);
    }
}
