package no.vegvesen.vt.nvdb.commons.core.functional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * General purpose collectors for streams
 */
public class Collectors {
    private Collectors() {}

    /**
     * Same as Collectors.toMap but preserves stream ordering by using a LinkedHashMap.
     *
     * @param <T> the type of the input elements
     * @param <K> the output type of the key mapping function
     * @param <U> the output type of the value mapping function
     * @param keyMapper a mapping function to produce keys
     * @param valueMapper a mapping function to produce values
     * @return a {@code Collector} which collects elements into a {@code Map}
     * whose keys and values are the result of applying mapping functions to
     * the input elements.
     */
    public static <T, K, U> Collector<T, ?, Map<K,U>> toLinkedMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper)
    {
        return java.util.stream.Collectors.toMap(
                keyMapper,
                valueMapper,
                (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u));},
                LinkedHashMap::new
        );
    }

    /**
     * Same as {@link java.util.stream.Collectors#joining} but allows a different last delimiter.
     * The following statement yields the string "1, 2 and 3":
     * <pre>{@code
     *     Stream.of("1", "2", "3").collect(joining(", ", " and "))
     * }</pre>
     *
     * @param delimiter the delimiter to be used between each element, except between the final two
     * @param lastDelimiter the delimiter to be used between the final two
     * @return A {@link Collector} which concatenates String elements, separated by the specified delimiters, in encounter order
     */
    public static Collector<CharSequence, ?, String> joining(String delimiter, String lastDelimiter) {
        return collectingAndThen(toList(), joiningWithDelimiters(delimiter, lastDelimiter));
    }

    private static Function<List<CharSequence>, String> joiningWithDelimiters(String delimiter, String lastDelimiter) {
        return list -> {
            int last = list.size() - 1;
            if (last < 1) {
                return String.join(delimiter, list);
            }
            return String.join(lastDelimiter, String.join(delimiter, list.subList(0, last)), list.get(last));
        };
    }
}
