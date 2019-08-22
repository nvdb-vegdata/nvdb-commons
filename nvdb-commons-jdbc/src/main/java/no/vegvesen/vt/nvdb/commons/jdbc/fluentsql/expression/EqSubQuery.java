package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.SelectStatement;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class EqSubQuery implements Expression {
    private final Field operand;
    private final SelectStatement subQuery;

    public EqSubQuery(Field operand, SelectStatement subQuery) {
        this.operand = requireNonNull(operand, "No operand specified");
        this.subQuery = requireNonNull(subQuery, "No subquery specified");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " = (" + subQuery.sql(context) + ")";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " != (" + subQuery.sql(context) + ")";
    }

    @Override
    public Stream<Object> params() {
        return subQuery.params().stream();
    }

    @Override
    public Stream<Field> fields() {
        return Stream.of(operand);
    }
}
