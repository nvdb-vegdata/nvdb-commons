package no.vegvesen.vt.nvdb.commons.core.functional;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * General purpose predicates for streams.
 */
public final class Predicates {
    private Predicates() {}

    /**
     * Removes duplicate objects from a stream when used in filter(). Like distinct() but using a specific key to define uniqueness.
     * @param keyExtractor the function to extract the key from the object
     * @return the predicate
     */
    public static <T> Predicate<? super T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Negates the outcome of another predicate.
     * @param inner the predicate to be negated
     * @return the predicate
     */
    public static <T> Predicate<? super T> not(Predicate<? super T> inner) {
        return inner.negate();
    }

    /**
     * Tests whether the result of a function is null.
     * @param function the function to test result of
     * @return the predicate
     */
    public static <T> Predicate<? super T> nullValue(Function<? super T, Object> function) {
        return o -> Objects.isNull(function.apply(o));
    }

    /**
     * Tests whether an object is instance of specified class.
     * @param clazz the class to test instance on
     * @return the predicate
     */
    public static <T> Predicate<? super T> instanceOf(Class<?> clazz) {
        return clazz::isInstance;
    }

    /**
     * Tests whether an object is contained in a container.
     * @param collection the collection to test containment on
     * @return the predicate
     */
    public static <T> Predicate<? super T> containedIn(Collection<? super T> collection) {
        return collection::contains;
    }

    /**
     * Tests whether an object is the same as another.
     * @param other the object to compare with
     * @return the predicate
     */
    public static <T> Predicate<? super T> sameAs(T other) {
        return o -> o == other;
    }
}
