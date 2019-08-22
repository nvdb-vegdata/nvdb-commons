package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Not implements Expression {
    private final Expression operand;

    Not(Expression operand) {
        this.operand = requireNonNull(operand, "No operand specified");
    }

    @Override
    public String sql(Context context) {
        return operand.negatedSql(context);
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context);
    }

    @Override
    public Stream<Object> params() {
        return operand.params();
    }

    @Override
    public Stream<Field> fields() {
        return operand.fields();
    }
}
