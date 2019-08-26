package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.ArrayList;
import java.util.Collections;

public class MultiMapOfList<K extends Comparable<K>, V> extends AbstractMultiMap<K, V> {

    public MultiMapOfList() {
        super(ArrayList::new, Collections::emptyList);
    }
}
