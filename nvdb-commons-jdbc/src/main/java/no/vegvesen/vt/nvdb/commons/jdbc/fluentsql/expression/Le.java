package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Le implements Expression {
    private final Field operand;
    private final Object value;

    public Le(Field operand, Object value) {
        if (value instanceof Field) {
            throw new IllegalArgumentException("Use LeField instead");
        }
        this.operand = requireNonNull(operand, "No operand specified");
        this.value = requireNonNull(value, "No value specified");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " <= ?";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " > ?";
    }

    @Override
    public Stream<Object> params() {
        return Stream.of(value);
    }

    @Override
    public Stream<Field> fields() {
        return Stream.of(operand);
    }
}
