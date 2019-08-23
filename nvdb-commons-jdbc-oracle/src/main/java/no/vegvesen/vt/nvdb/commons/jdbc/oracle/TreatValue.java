package no.vegvesen.vt.nvdb.commons.jdbc.oracle;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class TreatValue<T extends ObjectType> implements ObjectFunction, Projection {
    private ValueAs<T> valueAs;

    public TreatValue(ValueAs<T> valueAs) {
        this.valueAs = valueAs;
    }

    private String alias;

    @Override
    public Projection as(String alias) {
        this.alias = requireNonNull(alias, "No alias specified");
        return this;
    }

    /*public ObjectTypeDeref<T> dot(ObjectTypeField field) {
        return new ObjectTypeDeref(this, field);
    }*/

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "treat(" + valueAs.sql(context) + ")";
    }
}
