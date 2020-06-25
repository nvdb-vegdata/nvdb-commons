package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.core.functional.Functions.castTo;
import static no.vegvesen.vt.nvdb.commons.core.functional.Predicates.not;

/**
 * Helper functions for collections
 */
public final class CollectionHelper {
    @SafeVarargs
    public static <T> Set<T> asSet(T... items) {
        if (isNull(items)) {
            return new HashSet<>();
        } else {
            return Arrays.stream(items).collect(toSet());
        }
    }

    @SafeVarargs
    public static <T> List<T> asList(T... items) {
        if (isNull(items)) {
            return new ArrayList<>();
        } else {
            return Arrays.stream(items).collect(toList());
        }
    }

    public static <T> T head(List<? extends T> collection) {
        requireNonEmpty(collection, "collection is empty");
        return collection.get(0);
    }

    public static <T> T tail(List<? extends T> collection) {
        requireNonEmpty(collection, "collection is empty");
        return collection.get(collection.size() - 1);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return isNull(collection) || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?,?> map) {
        return isNull(map) || map.isEmpty();
    }

    public static boolean nonEmpty(Collection<?> collection) {
        return nonNull(collection) && !collection.isEmpty();
    }

    public static boolean nonEmpty(Map<?,?> map) {
        return nonNull(map) && !map.isEmpty();
    }

    public static <T> Collection<List<T>> partition(Collection<T> items, int batchSize) {
        int[] count = new int[]{-1};
        Map<Integer, List<T>> batchMap = items.stream()
                .collect(groupingBy(item -> {
                            count[0]++;
                            return Math.floorDiv(count[0], batchSize);
                        })
                );

        return batchMap.values();
    }

    /**
     * Returns a list containing all items in given collections
     */
    @SafeVarargs
    public static <T> List<T> concat(Collection<? extends T>... collections) {
        return streamIfNonNull(collections).flatMap(CollectionHelper::streamIfNonNull).collect(toList());
    }

    /**
     * Returns a set containing all items in given sets
     */
    @SafeVarargs
    public static <T> Set<T> concat(Set<? extends T>... sets) {
        return streamIfNonNull(sets).flatMap(CollectionHelper::streamIfNonNull).collect(toSet());
    }

    /**
     * Returns a map containing all items in given maps
     */
    @SafeVarargs
    public static <K, V> Map<K, V> concat(Map<K, ? extends V>... maps) {
        return streamIfNonNull(maps).flatMap(m -> m.entrySet().stream()).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Returns a list containing the items of first collection not found in second collection
     */
    public static <T> List<T> subtract(Collection<T> first, Collection<T> second) {
        List<T> result = new ArrayList<>(first);
        result.removeIf(second::contains);
        return result;
    }

    /**
     * Returns a set containing the items of first set not found in second set
     */
    public static <T> Set<T> subtract(Set<T> first, Set<T> second) {
        Set<T> result = new HashSet<>(first);
        result.removeIf(second::contains);
        return result;
    }

    /**
     * Returns a list where each element in the original list is up-casted to the given target class.
     */
    public static <T, S extends T> List<T> castListElements(List<S> list, Class<T> targetClass) {
        return list.stream().map(castTo(targetClass)).collect(toList());
    }

    /**
     * Returns a list where each element in the original list is down-casted to the given target class.
     */
    public static <S, T extends S> List<T> downcastListElements(List<S> list, Class<T> targetClass) {
        return list.stream().map(castTo(targetClass)).collect(toList());
    }

    /**
     * Returns the stream of the given list, or an empty stream if the list is null.
     */
    public static <T> Stream<T> streamIfNonNull(Collection<T> list) {
        if (list == null) {
            return Stream.empty();
        } else {
            return list.stream();
        }
    }

    /**
     * Returns the stream of the given array, or an empty stream if the array is null or empty.
     */
    @SafeVarargs
    public static <T> Stream<T> streamIfNonNull(T... items) {
        if (items == null) {
            return Stream.empty();
        } else {
            return Stream.of(items);
        }
    }

    /**
     * Adds items from collection to set, returns true if any of the new items exist in the set or there are duplicates in list
     * @return true if any items added already existed in the set or there were non-unique items in the collection
     */
    public static <T> boolean hasDuplicatesWhenAdding(Set<T> set, Collection<T> toAdd) {
        return toAdd.stream().map(set::add).anyMatch(b -> !b);
    }

    /**
     * Adds items from collection to set, returning duplicate items
     * @return items added that already exist in the set or were non-unique in the collection
     */
    public static <T> Set<T> addAndReturnDuplicates(Set<T> set, Collection<T> toAdd) {
        return toAdd.stream().filter(not(set::add)).collect(toSet());
    }

    /**
     * Returns the lowest value in a Collection of Double values.
     * @throws IllegalArgumentException if the collection didn't contain any double values.
     */
    public static Double minValue(Collection<Double> collection) {
        return streamIfNonNull(collection)
                .mapToDouble(v -> v)
                .min()
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Returns the highest value in a Collection of Double values.
     * @throws IllegalArgumentException if the collection didn't contain any double values.
     */
    public static Double maxValue(Collection<Double> collection) {
        return streamIfNonNull(collection)
                .mapToDouble(v -> v)
                .max()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static boolean containsIgnoreCase(Collection<String> collection, String item) {
        return streamIfNonNull(collection).anyMatch(i -> isNull(i) ? isNull(item) : i.equalsIgnoreCase(item));
    }

    /**
     * Returns true if the two collections contain the same items, but not necessarily in the same order.
     * @param first the first collection
     * @param second the second collection
     * @return true if the collections contain the same items; else false
     */
    public static boolean isSame(Collection<?> first, Collection<?> second) {
        if (isNull(first)) {
            return isNull(second);
        } else if (isNull(second)) {
            return false;
        } else {
            return first.size() == second.size() && first.containsAll(second);
        }
    }

    /**
     * Returns true if the two collections contain the same items, but not necessarily in the same order.
     * @param first the first collection
     * @param second the second collection
     * @param sameness the predicate to decide if two collection items are the same
     * @return true if the collections contain the same items; else false
     */
    public static <T> boolean isSame(Collection<T> first, Collection<T> second, BiPredicate<T, T> sameness) {
        if (isNull(first)) {
            return isNull(second);
        } else if (isNull(second)) {
            return false;
        } else if (first.size() != second.size()) {
            return false;
        } else {
            return first.stream().allMatch(f -> second.stream().anyMatch(s -> sameness.test(f, s)));
        }
    }

    /**
     * Returns true if the two collections contain the same strings, regardless of casing, and not necessarily in the same order.
     * @param first the first collection
     * @param second the second collection
     * @return true if the collections contain the same strings (case insensitive); else false
     */
    public static boolean isSameIgnoreCase(Collection<String> first, Collection<String> second) {
        if (isNull(first)) {
            return isNull(second);
        } else if (isNull(second)) {
            return false;
        } else if (first.size() != second.size()) {
            return false;
        } else {
            return first.stream().allMatch(s -> containsIgnoreCase(second, s));
        }
    }
}
