package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Helpers.paramMarkers;

public class In implements Expression {
    private final LeftOperand operand;
    private final Collection<?> values;

    public In(LeftOperand operand, Collection<?> values) {
        this.operand = requireNonNull(operand, "No operand specified");
        this.values = requireNonEmpty(values, "No values specified");
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " in (" + paramMarkers(values.size()) + ")";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " not in (" + paramMarkers(values.size()) + ")";
    }

    @Override
    public Stream<Object> params() {
        return values.stream().map(v -> v);
    }

    @Override
    public Stream<Field> fields() {
        return operand.fields();
    }
}
