package no.vegvesen.vt.nvdb.commons.core.functional;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Optionals {
    private Optionals() {}

    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.map(Stream::of).orElse(Stream.empty());
    }

    public static <T> T defaultIfNull(T value, T defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    public static <T, U> U mapIfNonNull(T value, Function<T, U> mapper) {
        return Optional.ofNullable(value).map(mapper).orElse(null);
    }
}
