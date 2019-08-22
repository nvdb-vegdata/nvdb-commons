package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public abstract class Helpers {

    public static String paramMarkers(long count) {
        return Stream.generate(() -> "?").limit(count).collect(joining(", "));
    }

    public static Object unwrapOptional(Object obj) {
        if (obj instanceof Optional<?>) {
            Optional<?> opt = (Optional<?>)obj;
            return opt.orElse(null);
        } else {
            return obj;
        }
    }
}
