package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class IsNull implements Expression {
    private final Field operand;

    public IsNull(Field operand) {
        this.operand = requireNonNull(operand, "No operand specified");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " is null";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " is not null";
    }

    @Override
    public Stream<Object> params() {
        return Stream.empty();
    }

    @Override
    public Stream<Field> fields() {
        return Stream.empty();
    }
}
