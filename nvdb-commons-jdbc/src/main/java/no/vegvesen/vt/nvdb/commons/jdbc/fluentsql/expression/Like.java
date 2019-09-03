package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Like implements Expression {
    private final Field operand;
    private final String pattern;

    public Like(Field operand, String pattern) {
        this.operand = requireNonNull(operand, "No operand specified");

        requireNonNull(pattern, "No pattern specified");
        this.pattern = pattern.contains("%") ? pattern : "%" + pattern + "%";
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " like ?";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " not like ?";
    }

    @Override
    public Stream<Object> params() {
        return Stream.of(pattern);
    }

    @Override
    public Stream<Field> fields() {
        return Stream.of(operand);
    }
}
