package no.vegvesen.vt.nvdb.commons.jdbc.oracle.datamodel;

import no.vegvesen.vt.nvdb.commons.jdbc.oracle.ObjectType;
import no.vegvesen.vt.nvdb.commons.jdbc.oracle.ObjectTypeField;

public class ColorType extends ObjectType {
    public ObjectTypeField RED;
    public ObjectTypeField GREEN;
    public ObjectTypeField BLUE;

    ColorType() {
        super("COLOR");

        RED = scalarField("RED");
        GREEN = scalarField("GREEN");
        BLUE = scalarField("BLUE");
    }
}
