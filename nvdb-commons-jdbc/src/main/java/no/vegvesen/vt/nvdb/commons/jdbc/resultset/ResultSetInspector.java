package no.vegvesen.vt.nvdb.commons.jdbc.resultset;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static no.vegvesen.vt.nvdb.commons.jdbc.resultset.ResultSetHelper.wrapSqlException;

public class ResultSetInspector {
    private final ResultSet rs;

    public static ResultSetInspector from(ResultSet rs) {
        return new ResultSetInspector(rs);
    }

    private ResultSetInspector(ResultSet rs) {
        this.rs = rs;
    }

    //
    // Boolean
    //

    public Optional<Boolean> getBoolean(Field field) {
        return getBoolean(field.alias());
    }

    public Boolean getBooleanOrNull(Field field) {
        return getBoolean(field).orElse(null);
    }

    public Optional<Boolean> getBoolean(String columnName) {
        return wrapSqlException(() -> {
            Boolean value = rs.getBoolean(columnName);
            return rs.wasNull() ? Optional.empty() : Optional.of(value);
        });
    }

    public Boolean getBooleanOrNull(String columnName) {
        return getBoolean(columnName).orElse(null);
    }

    //
    // Int
    //

    public Optional<Integer> getInt(Field field) {
        return getInt(field.alias());
    }

    public Integer getIntOrNull(Field field) {
        return getInt(field).orElse(null);
    }

    public Optional<Integer> getInt(String columnName) {
        return wrapSqlException(() -> {
            Integer value = rs.getInt(columnName);
            return rs.wasNull() ? Optional.empty() : Optional.of(value);
        });
    }

    public Integer getIntOrNull(String columnName) {
        return getInt(columnName).orElse(null);
    }

    public Optional<Integer> getInt(int columnIndex) {
        return wrapSqlException(() -> {
            Integer value = rs.getInt(columnIndex);
            return rs.wasNull() ? Optional.empty() : Optional.of(value);
        });
    }

    //
    // Long
    //

    public Optional<Long> getLong(Field field) {
        return getLong(field.alias());
    }

    public Long getLongOrNull(Field field) {
        return getLong(field).orElse(null);
    }

    public Optional<Long> getLong(String columnName) {
        return wrapSqlException(() -> {
            Long value = rs.getLong(columnName);
            return rs.wasNull() ? Optional.empty() : Optional.of(value);
        });
    }

    public Long getLongOrNull(String columnName) {
        return getLong(columnName).orElse(null);
    }

    public Optional<Long> getLong(int columnIndex) {
        return wrapSqlException(() -> {
            Long value = rs.getLong(columnIndex);
            return rs.wasNull() ? Optional.empty() : Optional.of(value);
        });
    }

    //
    // Double
    //

    public Optional<Double> getDouble(Field field) {
        return getDouble(field.alias());
    }

    public Double getDoubleOrNull(Field field) {
        return getDouble(field).orElse(null);
    }

    public Optional<Double> getDouble(String columnName) {
        return wrapSqlException(() -> {
            Double value = rs.getDouble(columnName);
            return rs.wasNull() ? Optional.empty() : Optional.of(value);
        });
    }

    public Double getDoubleOrNull(String columnName) {
        return getDouble(columnName).orElse(null);
    }

    //
    // String
    //

    public Optional<String> getString(Field field) {
        return getString(field.alias());
    }

    public String getStringOrNull(Field field) {
        return getString(field).orElse(null);
    }

    public Optional<String> getString(String columnName) {
        return wrapSqlException(() -> {
            String value = rs.getString(columnName);
            return Optional.ofNullable(value);
        });
    }

    public String getStringOrNull(String columnName) {
        return getString(columnName).orElse(null);
    }

    public Optional<String> getString(int columnIndex) {
        return wrapSqlException(() -> {
            String value = rs.getString(columnIndex);
            return Optional.ofNullable(value);
        });
    }

    //
    // UUID
    //

    public Optional<UUID> getUuid(Field field) {
        return getUuid(field.alias());
    }

    public UUID getUuidOrNull(Field field) {
        return getUuid(field).orElse(null);
    }

    public Optional<UUID> getUuid(String columnName) {
        return getString(columnName).map(UUID::fromString);
    }

    public UUID getUuidOrNull(String columnName) {
        return getUuid(columnName).orElse(null);
    }

    //
    // Enum
    //

    public <T extends Enum<T>> Optional<T> getEnum(Field field, Class<T> enumType) {
        return getString(field.alias()).map(s -> Enum.valueOf(enumType, s));
    }

    public <T extends Enum<T>> T getEnumOrNull(Field field, Class<T> enumType) {
        return getEnum(field, enumType).orElse(null);
    }

    public <T extends Enum<T>> Optional<T> getEnum(String columnName, Class<T> enumType) {
        return getString(columnName).map(s -> Enum.valueOf(enumType, s));
    }

    public <T extends Enum<T>> T getEnumOrNull(String columnName, Class<T> enumType) {
        return getEnum(columnName, enumType).orElse(null);
    }

    //
    // Enum (custom mapper)
    //

    public <T extends Enum<T>> Optional<T> getEnum(Field field, Function<String,T> mapper) {
        return getString(field.alias()).map(mapper);
    }

    public <T extends Enum<T>> T getEnumOrNull(Field field, Function<String,T> mapper) {
        return getEnum(field, mapper).orElse(null);
    }

    public <T extends Enum<T>> Optional<T> getEnum(String columnName, Function<String,T> mapper) {
        return getString(columnName).map(mapper);
    }

    public <T extends Enum<T>> T getEnumOrNull(String columnName, Function<String,T> mapper) {
        return getEnum(columnName, mapper).orElse(null);
    }

    //
    // Instant
    //

    public Optional<Instant> getInstant(Field field) {
        return getInstant(field.alias());
    }

    public Instant getInstantOrNull(Field field) {
        return getInstant(field).orElse(null);
    }

    public Optional<Instant> getInstant(String columnName) {
        return wrapSqlException(() -> {
            Timestamp value = rs.getTimestamp(columnName);
            return Optional.ofNullable(value).map(Timestamp::toInstant);
        });
    }

    public Instant getInstantOrNull(String columnName) {
        return getInstant(columnName).orElse(null);
    }

    //
    // LocalDateTime
    //

    public Optional<LocalDateTime> getLocalDateTime(Field field) {
        return getLocalDateTime(field.alias());
    }

    public LocalDateTime getLocalDateTimeOrNull(Field field) {
        return getLocalDateTime(field).orElse(null);
    }

    public Optional<LocalDateTime> getLocalDateTime(String columnName) {
        return wrapSqlException(() -> {
            Timestamp value = rs.getTimestamp(columnName);
            return Optional.ofNullable(value).map(Timestamp::toLocalDateTime);
        });
    }

    public LocalDateTime getLocalDateTimeOrNull(String columnName) {
        return getLocalDateTime(columnName).orElse(null);
    }

    //
    // Byte array
    //

    public Optional<byte[]> getBytes(Field field) {
        return getBytes(field.alias());
    }

    public byte[] getBytesOrNull(Field field) {
        return getBytes(field).orElse(null);
    }

    public Optional<byte[]> getBytes(String columnName) {
        return wrapSqlException(() -> {
            byte[] value = rs.getBytes(columnName);
            return Optional.ofNullable(value);
        });
    }

    public byte[] getBytesOrNull(String columnName) {
        return getBytes(columnName).orElse(null);
    }
}
