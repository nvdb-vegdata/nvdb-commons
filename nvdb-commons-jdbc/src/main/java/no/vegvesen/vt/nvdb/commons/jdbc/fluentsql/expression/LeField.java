package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class LeField implements Expression {
    private final LeftOperand left;
    private final Field right;

    public LeField(LeftOperand left, Field right) {
        this.left = requireNonNull(left, "No left field specified");
        this.right = requireNonNull(right, "No right field specified");
    }

    @Override
    public String sql(Context context) {
        return left.sql(context) + " <= " + right.sql(context);
    }

    @Override
    public String negatedSql(Context context) {
        return left.sql(context) + " > " + right.sql(context);
    }

    @Override
    public Stream<Object> params() {
        return Stream.empty();
    }

    @Override
    public Stream<Field> fields() {
        return Stream.concat(left.fields(), Stream.of(right));
    }
}
