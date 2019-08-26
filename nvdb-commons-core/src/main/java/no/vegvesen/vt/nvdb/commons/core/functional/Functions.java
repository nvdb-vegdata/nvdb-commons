package no.vegvesen.vt.nvdb.commons.core.functional;

import no.vegvesen.vt.nvdb.commons.core.lang.StringHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.Objects.nonNull;

/**
 * General purpose functions for streams.
 */
public final class Functions {
    private Functions() {}

    /**
     * Returns a function casting the input object to the specified class.
     * @param targetClass the class to cast to
     * @param <S> the input object type
     * @param <T> the output object type
     * @return the function object
     */
    public static <S, T> Function<S,T> castTo(Class<T> targetClass) {
        return targetClass::cast;
    }

    /**
     * Applies mapper if input is non-null
     * @param mapper the mapper to invoke
     * @param <S> the input object type
     * @param <T> the output object type
     * @return the function object
     */
    public static <S, T> Function<S,T> ifNonNull(Function<S,T> mapper) {
        return s -> nonNull(s) ? mapper.apply(s) : (T)null;
    }

    public static Function<String, List<String>> split(String regex) {
        return ifNonNull(s -> StringHelper.split(s, regex));
    }

    public static BinaryOperator<LocalDate> earliestDate() {
        return (a, b) -> a == null ? b : (b == null ? a : (a.compareTo(b) < 0 ? a : b));
    }
}