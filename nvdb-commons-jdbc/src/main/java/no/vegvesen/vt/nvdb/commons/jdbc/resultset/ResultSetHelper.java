package no.vegvesen.vt.nvdb.commons.jdbc.resultset;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static no.vegvesen.vt.nvdb.commons.core.functional.Optionals.mapIfNonNull;

public class ResultSetHelper {

    public static ResultSetMapper<Boolean> nonEmptyResultSet() {
        return ResultSet::next;
    }

    public static ResultSetMapper<Void> forEach(Consumer<ResultSetInspector> consumer) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            while (rs.next()) {
                consumer.accept(rsi);
            }
            return null;
        };
    }

    public static <T> ResultSetMapper<List<T>> toObjectList(ResultSetRowMapper<T> objectMapper) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            List<T> objects = new LinkedList<>();
            while (rs.next()) {
                objects.add(objectMapper.map(rsi));
            }
            return objects;
        };
    }

    public static <T> ResultSetMapper<Set<T>> toObjectSet(ResultSetRowMapper<T> objectMapper) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            Set<T> objects = new LinkedHashSet<>();
            while (rs.next()) {
                objects.add(objectMapper.map(rsi));
            }
            return objects;
        };
    }

    public static <T> ResultSetMapper<Optional<T>> toOptional(ResultSetRowMapper<T> objectMapper) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            if (rs.next()) {
                return Optional.ofNullable(objectMapper.map(rsi));
            }
            return Optional.empty();
        };
    }

    public static <T> ResultSetMapper<T> toSingleton(ResultSetRowMapper<T> objectMapper) {
        return rs -> {
            ResultSetInspector rsi = ResultSetInspector.from(rs);
            if (rs.next()) {
                return objectMapper.map(rsi);
            } else {
                throw emptyResultSet();
            }
        };
    }

    public static ResultSetMapper<Integer> toSingleInteger() {
        return toSingleton(rsi -> rsi.getInt(1).orElseThrow(ResultSetHelper::emptyResultSet));
    }

    public static ResultSetMapper<Long> toSingleLong() {
        return toSingleton(rsi -> rsi.getLong(1).orElseThrow(ResultSetHelper::emptyResultSet));
    }

    public static ResultSetMapper<String> toSingleString() {
        return toSingleton(rsi -> rsi.getString(1).orElseThrow(ResultSetHelper::emptyResultSet));
    }

    public static ResultSetMapper<List<String>> toStringList() {
        return toObjectList(rsi -> rsi.getString(1).orElseThrow(ResultSetHelper::emptyResultSet));
    }

    public static ResultSetRowMapper<Integer> toInteger(Field field) {
        return toInteger(field.alias());
    }

    public static ResultSetRowMapper<Integer> toInteger(String alias) {
        return rsi -> rsi.getIntOrNull(alias);
    }

    public static ResultSetRowMapper<Long> toLong(Field field) {
        return toLong(field.alias());
    }

    public static ResultSetRowMapper<Long> toLong(String alias) {
        return rsi -> rsi.getLongOrNull(alias);
    }

    public static ResultSetRowMapper<String> toString(Field field) {
        return toString(field.alias());
    }

    public static ResultSetRowMapper<String> toString(String alias) {
        return rsi -> rsi.getStringOrNull(alias);
    }

    public static ResultSetRowMapper<UUID> toUuid(Field field) {
        return toUuid(field.alias());
    }

    public static ResultSetRowMapper<UUID> toUuid(String alias) {
        return toString(alias).andThen(s -> mapIfNonNull(s, UUID::fromString));
    }

    public static ResultSetRowMapper<LocalDateTime> toLocalDateTime(Field field) {
        return toLocalDateTime(field.alias());
    }

    public static ResultSetRowMapper<LocalDateTime> toLocalDateTime(String alias) {
        return rsi -> rsi.getLocalDateTimeOrNull(alias);
    }

    public static ResultSetRowMapper<InputStream> toInputStream(Field field) {
        return rsi -> rsi.getInputStreamOrNull(field.alias());
    }

    public static ResultSetRowMapper<InputStream> toInputStream(String alias) {
        return rsi -> rsi.getInputStreamOrNull(alias);
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
