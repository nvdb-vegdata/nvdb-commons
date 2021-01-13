package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.nonNull;


/**
 * A linked hash map behaving like a cache:
 * - Only stores up to a specific number of entries
 * - Removes the 'coldest' (least recently accessed) entry when adding a new entry to a full cache
 * - Putting and getting an entry activates it (makes it 'hot').
 * @param <K> the key type
 * @param <V> the value type
 */
public class CacheMap<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public CacheMap(int initialCapacity, float loadFactor, int maxSize) {
        super(initialCapacity, loadFactor);
        this.maxSize = maxSize;
    }

    public CacheMap(int initialCapacity, int maxSize) {
        super(initialCapacity);
        this.maxSize = maxSize;
    }

    public CacheMap(int maxSize) {
        this.maxSize = maxSize;
    }

    public CacheMap(Map<? extends K, ? extends V> m, int maxSize) {
        super(m);
        this.maxSize = maxSize;
    }

    public CacheMap(int initialCapacity, float loadFactor, boolean accessOrder, int maxSize) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxSize = maxSize;
    }

    @Override
    public V get(Object key) {
        V value = remove(key);
        if (nonNull(value)) {
            // Reinsert at head of map as 'hot' item
            put((K)key, value);
        }
        return value;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
