package no.vegvesen.vt.nvdb.commons.jdbc.oracle;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Expression;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;

/**
 * The Oracle "value() as" clause
 */
public class ValueIsOf implements Expression {
    private ObjectTable table;
    private ObjectType[] objectTypes;

    public ValueIsOf(ObjectTable table) {
        this.table = requireNonNull(table, "No table specified");
    }

    public ValueIsOf isOf(ObjectType... objectTypes) {
        this.objectTypes = requireNonEmpty(objectTypes, "No object types specified");
        return this;
    }

    @Override
    public String sql(Context context) {
        return "value(" + table.alias() + ") is of (" + Stream.of(objectTypes).map(ot -> ot.sql(context)).collect(joining(",")) + ")";
    }

    @Override
    public String negatedSql(Context context) {
        return sql(context).replace("is of", "is not of");
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
