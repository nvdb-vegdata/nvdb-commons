package no.vegvesen.vt.nvdb.commons.jdbc.resultset;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.math.BigDecimal;
import java.sql.ResultSet;
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

    public static ResultSetMapper<Void> forEach(Consumer<ResultSet> consumer) {
        return rs -> {
            while (rs.next()) {
                consumer.accept(rs);
            }
            return null;
        };
    }

    public static <T> ResultSetMapper<List<T>> toObjectList(Function<ResultSet, T> objectMapper) {
        return rs -> {
            List<T> objects = new LinkedList<>();
            while (rs.next()) {
                objects.add(objectMapper.apply(rs));
            }
            return objects;
        };
    }

    public static <T> ResultSetMapper<Set<T>> toObjectSet(Function<ResultSet, T> objectMapper) {
        return rs -> {
            Set<T> objects = new LinkedHashSet<>();
            while (rs.next()) {
                objects.add(objectMapper.apply(rs));
            }
            return objects;
        };
    }

    public static <T> ResultSetMapper<Optional<T>> toOptional(Function<ResultSet, T> objectMapper) {
        return rs -> {
            if (rs.next()) {
                return Optional.of(objectMapper.apply(rs));
            }
            return Optional.empty();
        };
    }

    public static <T> ResultSetMapper<T> toSingleton(Function<ResultSet, T> objectMapper) {
        return rs -> {
            if (rs.next()) {
                return objectMapper.apply(rs);
            } else {
                throw new RuntimeException("The result set is empty");
            }
        };
    }

    public static ResultSetMapper<Integer> toSingleInteger() {
        return toSingleton(rs -> wrapSqlException(() -> {
            BigDecimal value = rs.getBigDecimal(1);
            return mapIfNonNull(value, BigDecimal::intValue);
        }));
    }

    public static ResultSetMapper<String> toSingleString() {
        return toSingleton(rs -> wrapSqlException(() -> rs.getString(1)));
    }

    public static Function<ResultSet, Integer> toInteger(Field field) {
        return toInteger(field.alias());
    }

    public static Function<ResultSet, Integer> toInteger(String alias) {
        return rs -> wrapSqlException(() -> {
            BigDecimal value = rs.getBigDecimal(alias);
            return mapIfNonNull(value, BigDecimal::intValue);
        });
    }

    public static Function<ResultSet, Long> toLong(Field field) {
        return toLong(field.alias());
    }

    public static Function<ResultSet, Long> toLong(String alias) {
        return rs -> wrapSqlException(() -> {
            BigDecimal value = rs.getBigDecimal(alias);
            return mapIfNonNull(value, BigDecimal::longValue);
        });
    }

    public static Function<ResultSet, String> toString(Field field) {
        return toString(field.alias());
    }

    public static Function<ResultSet, String> toString(String alias) {
        return rs -> wrapSqlException(() -> rs.getString(alias));
    }

    public static Function<ResultSet, UUID> toUuid(Field field) {
        return toUuid(field.alias());
    }

    public static Function<ResultSet, UUID> toUuid(String alias) {
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
}
