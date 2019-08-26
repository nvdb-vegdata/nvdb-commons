package no.vegvesen.vt.nvdb.commons.core.functional;

import no.vegvesen.vt.nvdb.commons.core.collection.MultiMap;
import no.vegvesen.vt.nvdb.commons.core.collection.MultiMapOfList;
import no.vegvesen.vt.nvdb.commons.core.collection.MultiMapOfSet;
import no.vegvesen.vt.nvdb.commons.core.collection.MultiMapOfSortedSet;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Collects objects into a {@link MultiMap}.
 */
public class MultiMapCollector<T, K extends Comparable<K>, V> implements Collector<T, MultiMap<K,V>, MultiMap<K,V>> {

    private final Supplier<MultiMap<K, V>> factory;
    private final Function<T, K> keyExtractor;
    private final Function<T, V> valueExtractor;

    public static <K extends Comparable<K>, V> MultiMapCollector<V, K, V> toMultiMapOfList(Function<V, K> keyExtractor) {
        return new MultiMapCollector<>(MultiMapOfList::new, keyExtractor, v -> v);
    }

    public static <T, K extends Comparable<K>, V> MultiMapCollector<T, K, V> toMultiMapOfList(Function<T, K> keyExtractor, Function<T, V> valueExtractor) {
        return new MultiMapCollector<>(MultiMapOfList::new, keyExtractor, valueExtractor);
    }

    public static <K extends Comparable<K>, V> MultiMapCollector<V, K, V> toMultiMapOfSet(Function<V, K> keyExtractor) {
        return new MultiMapCollector<>(MultiMapOfSet::new, keyExtractor, v -> v);
    }

    public static <T, K extends Comparable<K>, V> MultiMapCollector<T, K, V> toMultiMapOfSet(Function<T, K> keyExtractor, Function<T, V> valueExtractor) {
        return new MultiMapCollector<>(MultiMapOfSet::new, keyExtractor, valueExtractor);
    }

    public static <K extends Comparable<K>, V> MultiMapCollector<V, K, V> toMultiMapOfSortedSet(Function<V, K> keyExtractor) {
        return new MultiMapCollector<>(MultiMapOfSortedSet::new, keyExtractor, v -> v);
    }

    public static <T, K extends Comparable<K>, V> MultiMapCollector<T, K, V> toMultiMapOfSortedSet(Function<T, K> keyExtractor, Function<T, V> valueExtractor) {
        return new MultiMapCollector<>(MultiMapOfSortedSet::new, keyExtractor, valueExtractor);
    }

    public MultiMapCollector(Supplier<MultiMap<K, V>> factory, Function<T, K> keyExtractor, Function<T, V> valueExtractor) {
        this.factory = factory;
        this.keyExtractor = keyExtractor;
        this.valueExtractor = valueExtractor;
    }

    @Override
    public Supplier<MultiMap<K, V>> supplier() {
        return factory;
    }

    @Override
    public BiConsumer<MultiMap<K, V>, T> accumulator() {
        return (map, value) -> map.put(keyExtractor.apply(value), valueExtractor.apply(value));
    }

    @Override
    public BinaryOperator<MultiMap<K, V>> combiner() {
        return (left, right) -> {
            right.forEach(left::putAll);
            return left;
        };
    }

    @Override
    public Function<MultiMap<K, V>, MultiMap<K, V>> finisher() {
        return m -> m;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.IDENTITY_FINISH);
    }
}