package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.subquery.Subquery;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class EqSubquery implements Expression {
    private final Field operand;
    private final Subquery subquery;

    public EqSubquery(Field operand, Subquery subquery) {
        this.operand = requireNonNull(operand, "No operand specified");
        this.subquery = requireNonNull(subquery, "No sub query specified");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " = " + subquery.sql(context);
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " != " + subquery.sql(context);
    }

    @Override
    public Stream<Object> params() {
        return subquery.params().stream();
    }

    @Override
    public Stream<Field> fields() {
        return Stream.of(operand);
    }
}
