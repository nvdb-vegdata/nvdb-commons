package no.vegvesen.vt.nvdb.commons.jdbc.oracle;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import static java.util.Objects.requireNonNull;

public class ObjectTypeField implements Projection {
    private final ObjectType objectType;
    private final String name;
    private String alias;

    public ObjectTypeField(ObjectType objectType, String name) {
        this.objectType = objectType;
        this.name = name;
    }

    @Override
    public Projection as(String alias) {
        this.alias = requireNonNull(alias, "No alias specified");
        return this;
    }

    public ObjectType dot() {
        throw new UnsupportedOperationException("Can't dereference a primitive object field");
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return name;
    }
}
