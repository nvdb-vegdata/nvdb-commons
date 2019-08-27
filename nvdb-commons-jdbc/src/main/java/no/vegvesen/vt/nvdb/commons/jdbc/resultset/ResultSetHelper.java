package no.vegvesen.vt.nvdb.commons.jdbc.resultset;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static no.vegvesen.vt.nvdb.commons.core.functional.Optionals.mapIfNonNull;

public class ResultSetHelper {

    public static ResultSetMapper<Void> forEach(Consumer<ResultSetInspector> consumer) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            while (rs.next()) {
                consumer.accept(rsi);
            }
            return null;
        };
    }

    public static <T> ResultSetMapper<List<T>> toObjectList(Function<ResultSetInspector, T> objectMapper) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            List<T> objects = new LinkedList<>();
            while (rs.next()) {
                objects.add(objectMapper.apply(rsi));
            }
            return objects;
        };
    }

    public static <T> ResultSetMapper<Set<T>> toObjectSet(Function<ResultSetInspector, T> objectMapper) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            Set<T> objects = new LinkedHashSet<>();
            while (rs.next()) {
                objects.add(objectMapper.apply(rsi));
            }
            return objects;
        };
    }

    public static <T> ResultSetMapper<Optional<T>> toOptional(Function<ResultSetInspector, T> objectMapper) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            if (rs.next()) {
                return Optional.of(objectMapper.apply(rsi));
            }
            return Optional.empty();
        };
    }

    public static <T> ResultSetMapper<T> toSingleton(Function<ResultSetInspector, T> objectMapper) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            if (rs.next()) {
                return objectMapper.apply(rsi);
            } else {
                throw emptyResultSet();
            }
        };
    }

    public static ResultSetMapper<Integer> toSingleInteger() {
        return toSingleton(rsi -> rsi.getInt(1).orElseThrow(ResultSetHelper::emptyResultSet));
    }

    public static ResultSetMapper<String> toSingleString() {
        return toSingleton(rsi -> rsi.getString(1).orElseThrow(ResultSetHelper::emptyResultSet));
    }

    public static Function<ResultSetInspector, Integer> toInteger(Field field) {
        return toInteger(field.alias());
    }

    public static Function<ResultSetInspector, Integer> toInteger(String alias) {
        return rsi -> rsi.getIntOrNull(alias);
    }

    public static Function<ResultSetInspector, Long> toLong(Field field) {
        return toLong(field.alias());
    }

    public static Function<ResultSetInspector, Long> toLong(String alias) {
        return rsi -> rsi.getLongOrNull(alias);
    }

    public static Function<ResultSetInspector, String> toString(Field field) {
        return toString(field.alias());
    }

    public static Function<ResultSetInspector, String> toString(String alias) {
        return rsi -> rsi.getStringOrNull(alias);
    }

    public static Function<ResultSetInspector, UUID> toUuid(Field field) {
        return toUuid(field.alias());
    }

    public static Function<ResultSetInspector, UUID> toUuid(String alias) {
        return toString(alias).andThen(s -> mapIfNonNull(s, UUID::fromString));
    }

    @FunctionalInterface
    public interface ResultSetValueSupplier<T> {
        T get() throws SQLException;
    }

    public static <T> T wrapSqlException(ResultSetValueSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static RuntimeException emptyResultSet() {
        return new RuntimeException("The result set is empty");
    }
}
