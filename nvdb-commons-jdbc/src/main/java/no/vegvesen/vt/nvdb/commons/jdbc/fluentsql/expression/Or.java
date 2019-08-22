package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;

public class Or implements Expression {
    private final List<Expression> operands;

    Or(Expression... operands) {
        this.operands = asList(requireNonEmpty(operands, "No operands specified"));
    }

    @Override
    public String sql(Context context) {
        return "(" + operands.stream().map(e -> e.sql(context)).collect(joining(" or ")) + ")";
    }

    @Override
    public String negatedSql(Context context) {
        return "not " + sql(context);
    }

    @Override
    public Stream<Object> params() {
        return operands.stream().flatMap(Expression::params);
    }

    @Override
    public Stream<Field> fields() {
        return operands.stream().flatMap(Expression::fields);
    }
}
