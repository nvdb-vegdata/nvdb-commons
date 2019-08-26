package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.isEmpty;
import static no.vegvesen.vt.nvdb.commons.core.lang.StringHelper.isBlank;

public class Csv {
    private Csv() {}

    public static <T> String format(Collection<T> items) {
        return format(items, Objects::toString);
    }

    public static <T> String format(Collection<T> items, Function<T, String> mapper) {
        if (isEmpty(items)) {
            return "";
        } else {
            return items.stream().map(mapper).collect(joining(","));
        }
    }

    public static List<Integer> parseInts(String csv) {
        try {
            return Csv.parse(csv, Integer::parseInt);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse as comma separated integers: " + csv, e);
        }
    }

    public static <T> List<T> parse(String csv, Function<String, T> mapper) {
        if (isBlank(csv)) {
            return null;
        } else {
            return Stream.of(csv.split(","))
                    .map(String::trim)
                    .map(mapper)
                    .collect(toList());
        }
    }
}
