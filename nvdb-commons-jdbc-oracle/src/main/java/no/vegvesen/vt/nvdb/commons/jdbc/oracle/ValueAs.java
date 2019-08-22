package no.vegvesen.vt.nvdb.commons.jdbc.oracle;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;

import static java.util.Objects.requireNonNull;

/**
 * The Oracle "value() as" clause
 */
public class ValueAs<T extends ObjectType> implements ObjectFunction {
    private ObjectTable table;
    private T objectType;

    public ValueAs(ObjectTable table) {
        this.table = requireNonNull(table, "No table specified");
    }

    public ValueAs<T> as(T objectType) {
        this.objectType = requireNonNull(objectType, "No object type specified");
        return this;
    }

    public ObjectType objectType() {
        return objectType;
    }

    @Override
    public String sql(Context context) {
        return "value(" + table.alias() + ") as " + objectType.sql(context);
    }
}
