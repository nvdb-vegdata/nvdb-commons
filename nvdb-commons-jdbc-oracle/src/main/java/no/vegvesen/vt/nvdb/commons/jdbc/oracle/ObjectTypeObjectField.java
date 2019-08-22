package no.vegvesen.vt.nvdb.commons.jdbc.oracle;

public class ObjectTypeObjectField extends ObjectTypeField {
    private final ObjectType fieldType;

    public ObjectTypeObjectField(ObjectType objectType, String name, ObjectType fieldType) {
        super(objectType, name);
        this.fieldType = fieldType;
    }

    @Override
    public ObjectType dot() {
        return fieldType;
    }
}
