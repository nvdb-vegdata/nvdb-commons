package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.Collection;
import java.util.Optional;
import java.util.SortedMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Defines a map associating a collection of values of type V to some key of type K.
 * The keys are kept in ascending natural order.
 * @param <K> the key type, must implement the {@link Comparable} interface
 * @param <V> the value type
 */
public interface MultiMap<K extends Comparable<K>, V> {
    void put(K key, V newValue);

    void putAll(K key, Collection<V> newValues);
    void putAll(Function<V, K> keyMapper, Collection<V> newValues);

    Collection<V> get(K key);

    /**
     * Removes all values for specified key
     * @param key the key to remove values for
     * @return true if one or more values were removed; else false
     */
    boolean removeAll(K key);

    /**
     * Removes specified value if found for specified key
     * @param key the key to remove value for
     * @param value the value to remove
     * @return true if value was removed; else false
     */
    boolean remove(K key, V value);

    boolean isEmpty();

    int size();

    int valueCount();

    Optional<K> firstKey();

    Optional<K> lastKey();

    Optional<V> firstValue(K key);

    Optional<V> lastValue(K key);

    void forEach(BiConsumer<K, Collection<V>> action);

    /**
     * Returns a stream of all keys in ascending natural order.
     */
    Stream<K> keys();

    /**
     * Returns a stream of all values in ascending natural order of corresponding key.
     */
    Stream<V> values();

    /**
     * Returns a stream of all values for a key.
     */
    Stream<V> values(K key);

    SortedMap<K, Collection<V>> entries();

    boolean containsKey(K key);

    boolean containsValue(K key, V value);
}
