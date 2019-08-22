package no.vegvesen.vt.nvdb.commons.jdbc.oracle;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

public class ObjectTable extends Table {

    private final ObjectType objectType;

    public ObjectTable(String name, ObjectType objectType) {
        super(name);
        this.objectType = objectType;
    }

    public ObjectTable(String name, String alias, ObjectType objectType) {
        super(name, alias);
        this.objectType = objectType;
    }

    public ObjectTypeObjectField objectField(String name, ObjectType fieldType) {
        return objectType.objectField(name, fieldType);
    }

    public ObjectTypeField scalarField(String name) {
        return objectType.scalarField(name);
    }
}
