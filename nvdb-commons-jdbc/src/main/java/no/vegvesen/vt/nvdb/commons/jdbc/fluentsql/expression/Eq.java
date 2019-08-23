package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Eq implements Expression {
    private final Field operand;
    private final Object value;

    public Eq(Field operand, Object value) {
        if (value instanceof Field) {
            throw new IllegalArgumentException("Use EqField for expressions with field operand");
        }
        this.operand = requireNonNull(operand, "No operand specified");
        this.value = requireNonNull(value, "Use IsNull for expressions with SQL NULL operand");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " = ?";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " != ?";
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
