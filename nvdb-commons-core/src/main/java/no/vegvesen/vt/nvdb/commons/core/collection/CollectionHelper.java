package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.core.functional.Functions.castTo;

/**
 * Helper functions for collections
 */
public final class CollectionHelper {
    private CollectionHelper() {}

    public static <T> Set<T> asSet(T... items) {
        if (isNull(items)) {
            return new HashSet<>();
        } else {
            return Arrays.stream(items).collect(toSet());
        }
    }

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

    public static boolean nonEmpty(Collection<?> collection) {
        return nonNull(collection) && !collection.isEmpty();
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
     * Returns a list containing all items in both first and second list
     */
    public static <T> List<T> concat(List<T> first, List<T> second) {
        List<T> result = new ArrayList<>(first);
        result.addAll(second);
        return result;
    }

    /**
     * Returns a list containing the items of first list not found in second list
     */
    public static <T> List<T> subtract(List<T> first, List<T> second) {
        List<T> result = new ArrayList<>(first);
        result.removeIf(second::contains);
        return result;
    }

    /**
     * Returns a list where each element in the original list is casted to the given target class.
     */
    public static <T, S extends T> List<T> castListElements(List<S> list, Class<T> targetClass) {
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
    public static <T> Stream<T> streamIfNonNull(T... items) {
        if (items == null) {
            return Stream.empty();
        } else {
            return Stream.of(items);
        }
    }

    /**
     * Adds items from collection to set, returns true if any of the new items exist in the set or there are duplicates in list
     */
    public static <T> boolean hasDuplicatesWhenAdding(Set<T> set, Collection<T> toAdd) {
        return toAdd.stream().map(set::add).anyMatch(b -> !b);
    }

    public static <T> Predicate<T> existsInCollection(Collection <T> collection) {
        return element -> collection.stream().anyMatch(e -> e.equals(element));
    }
}
