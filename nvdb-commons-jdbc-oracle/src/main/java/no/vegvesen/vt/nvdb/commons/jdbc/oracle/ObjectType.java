package no.vegvesen.vt.nvdb.commons.jdbc.oracle;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Sql;

public class ObjectType implements Sql {
    private final String name;

    public ObjectType(String name) {
        this.name = name;
    }

    public ObjectTypeField scalarField(String name) {
        return new ObjectTypeField(this, name);
    }

    public ObjectTypeObjectField objectField(String name, ObjectType fieldType) {
        return new ObjectTypeObjectField(this, name, fieldType);
    }

    @Override
    public String sql(Context context) {
        return name;
    }
}
