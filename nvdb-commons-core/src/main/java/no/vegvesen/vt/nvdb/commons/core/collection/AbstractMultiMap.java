package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.nonEmpty;
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.streamIfNonNull;

public class AbstractMultiMap<K extends Comparable<K>, V> implements MultiMap<K, V> {
    private final Supplier<Collection<V>> collectionFactory;
    private final Supplier<Collection<V>> emptyCollectionSupplier;

    private final SortedMap<K, Collection<V>> entries = new TreeMap<>();

    public AbstractMultiMap(Supplier<Collection<V>> collectionFactory, Supplier<Collection<V>> emptyCollectionSupplier) {
        this.collectionFactory = collectionFactory;
        this.emptyCollectionSupplier = emptyCollectionSupplier;
    }

    @Override
    public void put(K key, V newValue) {
        if (nonNull(newValue)) {
            Collection<V> values = getValuesOrCreateEmpty(key);
            values.add(newValue);
        }
    }

    @Override
    public void putAll(K key, Collection<V> newValues) {
        if (nonEmpty(newValues)) {
            Collection<V> values = getValuesOrCreateEmpty(key);
            values.addAll(newValues);
        }
    }

    @Override
    public void putAll(Function<V,K> keyMapper, Collection<V> newValues) {
        streamIfNonNull(newValues).forEach(newValue -> {
            K key = keyMapper.apply(newValue);
            put(key, newValue);
        });
    }

    @Override
    public Collection<V> get(K key) {
        return entries.getOrDefault(key, emptyCollectionSupplier.get());
    }

    @Override
    public boolean removeAll(K key) {
        return nonNull(entries.remove(key));
    }

    @Override
    public boolean remove(K key, V value) {
        Collection<V> values = entries.get(key);
        if (nonNull(values)) {
            if (values.contains(value)) {
                values.remove(value);
                if (values.isEmpty()) {
                    entries.remove(key);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public int valueCount() {
        return entries.values().stream().map(Collection::size).reduce(Integer::sum).orElse(0);
    }

    @Override
    public Optional<K> firstKey() {
        return isEmpty() ? Optional.empty() : Optional.of(entries.firstKey());
    }

    @Override
    public Optional<K> lastKey() {
        return isEmpty() ? Optional.empty() : Optional.of(entries.lastKey());
    }

    @Override
    public Optional<V> firstValue(K key) {
        Collection<V> values = get(key);
        if (nonEmpty(values)) {
            if (values instanceof List) {
                return Optional.of(((List<V>)values).get(0));
            } else if (values instanceof SortedSet) {
                return Optional.of(((SortedSet<V>)values).first());
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<V> lastValue(K key) {
        Collection<V> values = get(key);
        if (nonEmpty(values)) {
            if (values instanceof List) {
                return Optional.of(((List<V>)values).get(values.size()-1));
            } else if (values instanceof SortedSet) {
                return Optional.of(((SortedSet<V>)values).last());
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void forEach(BiConsumer<K, Collection<V>> action) {
        entries.forEach(action);
    }

    @Override
    public Stream<K> keys() {
        return entries.keySet().stream();
    }

    @Override
    public Stream<V> values() {
        return entries.values().stream().flatMap(Collection::stream);
    }

    @Override
    public Stream<V> values(K key) {
        return streamIfNonNull(entries.get(key));
    }

    @Override
    public SortedMap<K, Collection<V>> entries() {
        return entries;
    }

    @Override
    public boolean containsKey(K key) {
        return entries.containsKey(key);
    }

    @Override
    public boolean containsValue(K key, V value) {
        Collection<V> values = entries.get(key);
        return nonNull(values) && values.contains(value);
    }

    private Collection<V> getValuesOrCreateEmpty(K key) {
        Collection<V> values = entries.get(key);
        if (isNull(values)) {
            values = collectionFactory.get();
            entries.put(key, values);
        }
        return values;
    }
}
